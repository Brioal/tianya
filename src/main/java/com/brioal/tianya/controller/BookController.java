package com.brioal.tianya.controller;

import com.brioal.tianya.bean.BookBean;
import com.brioal.tianya.bean.QueryBean;
import com.brioal.tianya.bean.base.ResultBean;
import com.brioal.tianya.config.Config;
import com.brioal.tianya.repositorys.BookRepository;
import com.brioal.tianya.service.BookService;
import com.brioal.tianya.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2019/4/10.
 */
@RestController
@RequestMapping("/book")
public class BookController {

    private BookRepository mBookRepository;
    private BookService mBookService;

    private EmailService mEmailService;

    @Qualifier("emailServiceImpl")
    @Autowired
    public void setEmailService(EmailService emailService) {
        mEmailService = emailService;
    }

    @Qualifier("bookServiceImpl")
    @Autowired
    public void setBookService(BookService bookService) {
        mBookService = bookService;
    }

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        mBookRepository = bookRepository;
    }


    /**
     * 获取书本列表
     *
     * @param queryBean
     * @return
     */
    @RequestMapping("/list")
    public ResultBean getList(@RequestBody QueryBean queryBean) {
        Page<BookBean> page = mBookService.searchBook(queryBean.getKey(), queryBean.getIntValue1(), queryBean.getPageRequest());
        return ResultBean.returnPage(page);
    }

    /**
     * 获取当前的状态
     *
     * @return
     */
    @PostMapping("/state")
    public ResultBean state() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", Config.CRAW_CONFIG_BOOK_ID);
        map.put("menu", Config.CRAW_CONFIG_STATUS_MENU_ING);
        map.put("book", Config.CRAW_CONFIG_STATUS_BOOK_ING);
        map.put("book_name", Config.CRAW_CONFIG_BOOK_NAME);
        map.put("book_page", Config.CRAW_CONFIG_PAGE);
        return ResultBean.returnSuccess(map);
    }

    /**
     * 开始抓取书本
     *
     * @return
     */
    @PostMapping("/craw_menu")
    public ResultBean startCrawBook() {
        mBookService.crawMenu();
        return ResultBean.returnSuccess("");
    }

    /**
     * 开始抓取txt
     *
     * @return
     */
    @PostMapping("/craw_book/{id}")
    public ResultBean startCrawTxt(@PathVariable("id") int id) {
        mBookService.crawBook(id);
        return ResultBean.returnSuccess("");
    }

    /**
     * 推送内容
     *
     * @return
     */
    @PostMapping("/sender/{id}")
    public ResultBean senderFile(@PathVariable("id") int id) {
        mEmailService.sendFile(id);
        return ResultBean.returnSuccess("");
    }

    /**
     * 停止抓取书单
     *
     * @return
     */
    @PostMapping("/stop")
    public ResultBean stopMenu() {
        Config.refreshState();
        return ResultBean.returnSuccess("");
    }


    /**
     * 开始抓取内容
     *
     * @return
     */
    @PostMapping("/start_craw")
    public ResultBean stopTxt() {
        mBookService.crawNextNeededBook();
        return ResultBean.returnSuccess("");
    }
}
