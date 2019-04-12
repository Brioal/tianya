package com.brioal.tianya.repositorys;


import com.brioal.tianya.bean.FileBean;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/10/11.
 */
@Repository
public interface FileRepository extends JpaRepository<FileBean, Integer> {
}
