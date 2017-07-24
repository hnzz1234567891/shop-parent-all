package com.shop.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.shop.core.base.BaseController;
import com.shop.core.base.ResultInfo;
import com.shop.core.constant.Constant;
import com.shop.core.exception.ParamException;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

/**
 * Created by TW on 2017/6/14.
 */
@RestController
@RequestMapping("sms")
public class SmsController extends BaseController {
/*
    @Value("${sms.url}")
    private String smsUrl;
    @Value("${sms.appkey}")
    private String smsAppKey;
    @Value("${sms.secret}")
    private String smsSecret;
    @Value("${sms.type}")
    private String smsType;
    @Value("${sms.free.sign.name}")
    private String smsFreeSignName;
    @Value("${sms.template.code}")
    private String smsTemplateCode;*/

    @Autowired
    private RabbitTemplate rabbitTemplate;


    private static Logger logger = LoggerFactory.getLogger(SmsController.class);

    @RequestMapping("send")
    public ResultInfo send(String phone, HttpServletRequest request) {
        if (StringUtils.isBlank(phone)) {
            return failure(Constant.ERROR_CODE, "请输入手机号码");
        }
        int verifyCode = (int) ((int)((Math.random() * 9 + 1) * 100000));
//        send(verifyCode + "", phone);

        // 存入队列
        Map<String, String> params = new HashMap<>();
        params.put("verifyCode", verifyCode + "");
        params.put("phone", phone);
        rabbitTemplate.convertAndSend(params);

        logger.info("手机验证码为：{}，打死也不要告诉别人", verifyCode);
        request.getSession().setAttribute(Constant.VERIFY_CODE_KEY, verifyCode + "");
        request.getSession().setMaxInactiveInterval(300);
        return success(Constant.SUCCESS_MSG);
    }

    /**
     * 发送

    private void send(String verifyCode, String phone) {
        TaobaoClient client = new DefaultTaobaoClient(smsUrl, smsAppKey, smsSecret);
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setSmsType(smsType);
        req.setSmsFreeSignName(smsFreeSignName);
        Map<String, String> param = new HashMap<>();
        param.put("verifyCode", verifyCode);
        req.setSmsParam(JSON.toJSONString(param));
        req.setRecNum(phone);
        req.setSmsTemplateCode(smsTemplateCode);
        try {
            AlibabaAliqinFcSmsNumSendResponse response = client.execute(req);
            logger.info("发送短信验证码结果：{}", JSON.toJSONString(response));
            if (!response.isSuccess()) {
                throw new ParamException("发送短信失败，请重试");
            }
        } catch (ApiException e) {
            logger.info("发送短信验证码异常：{}", e);
            throw new ParamException("发送短信失败，请重试");
        }
    }
     */
}
