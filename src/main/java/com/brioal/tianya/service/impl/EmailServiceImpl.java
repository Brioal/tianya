package com.brioal.tianya.service.impl;

import com.brioal.tianya.bean.BookBean;
import com.brioal.tianya.config.Config;
import com.brioal.tianya.repositorys.BookRepository;
import com.brioal.tianya.service.EmailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2019/4/12.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private JavaMailSender mailSender;

    private BookRepository mBookRepository;

    @Autowired
    public void setBookRepository(BookRepository bookRepository) {
        mBookRepository = bookRepository;
    }

    @Autowired
    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * 推送内容
     *
     * @param id
     */
    @Async
    @Override
    public void sendFile(int id) {
        System.out.println("开始推送:");
        BookBean bookBean = mBookRepository.findById(id).get();
        if (bookBean == null) {
            return;
        }
        System.out.println("标题:"+bookBean.getTitle());
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mailMessage, true);
//            messageHelper.setTo("974918440@kindle.cn");
            messageHelper.setTo("brioal@kindle.cn");
            messageHelper.setFrom("brioal2019@163.com");
            messageHelper.setSubject("主题："+bookBean.getTitle());
            messageHelper.setText("附件");
            FileSystemResource file = new FileSystemResource(new File(Config.getFullFilePath(bookBean.getTitle())));
            //加入邮件
            messageHelper.addAttachment(bookBean.getTitle()+".txt", file);
            mailSender.send(mailMessage);
            bookBean.setSend(true);
            mBookRepository.save(bookBean);
            System.out.println("推送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            bookBean.setSend(false);
            mBookRepository.save(bookBean);
            System.out.println("推送失败!");
        }
    }
}
