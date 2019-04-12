package com.brioal.tianya.bean;

import com.brioal.tianya.bean.base.EntityBean;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/10/11.
 */
@Entity
@Getter
@Setter
public class FileBean extends EntityBean {

    // 文件原名称
    private String name;

    // 文件路径
    private String path;

    // 文件大小
    private String size;


    // 是否是临时文件
    private boolean temp = false;


}
