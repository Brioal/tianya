package com.brioal.tianya.service;


import com.brioal.tianya.bean.FileBean;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/10/11.
 */

@Service
public interface FileService {

    /**
     * 读取文件夹htmls下的html文件内容
     *
     * @param name
     * @return
     */
    public String readHtmlContents(String name);


    // 保存文件
    public FileBean saveFile(MultipartFile file, boolean temp);

    // 修改文件
    public FileBean editFile(FileBean fileBean, MultipartFile file, boolean temp);

    /**
     * 保存文件
     *
     * @param file
     * @param temp
     * @return
     */
    public FileBean saveFile(File file, boolean temp);


    /**
     * 修改文件
     *
     * @param fileBean
     * @param file
     * @param temp
     * @return
     */
    public FileBean editFile(FileBean fileBean, File file, boolean temp);

    /**
     * 删除文件
     *
     * @param fileBean
     */
    public void delFile(FileBean fileBean);
}
