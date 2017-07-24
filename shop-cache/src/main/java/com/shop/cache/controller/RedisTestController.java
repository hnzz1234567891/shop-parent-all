package com.shop.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.cache.service.RedisTestService;
import com.shop.core.base.ResultInfo;

@RequestMapping("test")
@RestController
public class RedisTestController {
	
	@Autowired
	private RedisTestService redisTestService;
	
	@RequestMapping("set")
	public ResultInfo set(String key, String value) {
		redisTestService.set(key, value);
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setResult("Success");
		resultInfo.setResultCode(200);
		resultInfo.setResultMessage("Success");
		return resultInfo;
	}
	
	@RequestMapping("get")
	public ResultInfo get(String key) {
		String value = redisTestService.set(key);
		ResultInfo resultInfo = new ResultInfo();
		resultInfo.setResult(value);
		resultInfo.setResultCode(200);
		resultInfo.setResultMessage("Success");
		return resultInfo;
	}
	
}
