package com.brioal.tianya.bean;

import com.brioal.tianya.bean.base.EntityBean;
import com.brioal.tianya.config.Config;
import com.brioal.tianya.utils.ConvertUtil;
import com.brioal.tianya.utils.SizeConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.File;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

/**
 * 书籍实体
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2019/4/10.
 */

@Entity
@Getter
@Setter
public class BookBean extends EntityBean {

    // 标题
    @Column(unique = true)
    private String title;
    // 作者
    private String people;
    // 阅读量
    private long readCount;
    // 评论量
    private long reviewCount;
    // 日期
    private String time;
    // 地址
    private String address;
    // 是否全部完成
    private boolean done;

    // 是否推送过了
    private boolean send;


    public String getSize() {
        if (title == null) {
            return "";
        }
        String path = Config.getFullFilePath(title);
        File file = new File(path);
        if (!file.exists()) {
            return "";
        }
        return SizeConverter.convertBytes(file.length(), true);
    }


}
