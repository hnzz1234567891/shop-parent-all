package com.shsxt.controller;

import java.util.List;

import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import com.shop.core.base.ResultInfo;

public class TestRestTemplate {
	
	@Test
	public void testGet() {
		RestTemplate restTemplate = new RestTemplate();
		ResultInfo resultInfo = restTemplate.getForObject("http://localhost:8081/hot_search/get", ResultInfo.class);
		List<String> keywords = (List<String>) resultInfo.getResult();
		System.out.println(keywords.size());
	}
	
}
