package com.brioal.tianya.utils;

import java.util.Random;

/**
 * 用于随机的工具类
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/12/25.
 */

public class RandomUtil {
    public static final String SOURCES_FULL =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    public static final String SOURCES_NUM =
            "1234567890";

    /**
     * 生成随机的文件名拼接字符串
     *
     * @return
     */
    public static String randomFileNameAppend() {
        return randomStr(5);
    }

    /**
     * 生成随机的密码拼接字符串
     *
     * @return
     */
    public static String randomPasswordAppend() {
        return randomNumStr(4);
    }

    /**
     * 产生指定位数的随机字符串,包含数字,大小写
     *
     * @param size
     * @return
     */
    public static String randomStr(int size) {
        Random random = new Random();
        char[] text = new char[size];
        for (int i = 0; i < size; i++) {
            text[i] = SOURCES_FULL.charAt(random.nextInt(SOURCES_FULL.length()));
        }
        return new String(text);
    }

    /**
     * 产生指定位数的随机数字字符串
     *
     * @param size
     * @return
     */
    public static String randomNumStr(int size) {
        Random random = new Random();
        char[] text = new char[size];
        for (int i = 0; i < size; i++) {
            text[i] = SOURCES_NUM.charAt(random.nextInt(SOURCES_NUM.length()));
        }
        return new String(text);
    }

    /**
     * 返回一个随机的间隔时间 3~13秒
     *
     * @return
     */
    public static long randomTime() {
        Random random = new Random();
        long result = (long) (Math.random() * 10 * 1000);
        System.out.println("随机时间:" + result + 3000);
        return result + 3000;
    }


}
