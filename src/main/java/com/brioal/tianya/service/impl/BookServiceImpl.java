package com.brioal.tianya.service.impl;

import com.brioal.tianya.bean.BookBean;
import com.brioal.tianya.config.Config;
import com.brioal.tianya.repositorys.BookRepository;
import com.brioal.tianya.service.BookService;
import com.brioal.tianya.service.EmailService;
import com.brioal.tianya.utils.RandomUtil;
import com.brioal.tianya.utils.TextUtil;
import com.brioal.tianya.utils.TianyaDateFormatUtl;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2019/4/11.
 */
@Service
public class BookServiceImpl implements BookService {

    private BookRepository mBookRepository;

    private EmailService mEmailService;

    @Qualifier("emailServiceImpl")
    @Autowired
    public void setEmailService(EmailService emailService) {
        mEmailService = emailService;
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        mBookRepository = bookRepository;
    }

    /**
     * 搜索书
     *
     * @param key
     * @param read
     * @param pageable
     * @return
     */
    @Override
    public Page<BookBean> searchBook(String key, long read, Pageable pageable) {
        Page<BookBean> result = mBookRepository.findAll(new Specification<BookBean>() {
            @Override
            public Predicate toPredicate(Root<BookBean> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                // 关键字
                if (TextUtil.isStringAvailableAddNotNull(key)) {
                    list.add(cb.like(root.<String>get("title"), "%" + key + "%"));
                }
                // 阅读量
                list.add(cb.greaterThanOrEqualTo(root.<Long>get("readCount"), read));
                query.orderBy(cb.desc(root.get("readCount")));
                Predicate[] p = new Predicate[list.size()];
                return cb.and(list.toArray(p));
            }

        }, pageable);
        return result;
    }

    /**
     * 开始抓取书本
     */
    @Async
    @Override
    public void crawMenu() {
        // 设置状态为开始
        Config.CRAW_CONFIG_STATUS_MENU_ING = true;
        Config.CRAW_CONFIG_PAGE = 0;
        // 开始抓取
        String homeUrl = "http://bbs.tianya.cn/list-16-1.shtml";
        parseMenu(homeUrl);
    }

    /**
     * 抓取书本内容
     *
     * @param id
     */
    @Async
    @Override
    public void crawBook(int id) {
        BookBean bookBean = mBookRepository.findById(id).get();
        if (bookBean == null) {
            return;
        }
        String address = bookBean.getAddress();
        if (!TextUtil.isStringAvailableAddNotNull(address)) {
            return;
        }
        String title = bookBean.getTitle();
        String url = Config.getFullUrl(address);
        // 刷新时间
        TianyaDateFormatUtl.handleEdit(bookBean);
        mBookRepository.save(bookBean);
        // 设置状态
        Config.CRAW_CONFIG_STATUS_BOOK_ING= true;
        Config.CRAW_CONFIG_BOOK_ID= id;
        Config.CRAW_CONFIG_BOOK_NAME = bookBean.getTitle();
        Config.CRAW_CONFIG_PAGE = 0;
        // 创建文件/清空文件
        String filePath = Config.getFullFilePath(title);
        File file = new File(filePath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            parseContent(bookBean, url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 开始抓取文本内容
     *
     * @param bookBean
     * @param url
     */
    private void parseContent(BookBean bookBean, String url) {
        if (!Config.CRAW_CONFIG_STATUS_BOOK_ING) {
            // 手动停止了
            System.out.println("手动停止了");
            return;
        }
        Config.CRAW_CONFIG_PAGE++;
        String title = bookBean.getTitle();
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Elements bbs = doc.select("div[_host~=" + bookBean.getPeople() + "]");
            // 写入到文件
            File file = new File(Config.getFullFilePath(title));
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            for (Element element : bbs) {
                Element e_content = element.selectFirst(".bbs-content");
                if (e_content == null) {
                    continue;
                }
                String content = e_content.text();
                // 替换换行
                content = content.replaceAll(" 　　", "\r\n　　");
                // 添加前面的换行符
                content = "\r\n=================================================\n" + content;
                System.out.println(content);
                writer.write(content);
                writer.write("\n");
            }
            writer.close();
            // 获取下一页的位置
            Element e_next = doc.selectFirst(".js-keyboard-next");
            if (e_next == null) {
                Config.CRAW_CONFIG_STATUS_BOOK_ING = false;
                saveAndSend(bookBean);
                crawNextNeededBook();
                return;
            }
            String nextUrl = e_next.attr("href");
            if (!TextUtil.isStringAvailableAddNotNull(nextUrl)) {
                Config.CRAW_CONFIG_STATUS_BOOK_ING = false;
                saveAndSend(bookBean);
                crawNextNeededBook();
                return;
            }
            String fullUrl = Config.getFullUrl(nextUrl);
            System.out.println("下一页的文本:"+fullUrl);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    parseContent(bookBean, fullUrl);
                }
            }, RandomUtil.randomTime());

        } catch (Exception e) {
            e.printStackTrace();
            // 网络抓取失败,充实
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    parseContent(bookBean, url);
                }
            }, RandomUtil.randomTime());
        }
    }

    private void saveAndSend(BookBean bookBean) {
        bookBean.setDone(true);
        mBookRepository.save(bookBean);
        System.out.println("是否推送了:" + bookBean.isSend());
        if (bookBean.isSend()) {
            System.out.println("已经推送,跳过");
        } else {
            System.out.println("没有推送,开始推送");
            mEmailService.sendFile(Config.CRAW_CONFIG_BOOK_ID);
        }
    }

    /**
     * 开始下一本书的抓取
     */
    @Async
    @Override
    public void crawNextNeededBook() {
        System.out.println("没有下一页了,结束");
        System.out.println("抓取书本完成");
        System.out.println("寻找下一个没有抓取的文本");
        // 查询一个没有抓取的然后抓取
        BookBean nextBean = mBookRepository.findFirstByDoneEqualsOrderByReadCountDesc(false);
        crawBook(nextBean.getId());
    }

    /**
     * 解析菜单内容
     *
     * @param url
     */

    public void parseMenu(String url) {
        if (!Config.CRAW_CONFIG_STATUS_MENU_ING) {
            // 手动停止了
            return;
        }
        if (!TextUtil.isStringAvailableAddNotNull(url)) {
            Config.CRAW_CONFIG_STATUS_MENU_ING = false;
            return;
        }
        System.out.println("正在抓取目录");
        System.out.println("当前页数:" + Config.CRAW_CONFIG_PAGE);
        System.out.println("当前地址:" + url);
        try {
            Config.CRAW_CONFIG_PAGE++;
            Document doc = Jsoup.connect(url).get();
            // 获取所有的tr
            Elements trs = doc.select("tr");
            for (Element tr : trs) {
                // 获取所有的td
                Elements tds = tr.select("td");
                if (tds.size() < 5) {
                    // 说明不是文章
                    continue;
                }
                // 获取地址
                Element e_title = tds.get(0).selectFirst("a");
                if (e_title == null) {
                    continue;
                }
                String address = e_title.attr("href");
                System.out.println(address);
                // 获取标题
                String title = e_title.text();
                System.out.println(title);
                // 获取作者
                Element e_people = tds.get(1).selectFirst("a");
                if (e_people == null) {
                    continue;
                }
                String people = e_people.text();
                System.out.println(people);
                // 获取阅读量
                Element e_read = tds.get(2);
                if (e_read == null) {
                    continue;
                }
                long read = Long.parseLong(e_read.text());
                System.out.println(read);
                // 阅读量小于1w,则不抓取
                if (read < 10000) {
                    continue;
                }
                // 获取阅读李安
                Element e_review = tds.get(3);
                if (e_review == null) {
                    continue;
                }
                long review = Long.parseLong(e_review.text());
                System.out.println(review);
                // 获取更新时间
                Element e_time = tds.get(4);
                if (e_review == null) {
                    continue;
                }
                String time = e_time.attr("title");
                System.out.println(time);

                // 是否已经存在了
                title = title.trim();
                boolean exit = mBookRepository.existsAllByTitleEquals(title);
                BookBean bookBean = null;
                if (exit) {
                    // 更新
                    bookBean = mBookRepository.findDistinctByTitleEquals(title);
                    TianyaDateFormatUtl.handleEdit(bookBean);
                } else {
                    // 新建
                    bookBean = new BookBean();
                    TianyaDateFormatUtl.handleCreate(bookBean);
                    bookBean.setTitle(title.trim());
                }
                // 保存到数据库
                bookBean.setAddress(address.trim());
                bookBean.setPeople(people.trim());
                bookBean.setReadCount(read);
                bookBean.setReviewCount(review);
                bookBean.setTime(time.trim());

                mBookRepository.save(bookBean);

            }
            // 获取下一页的地址 href="/list.jsp?item=16&nextid=1554992100000"
            Element e_next = doc.selectFirst("a[href~=\\/list.jsp\\?item=16&nextid=.+]");
            if (e_next == null) {
                // 找不到下一页,结束
                Config.CRAW_CONFIG_STATUS_MENU_ING = false;

                return;
            }

            String nextAddress = e_next.attr("href");
            String fullUrl = Config.getFullUrl(nextAddress);
            // 之后再抓取
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    parseMenu(fullUrl);
                }
            }, RandomUtil.randomTime());

        } catch (Exception e) {
            e.printStackTrace();
            // 网络抓取失败,充实
            System.out.println("抓取目录失败,稍后重试");
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    parseMenu(url);
                }
            }, RandomUtil.randomTime());
        }
    }
}
