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
     * 刷新状态
     */
    public static void refreshState() {
        Config.CRAW_CONFIG_BOOK_ID = -1;
        Config.CRAW_CONFIG_PAGE = 0;
        Config.CRAW_CONFIG_BOOK_NAME = "";
        Config.CRAW_CONFIG_STATUS_BOOK_ING = false;
        Config.CRAW_CONFIG_STATUS_MENU_ING = false;
    }
    /**
     * 书本的id
     */
    public static int CRAW_CONFIG_BOOK_ID = -1;

    /**
     * 当前抓取的书本的页数
     */
    public static int CRAW_CONFIG_PAGE = 0;


    /**
     * 当前抓取的书本的名称
     */
    public static String CRAW_CONFIG_BOOK_NAME = "";


    /**
     * 当前是否正在抓取书本
     */
    public static boolean CRAW_CONFIG_STATUS_BOOK_ING = false;


    /**
     * 当前是否正在抓取书单
     */
    public static boolean CRAW_CONFIG_STATUS_MENU_ING = false;


    /**
     * 项目的主目录
     */
    public static String PROJECT_DIR = "/Tianya";

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


}
