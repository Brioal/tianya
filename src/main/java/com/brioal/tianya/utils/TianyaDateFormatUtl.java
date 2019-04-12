package com.brioal.tianya.utils;


import com.brioal.tianya.bean.base.EntityBean;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TianyaDateFormatUtl {

    /**
     * 给数据实体设置创建时间
     *
     * @param entityBean
     * @return
     */
    public static void handleEdit(EntityBean entityBean) {
        entityBean.setCreateTime(getCurrentTime());
        entityBean.setEditTime(getCurrentTime());
    }

    /**
     * 给数据实体设置创建时间
     *
     * @param entityBean
     * @return
     */
    public static void handleCreate(EntityBean entityBean) {
        entityBean.setCreateTime(getCurrentTime());
        entityBean.setEditTime(getCurrentTime());
    }


    /**
     * 返回当前的时间
     *
     * @return
     */
    public static Timestamp getCurrentTime() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获取当前年份
     *
     * @return
     */
    public static int currentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 格式转换
     *
     * @param timestamp
     * @param format
     * @return
     */
    public static String formatDate(Timestamp timestamp, String format) {
        SimpleDateFormat util = new SimpleDateFormat(format);
        return util.format(timestamp);
    }

    /**
     * 格式转换
     *
     * @param str
     * @param format
     * @return
     */
    public static Timestamp parseDate(String str, String format) {
        try {
            SimpleDateFormat util = new SimpleDateFormat(format);
            Date date = util.parse(str);
            return new Timestamp(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析日期
     * @param str
     * @return
     */
    public static Timestamp parseDateAndTime(String str) {
        return parseDate(str, "yyyy-MM-dd hh:mm:ss");
    }


    /**
     * 返回完整的日期
     *
     * @param timestamp
     * @return
     */
    public static String formatFullDate(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        return formatDate(timestamp, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 返回今天的日期用作文件的路径
     *
     * @return
     */
    public static String getTodayDateForFilePath() {
        String result = formatDate(new Timestamp(System.currentTimeMillis()), "yyyy_MM_dd");
        return result;
    }

    /**
     * 获取当前的完整时间 yyyy-MM-dd mm:HH:ss
     *
     * @return
     */
    public static String getDetailDate() {
        return formatFullDate(getCurrentTime());
    }
}
