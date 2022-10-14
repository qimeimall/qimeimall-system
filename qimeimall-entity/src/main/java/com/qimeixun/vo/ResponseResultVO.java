package com.qimeixun.vo;

import com.qimeixun.constant.StatusCodeConstants;
import com.qimeixun.ro.PageRO;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ResponseResultVO<T> implements Serializable {

    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 数据
     */
    private T data;


    public ResponseResultVO(int code, String message, T data) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public static ResponseResultVO resultList(List list, int count, PageRO page) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("count", count);
        map.put("pageSize", page.getPageSize());
        map.put("currentPage", page.getCurrentPage());
        return new ResponseResultVO(StatusCodeConstants.SUCCESS, "成功", map);
    }

    public static ResponseResultVO resultEmptyList(PageRO page) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", new ArrayList<>());
        map.put("count", 0);
        map.put("pageSize", page.getPageSize());
        map.put("currentPage", page.getCurrentPage());
        return new ResponseResultVO(StatusCodeConstants.SUCCESS, "成功", map);
    }

    /**
     * 对返回值的封装
     *
     * @param code
     * @param msg
     * @param object
     * @return
     */
    public static ResponseResultVO result(int code, String msg, Object object) {
        return new ResponseResultVO(code, msg, getData(object));
    }

    /**
     * 成功
     *
     * @param msg
     * @param object
     * @return
     */
    public static ResponseResultVO successResult(String msg, Object object) {
        return new ResponseResultVO(StatusCodeConstants.SUCCESS, msg, getData(object));
    }


    public static ResponseResultVO successResult() {
        return new ResponseResultVO(StatusCodeConstants.SUCCESS, "成功", new HashMap<>());
    }

    /**
     * 成功
     *
     * @param msg
     * @return
     */
    public static ResponseResultVO successResult(String msg) {
        return new ResponseResultVO(StatusCodeConstants.SUCCESS, msg, getData(new HashMap<>()));
    }

    /**
     * 成功
     *
     * @param object
     * @return
     */
    public static ResponseResultVO successResult(Object object) {
        return new ResponseResultVO(StatusCodeConstants.SUCCESS, "成功", getData(object));
    }

    /**
     * 成功
     *
     * @param list
     * @return
     */
    public static ResponseResultVO successResult(List list) {
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        return new ResponseResultVO(StatusCodeConstants.SUCCESS, "成功", map);
    }

    /**
     * 失败
     *
     * @param msg
     * @param object
     * @return
     */
    public static ResponseResultVO failResult(String msg, Object object) {
        return new ResponseResultVO(StatusCodeConstants.FAIL, msg, getData(object));
    }

    public static ResponseResultVO failResult() {
        return new ResponseResultVO(StatusCodeConstants.FAIL, "失败", new HashMap<>());
    }

    /**
     * 失败
     *
     * @param msg
     * @return
     */
    public static ResponseResultVO failResult(String msg) {
        return new ResponseResultVO(StatusCodeConstants.FAIL, msg, getData(new HashMap<>()));
    }


    /**
     * 失败
     *
     * @param msg
     * @return
     */
    public static ResponseResultVO failNoLoginResult(String msg) {
        return new ResponseResultVO(StatusCodeConstants.NOLOGINFAIL, msg, getData(new HashMap<>()));
    }

    public static ResponseResultVO errorResult() {
        return new ResponseResultVO(StatusCodeConstants.ERROR, "系统错误", getData(new HashMap<>()));
    }

    private static Object getData(Object object) {
        if (object == null) {
            return new HashMap<>();
        } else {
            return object;
        }
    }


}
