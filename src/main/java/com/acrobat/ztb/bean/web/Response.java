package com.acrobat.ztb.bean.web;

import java.io.Serializable;

/**
 * 响应结果
 *
 * @author xutao
 * @date 2020-03-17 23:11
 */
public class Response implements Serializable {

    private Integer code;

    private String msg;

    public Response() {
    }

    public Response(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
