package com.brioal.tianya.bean.base;


import com.brioal.tianya.utils.ConvertUtil;
import com.brioal.tianya.utils.ListUtil;
import com.brioal.tianya.utils.TextUtil;

import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 全局的数据返回实体
 * email:brioal@foxmail.com
 * github:https://github.com/Brioal
 * Created by brioa on 2018/5/11.
 */
@Getter
@Setter
public class ResultBean implements Serializable {
    // 是否请求成功
    private boolean success = false;
    // 错误信息
    private String errorMsg = "";
    // 返回数据
    private Object data;
    // 错误码
    /**
     * 100 成功
     * 101 普通错误,没有指定的错误信息
     * 102 账号错误
     * 103 密码错误
     * 104 账号被禁用
     * 105 没有权限
     * 106 登录过期
     * 107 没有登录
     * 108 系统未开启
     */
    private int errorCode = 101;

    /**
     * 返回表单校验的错误
     * @param bindingResult
     * @return
     */
    public ResultBean returnValidateResult(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
        }
        StringBuffer stringBuffer = new StringBuffer();
        List<ObjectError> list =bindingResult.getAllErrors();
        for (ObjectError objectError:list) {
            stringBuffer.append(objectError.getDefaultMessage());
            stringBuffer.append("\n");
        }
        String str = stringBuffer.toString();
        if (!TextUtil.isStringAvailableAddNotNull(str)) {
            return null;
        }
        return ResultBean.returnFailed(str);
    }
    /**
     * 返回系统未开启的错误
     * @return
     */
    public static ResultBean returnSystemOffError() {
        return returnFailed("系统未开启", 108);
    }

    /**
     * 返回指定参数的数据
     * @param object
     * @param values
     * @return
     */
    public static ResultBean returnSuccess(Object object, String[] values) {
        return ResultBean.returnSuccess(ConvertUtil.returnNeedValue(object, values));
    }
    /**
     * 返回指定参数的数据
     * @param list
     * @param values
     * @return
     */
    public static ResultBean returnSuccess(List list, String[] values) {
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            result.add(ConvertUtil.returnNeedValue(list.get(i), values));
        }
        return ResultBean.returnSuccess(result);
    }

    /**
     * 返回未登录的错误
     *
     * @return
     */
    public static ResultBean returnLogin() {
        ResultBean resultBean = new ResultBean();
        resultBean.setErrorMsg("请先登录");
        resultBean.setErrorCode(107);
        return resultBean;
    }

    /**
     * 返回重新登录的错误
     *
     * @return
     */
    public static ResultBean returnRLogin() {
        ResultBean resultBean = new ResultBean();
        resultBean.setErrorMsg("登录过期,请重新登录");
        resultBean.setErrorCode(106);
        return resultBean;
    }

    /**
     * 返回没有权限的错误
     *
     * @return
     */
    public static ResultBean returnNotAllow() {
        ResultBean resultBean = new ResultBean();
        resultBean.setErrorMsg("没有权限进行此操作");
        resultBean.setErrorCode(105);
        return resultBean;
    }

    /**
     * 返回格式化之后的page
     *
     * @param list
     * @param total
     * @return
     */
    public static ResultBean returnFormatPage(List list, long total) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", list);
        map.put("totalElements", total);
        return returnSuccess(map);
    }
    /**
     * 返回格式化之后的page
     *
     * @param list
     * @param total
     * @return
     */
    public static ResultBean returnFormatPage(List list, long total,String [] needValues) {
        // 获取列表
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            result.add(ConvertUtil.returnNeedValue(list.get(i), needValues));
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("content", result);
        map.put("totalElements", total);
        return returnSuccess(map);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    // 返回成功的内容
    public static ResultBean returnSuccess(Object data) {
        ResultBean bean = new ResultBean();
        bean.setSuccess(true);
        bean.setData(data);
        bean.setErrorCode(100);
        return bean;
    }

    // 返回失败的内容
    public static ResultBean returnFailed(String errorMsg) {
        ResultBean bean = new ResultBean();
        bean.setSuccess(false);
        bean.setErrorMsg(errorMsg);
        bean.setData(null);
        bean.setErrorCode(101);
        return bean;
    }

    // 返回失败的内容
    public static ResultBean returnFailed(String errorMsg, int errorCode) {
        ResultBean bean = new ResultBean();
        bean.setSuccess(false);
        bean.setErrorMsg(errorMsg);
        bean.setData(null);
        bean.setErrorCode(errorCode);
        return bean;
    }

    // 返回报错信息
    public static ResultBean returnException(Exception e) {
        ResultBean bean = new ResultBean();
        bean.setErrorMsg(e.getMessage());
        return bean;
    }

    /**
     * 校验参数
     *
     * @param result
     * @return
     */
    public static ResultBean returnInvalidateResult(BindingResult result) {
        if (result.hasErrors()) {
            StringBuffer buffer = new StringBuffer();
            for (FieldError fieldError : result.getFieldErrors()) {
                System.out.println(fieldError.getDefaultMessage());
                buffer.append(fieldError.getDefaultMessage());
                buffer.append("\n");
            }
            return ResultBean.returnFailed(buffer.toString());
        }
        return null;
    }

    /**
     * 直接返回page列表
     *
     * @param page
     * @return
     */
    public static ResultBean returnPage(Page page) {
        ResultBean errorBean = ResultBean.returnFailed("列表为空");
        if (page == null) {
            return errorBean;
        }
        if (!ListUtil.isAvaliable(page.getContent())) {
            return errorBean;
        }
        return ResultBean.returnSuccess(page);
    }

    /**
     * 返回格式化之后的page数据
     * @param page
     * @param values
     * @return
     */
    public static ResultBean returnPage(Page page, String[] values) {
        HashMap<String, Object> map = new HashMap<>();
        // 转换列表
        List<HashMap<String, Object>> list = new ArrayList<>();
        if (page.getContent() == null || page.getContent().size() == 0) {
            return ResultBean.returnSuccess(null);
        }
        for (int i = 0; i < page.getContent().size(); i++) {
            list.add(ConvertUtil.returnNeedValue(page.getContent().get(i), values));
        }
        map.put("content", list);
        map.put("totalElements", page.getTotalElements());
        return returnSuccess(map);
    }

}
