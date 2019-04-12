package com.brioal.tianya.utils;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * 一些常用的转换工具类
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/10/23.
 */

public class ConvertUtil {

    /**
     * 格式化小数
     *
     * @param d
     * @return
     */
    public static String formatDouble4(double d) {
        if (d == 0) {
            return "0";
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
    }

    // 转换需要的参数到HashMap
    public static HashMap<String, Object> returnNeedValue(Object object, String[] methods) {
        HashMap<String, Object> map = null;
        try {
            Class<?> userClass = Class.forName(object.getClass().getName());
            map = new HashMap<>();
            // 循环参数
            for (int i = 0; i < methods.length; i++) {
                String methodName = methods[i];
                String getName = methodName;
                // 首字母大写
                getName = (new StringBuilder()).append(Character.toUpperCase(getName.charAt(0))).append(getName.substring(1)).toString();
                // 获取get方法
                try {
                    Method method = userClass.getMethod("get" + getName);//得到方法对象
                    Object value = method.invoke(object);
                    map.put(methodName, value);
                } catch (Exception e) {
                }
                // 获取is 方法
                try {
                    Method method = userClass.getMethod("is" + getName);//得到方法对象
                    Object value = method.invoke(object);
                    map.put(methodName, value);
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 返回正确的等级要求
     *
     * @param require
     * @return
     */
    public static String getDegreeRequire(int require) {
        switch (require) {
            case 0:
                return "服从结果";
            case 1:
                return "一等奖";
            case 2:
                return "二等奖及以上";
        }
        return "";
    }

    /**
     * 返回评审组名称
     *
     * @param type
     * @return
     */
    public static String getParentReviewGroupName(int type) {
        switch (type) {
            case 1:
                return "科技功臣评审组";
            case 2:
                return "自然科学评审组";
            case 3:
                return "技术发明评审组";
            case 4:
                return "科技进步评审组";
            case 5:
                return "企业评审组";
            case 6:
                return "企业评审组";
        }
        return "";
    }

    /**
     * 返回奖种名称
     *
     * @param awardType
     * @return
     */
    public static String convertAwardName(int awardType) {
        switch (awardType) {
            case 1:
                return "科技功臣奖";
            case 2:
                return "自然科学奖";
            case 3:
                return "技术发明奖";
            case 4:
                return "科技进步奖";
            case 5:
                return "企业家奖";
            case 6:
                return "企业奖";
            default:
                return "未知";
        }
    }

    /**
     * 转换性别
     *
     * @param sex
     * @return
     */
    public static String convertSex(int sex) {
        switch (sex) {
            case 0:
                return "男";
            case 1:
                return "女";
            default:
                return "男";
        }
    }
}
