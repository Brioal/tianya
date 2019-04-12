package com.brioal.tianya.service.impl;


import com.brioal.tianya.bean.FileBean;
import com.brioal.tianya.config.Config;
import com.brioal.tianya.repositorys.FileRepository;
import com.brioal.tianya.service.FileService;
import com.brioal.tianya.utils.ReviewFileUtils;
import com.brioal.tianya.utils.SizeConverter;
import com.brioal.tianya.utils.TianyaDateFormatUtl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/10/11.
 */
@Service
public class FileServiceImpl implements FileService {


    FileRepository mFileRepository;

    @Autowired
    public void setFileRepository(FileRepository fileRepository) {
        mFileRepository = fileRepository;
    }


    /**
     * 读取文件夹htmls下面的指定html 的内容
     *
     * @param name
     * @return
     */
    @Override
    public String readHtmlContents(String name) {
        String path = ReviewFileUtils.getTemplateFilePath("htmls/" + name);
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            String str = "";
            StringBuffer stringBuffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                stringBuffer.append(str);
            }
            return stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 保存文件
     *
     * @param file
     * @return
     */
    @Override
    public FileBean saveFile(MultipartFile file, boolean temp) {
        FileBean fileBean = new FileBean();
        // 保存文件
        File targetFile = ReviewFileUtils.saveRegularFileAndReturnPath(file);
        return saveFile(targetFile, temp);
    }

    /**
     * 编辑文件
     *
     * @param fileBean
     * @param file
     * @param temp
     * @return
     */
    @Override
    public FileBean editFile(FileBean fileBean, MultipartFile file, boolean temp) {
        String oldPath = fileBean.getPath();
        // 先保存文件
        File targetFile = ReviewFileUtils.saveRegularFileAndReturnPath(file);
        fileBean.setPath(ReviewFileUtils.getRelativePath(targetFile));
        // 保存修改时间
        fileBean.setEditTime(TianyaDateFormatUtl.getCurrentTime());
        // 保存文件大小
        String fileSize = SizeConverter.convertBytes(file.getSize(), true);
        fileBean.setSize(fileSize);
        // 是否是临时文件
        fileBean.setTemp(temp);
        // 保存文件
        FileBean result = mFileRepository.save(fileBean);
        // 删除原有文件
        File oldFile = new File(Config.getFullLocalPath(oldPath));
        if (oldFile.exists()) {
            oldFile.delete();
        }
        return result;
    }

    /**
     * 新建一个FileBean
     *
     * @param file
     * @param temp
     * @return
     */
    @Override
    public FileBean saveFile(File file, boolean temp) {
        FileBean fileBean = new FileBean();
        // 保存文件名
        fileBean.setName(file.getName());
        // 获取相对的路径
        fileBean.setPath(ReviewFileUtils.getRelativePath(file));
        // 保存创建时间
        fileBean.setCreateTime(TianyaDateFormatUtl.getCurrentTime());
        // 保存修改时间
        fileBean.setEditTime(TianyaDateFormatUtl.getCurrentTime());
        // 保存文件大小
        String fileSize = SizeConverter.convertBytes(file.length(), true);
        fileBean.setSize(fileSize);
        // 是否是临时文件
        fileBean.setTemp(temp);
        // 保存文件
        FileBean result = mFileRepository.save(fileBean);
        return result;
    }

    @Override
    public FileBean editFile(FileBean fileBean, File file, boolean temp) {
        // 原有文件路径
        String oldPath = fileBean.getPath();
        // 保存文件
        fileBean.setName(file.getName());
        fileBean.setPath(ReviewFileUtils.getRelativePath(file));
        // 保存修改时间
        fileBean.setEditTime(TianyaDateFormatUtl.getCurrentTime());
        // 保存文件大小
        String fileSize = SizeConverter.convertBytes(file.length(), true);
        fileBean.setSize(fileSize);
        // 是否是临时文件
        fileBean.setTemp(temp);
        // 保存文件
        FileBean result = mFileRepository.save(fileBean);
        // 删除原有文件
        File oldFile = new File(Config.getFullLocalPath(oldPath));
        if (oldFile.exists()) {
            oldFile.delete();
        }
        return result;
    }

    /**
     * 删除文件
     *
     * @param fileBean
     */
    @Override
    public void delFile(FileBean fileBean) {
        if (fileBean == null) {
            return;
        }
        try {
            // 删除物理文件
            File file = new File(Config.getFullLocalPath(fileBean.getPath()));
            if (file.exists()) {
                file.delete();
            }
            // 删除数据库数据
            mFileRepository.deleteById(fileBean.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
