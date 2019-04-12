package com.brioal.tianya.service;

import org.springframework.stereotype.Service;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2019/4/12.
 */
@Service
public interface EmailService {

    /**
     * 发送文件
     * @param id
     */
    void sendFile(int id);
}
