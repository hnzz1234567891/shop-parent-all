package com.shop.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.cache.config.RedisClient;

@Service
public class RedisTestService {
	
	@Autowired
	private RedisClient redisClient;
	
	public void set(String key, String value) {
		redisClient.set(key, value);
	}

	public String set(String key) {
		
		return redisClient.get(key);
	}

}
