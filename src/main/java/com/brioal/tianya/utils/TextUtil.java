package com.brioal.tianya.utils;


/**
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/1/11.
 */

public class TextUtil {

    /**
     * 判断字符串是否可用
     *
     * @param str
     * @return
     */
    public static boolean isStringAvailableAddNotNull(String str) {
        if (str == null) {
            return false;
        }
        if (str.equals("")) {
            return false;
        }
        if (str.equals("null")) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否可用
     *
     * @param str
     * @return
     */
    public static boolean isStringAvailable(String str) {
        if (str == null) {
            return false;
        }
        if (str.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * 判断字符串是否错误
     *
     * @param str
     * @return
     */
    public static boolean isStringError(String str) {
        if (str == null) {
            return true;
        }
        if (str.equals("")) {
            return true;
        }
        return false;
    }


    /**
     * 是否全是英文
     *
     * @param str
     * @return
     */
    public boolean isAllEn(String str) {
        if (!isStringAvailable(str)) {
            return false;
        }
        return str.matches("[a-zA-Z]+");
    }

    /**
     * 是否全是中文
     *
     * @param str
     * @return
     */
    public boolean isAllChinese(String str) {
        if (!isStringAvailable(str)) {
            return false;
        }
        char[] chs = str.toCharArray();
        for (char ch : chs) {
            if (!isChinese(ch)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是中文字符
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
}
