package com.acrobat.ztb.utils;

import com.acrobat.ztb.bean.web.Response;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * @author xutao
 * @date 2020-03-17 23:10
 */
public class ResponseUtil {


    public static String success() {
        Response response = new Response(0, "OK");
        return serialize(response);
    }

    public static String lack_param(String paramName) {
        Response response = new Response(1000, String.format("缺少必要的参数：%s", paramName));
        return serialize(response);
    }

    public static String username_existed() {
        Response response = new Response(1020, "用户名已存在!");
        return serialize(response);
    }

    public static String user_not_exist() {
        Response response = new Response(1021, "用户不存在!");
        return serialize(response);
    }

    public static String invalid_password() {
        Response response = new Response(1022, "密码不正确!");
        return serialize(response);
    }

    public static String unmatch_password() {
        Response response = new Response(1023, "两次输入的密码不匹配!");
        return serialize(response);
    }

    public static String invalid_nmap_target() {
        Response response = new Response(1040, "网段格式不正确!");
        return serialize(response);
    }

    public static String miss_scan_config() {
        Response response = new Response(1041, "找不到扫描配置信息!");
        return serialize(response);
    }

    public static String unmatch_sign() {
        Response response = new Response(2000, "签名不匹配!");
        return serialize(response);
    }

    // ----------------------------------------------------------------------------

    private static String serialize(Response response) {
        try {
            return JacksonUtil.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("jackson序列化异常", e);
        }
    }
}
