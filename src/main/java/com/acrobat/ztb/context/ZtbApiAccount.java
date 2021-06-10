package com.acrobat.ztb.context;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 招投标平台定期通过接口向我方推送数据
 * 我方开设账号
 * @author xutao
 * @date 2021-03-11 16:35
 */
@Data
@NoArgsConstructor
public class ZtbApiAccount implements Serializable {

    /* 账号标识 */
    public static String token = "ztbPlatform";
    /* MD5加盐 */
    public static String key = "58C4605CE03C4112836C189CCD7F14B8";

}
