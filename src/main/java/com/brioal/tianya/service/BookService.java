package com.brioal.tianya.service;

import com.brioal.tianya.bean.BookBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2019/4/11.
 */
@Service
public interface BookService {

    /**
     * 搜索内容
     *
     * @param key
     * @param read
     * @param pageable
     * @return
     */
    public Page<BookBean> searchBook(String key, long read, Pageable pageable);


    /**
     * 抓取书本列表
     */
    public void crawBook();

    /**
     * 抓取txt文本
     *
     * @param id
     */
    public void crawTxt(int id);

}
