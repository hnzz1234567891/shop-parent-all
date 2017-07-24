package com.shsxt.smscenter.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.shop.core.exception.ParamException;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

@Component
public class SendSmsVerifycode {
	
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
	private String smsTemplateCode;

	private static Logger logger = LoggerFactory.getLogger(SendSmsVerifycode.class);

	public void listener (Map<String, String> params) {
		if (params == null) {
			return;
		}
		String verifyCode = params.get("verifyCode");
		String phone = params.get("phone");
		if (StringUtils.isBlank(verifyCode) || StringUtils.isBlank(phone)) {
			return;
		}
		// 发送验证码
		send(verifyCode, phone);
	}


	/**
	 * 发送
	 */
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

}
