package com.acrobat.ztb.model;

import com.acrobat.ztb.utils.JacksonUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * 保函流水记录
 * @author xutao
 * @date 2021-03-11 16:24
 */
@Data
@NoArgsConstructor
@TableName("transact_history")
public class TransactHistory implements Serializable {

    /* 项目名称（标段名称） */
    private String projectName;
    /* 项目编码（标段编码） */
    private String projectCode;
    /* 开标时间（投标截止时间），格式：yyyy-MM-dd HH:mm:ss */
    private String kbTime;
    /* 保函办理成功时间，格式：yyyy-MM-dd HH:mm:ss */
    private String transactTime;
    /* 投标人/投保人 */
    private String tbUnitName;

    /* 招标单位/受益人 */
    private String zbUnitName;
    /* 保费（浮点型数字），元 */
    private String baohanMoney;
    /* 发票金额（浮点型数字），元 */
    private String invoiceMoney;
    /* 申请编码/投保单号 */
    private String applicationNo;
    /* 保函编码/保单号 */
    private String baohanCode;
    /* 区域标识 */
    private String regionCode;
    /* 区域名称 */
    private String regionName;
    /* 产品名称 */
    private String productName;
    /* 出函机构 */
    private String orgName;

    public TransactHistory(String projectName, String projectCode, String kbTime, String transactTime) {
        this.projectName = projectName;
        this.projectCode = projectCode;
        this.kbTime = kbTime;
        this.transactTime = transactTime;
    }

    public static void main(String[] args) throws JsonProcessingException {
        List<TransactHistory> testList = new ArrayList<>();

        testList.add(new TransactHistory("工程1", "gc001", "2020-08-01 12:02:01", "2020-09-01 12:02:01"));
        testList.add(new TransactHistory("工程2", "gc002", "2020-08-01 12:02:01", "2020-09-01 12:02:01"));
        testList.add(new TransactHistory("工程3", "gc003", "2020-08-01 12:02:01", "2020-09-01 12:02:01"));
        String jsonData = JacksonUtil.writeValueAsString(testList);

        System.out.println(jsonData);
        System.out.println(Base64.getEncoder().encodeToString(jsonData.getBytes(Charset.forName("UTF-8"))));
    }
}
