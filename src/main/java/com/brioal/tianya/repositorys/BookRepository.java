package com.brioal.tianya.repositorys;

import com.brioal.tianya.bean.BookBean;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2019/4/10.
 */
@Repository
public interface BookRepository extends JpaRepository<BookBean, Integer> , JpaSpecificationExecutor<BookBean> {


    /**
     * 获取书籍列表
     *
     * @param readCount 阅读量阈值
     * @param pageable  分页,排序
     * @return
     */
    public Page<BookBean> findAllByReadCountGreaterThanEqualOrderByEditTimeDesc(long readCount, Pageable pageable);


    /**
     * 当前书籍是否已经存在
     *
     * @param title
     * @return
     */
    public boolean existsAllByTitleEquals(String title);

    /**
     * 寻找指定名称的书籍
     * @param title
     * @return
     */
    public BookBean findDistinctByTitleEquals(String title);
}
