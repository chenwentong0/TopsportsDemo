package com.example.common.net.subscriber;

/**
 * @author by chen.wentong on 2018/8/17.
 * 基本数据结构
 */

public class BaseModel<T> {

    private T data;
    private String msg;
    private int code;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
