package com.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
public class TestController {
	
	
	@RequestMapping("index")
	@ResponseBody
	public String index() {
		return "hello world";
	}
	
	@RequestMapping("index2")
	@ResponseBody
	public Map<String, Object> index2() {
		Map<String, Object> result = new HashMap<>();
		result.put("resultCode", 200);
		result.put("resultMessage", "Success");
		return result;
	}
	
	
}
