package com.acrobat.ztb.controller;

import com.acrobat.ztb.model.TransactHistory;
import com.acrobat.ztb.service.TransactService;
import com.acrobat.ztb.utils.JacksonUtil;
import com.acrobat.ztb.utils.ResponseUtil;
import com.acrobat.ztb.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xutao
 * @date 2021-03-12 10:56
 */
@RequestMapping("/transact")
@Controller
public class TransactController {

    @Autowired
    private TransactService transactService;


    @ResponseBody
    @RequestMapping("/history/push.do")
    public String pushHistory(@RequestParam(name = "token", required = true) String token,
                              @RequestParam(name = "data", required = true) String data,
                              @RequestParam(name = "sign", required = true) String sign) throws IOException {
        // 检查签名
        Map<String, Object> params = new HashMap<>();
        params.put("token", token);
        params.put("data", data);
        if (!SignUtil.signAndCheck(params, sign)) {
            return ResponseUtil.unmatch_sign();
        }

        // 同时支持推送列表和单条记录
        String jsonData = new String(Base64.getDecoder().decode(data), "UTF-8");
        try {
            List<TransactHistory> histories = JacksonUtil.readListValues(jsonData, TransactHistory.class);
            transactService.saveBatch(histories, 1000);
        } catch (Exception e) {
            TransactHistory history = JacksonUtil.readValue(jsonData, TransactHistory.class);
            transactService.save(history);
        }

        return ResponseUtil.success();
    }
}
