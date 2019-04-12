package com.brioal.tianya.bean;

import org.springframework.data.domain.PageRequest;

import lombok.Getter;
import lombok.Setter;

/**
 * 查询参数
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2019/4/10.
 */

@Getter
@Setter
public class QueryBean {
    // 当前页数
    private int page;
    // 页面大小
    private int size;
    // 排序方式
    private String sort;
    // 升序 降序
    private String order;
    // 关键字
    private String key;
    // 其他参数
    private int intValue1;


    public PageRequest getPageRequest() {
        return PageRequest.of(page, size);
    }
}
