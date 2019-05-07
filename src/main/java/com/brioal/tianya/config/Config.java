package com.brioal.tianya.config;

import java.io.File;

/**
 * 参数配置i文件
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by Brioal on 2018/4/20.
 */

public class Config {


    /**
     * 是否正在抓取书单
     */
    public static boolean IS_MENU_CRAWING = false;
    /**
     * 是否正在抓取书本
     */
    public static boolean IS_TXT_CRAWING = false;

    /**
     * 正在抓取的书本的id
     */
    public static int TXT_ID = -1;

    /**
     * 当年页数
     */
    public static int CURRENT_INDEX = 0;

    /**
     * 项目的主目录
     */
    public static String PROJECT_DIR = "/Tianya";
    /**
     * 普通文件的目录名称
     */
    public static String REGULAR_FILE_DIR_NAME = "saveFiles";
    /**
     * 模板文件的存放位置
     */
    public static String TEMPLATE_FILE_DIR_NAME = "templates";

    /**
     * 首页url
     */
    public static String URL_HOME = "http://bbs.tianya.cn";

    /**
     * 返回完整的url路径
     *
     * @param url
     * @return
     */
    public static String getFullUrl(String url) {
        return URL_HOME + url;
    }

    /**
     * 获取文件的全路径
     *
     * @param title
     * @return
     */
    public static String getFullFilePath(String title) {
        File dir = new File("/txts");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return "/txts/" + title + ".txt";
    }

    /**
     * 返回文件全路径
     *
     * @param path
     * @return
     */
    public static String getFullLocalPath(String path) {
        return PROJECT_DIR + "/" + path;
    }

    /**
     * 邮箱发送地址，
     * 以后需要改变
     */
    public static String SEND_EMAIL_ADDRESS = "gspcc2018@163.com";

}
