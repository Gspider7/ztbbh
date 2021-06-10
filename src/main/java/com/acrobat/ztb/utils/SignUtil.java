package com.acrobat.ztb.utils;

import com.acrobat.ztb.context.ZtbApiAccount;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 签名工具
 * @author xutao
 * @date 2021-03-12 11:03
 */
@Slf4j
public class SignUtil {


    public static String sign(Map<String, Object> params) {
        // 按参数名字典升序排列
        List<String> keyList = new ArrayList<>(params.keySet());
        Collections.sort(keyList);

        // 拼接
        StringBuilder sb = new StringBuilder();
        keyList.forEach(key -> {
            sb.append(key).append("=").append(params.get(key));
            sb.append("&");
        });
        sb.append("key=").append(ZtbApiAccount.key);

        // MD5
        try {
            return DigestUtils.md5DigestAsHex(sb.toString().getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("MD5编码失败：不支持的字符编码方式", e);
            return null;
        }
    }

    public static boolean signAndCheck(Map<String, Object> params, String sign) {
        return signAndCheck(params, sign, false);
    }

    public static boolean signAndCheck(Map<String, Object> params, String sign, boolean caseMatch) {
        String genSign = sign(params);
        if (caseMatch) {
            return sign.equals(genSign);
        }
        return sign.equalsIgnoreCase(genSign);
    }

    public static void main(String[] args) throws JsonProcessingException {
        List<String> testList = Arrays.asList("bb", "aa", "kk");
        Collections.sort(testList);

        log.info(JacksonUtil.writeValueAsString(testList));
    }
}
