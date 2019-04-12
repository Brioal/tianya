package com.brioal.tianya.bean.base;

import com.brioal.tianya.utils.TianyaDateFormatUtl;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

/**
 * 所有需要保存到数据库的实体要继承的父类
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/10/19.
 */
@MappedSuperclass
@Getter
@Setter
public class EntityBean implements Serializable {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // 创建时间
    @JsonIgnore
    private Timestamp createTime = TianyaDateFormatUtl.getCurrentTime();

    // 修改时间
    @JsonIgnore
    private Timestamp editTime = null;

    /**
     * 返回修改时间
     *
     * @return
     */
    public String getCreateTimeStr() {
        return TianyaDateFormatUtl.formatFullDate(createTime);
    }

    /**
     * 返回修改时间
     *
     * @return
     */
    public String getEditTimeStr() {
        return TianyaDateFormatUtl.formatFullDate(editTime);
    }

}
