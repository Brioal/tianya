package com.brioal.tianya.utils;


import java.util.List;

/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by Brioal on 2017/8/4.
 */

public class ListUtil {
    /**
     * 判断list是否可用 对象 + 数量
     *
     * @param list
     * @return
     */
    public static boolean isAvaliable(List list) {
        if (list == null) {
            return false;
        }
        if (list.size() == 0) {
            return false;
        }
        return true;
    }

    /**
     * 判断list是否可用 对象 + 数量
     *
     * @param list
     * @return
     */
    public static boolean empty(List list) {
        if (list == null) {
            return true;
        }
        if (list.size() == 0) {
            return true;
        }
        return false;
    }

}
