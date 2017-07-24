package com.shsxt.controller;

import com.alibaba.fastjson.JSON;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * Created by TW on 2017/6/16.
 */
public class TestSms {

    public static void main(String[] args) {
        String url = "https://eco.taobao.com/router/rest";
        String appkey = "23560540";
        String secret = "cb6ebff7fcdc193349497f578905b9b4";
        TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType("normal");
        req.setSmsFreeSignName("尚HAI购网");
        req.setSmsParam("{\"verifyCode\":\"23234234\"}");
        req.setRecNum("17682488537");
        req.setSmsTemplateCode("SMS_39340142");
        try {
            AlibabaAliqinFcSmsNumSendResponse response = client.execute(req);
            System.out.println(JSON.toJSONString(response));
        }catch (ApiException e) {
            e.printStackTrace();
        }

    }


}
