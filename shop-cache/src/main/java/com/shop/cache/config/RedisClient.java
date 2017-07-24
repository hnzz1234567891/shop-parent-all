package com.shop.cache.config;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


import redis.clients.jedis.Jedis;

@Component
public class RedisClient {
	
	@Resource(name="master.jedis")
	private Jedis masterJedis;
	@Resource(name="slave.jedis")
	private Jedis slaveJedis;
	
	/**
	 * 操作String：set
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
			return;
		}
		masterJedis.set(key, value);
	}
	
	/**
	 * 操作String：get
	 * @param key
	 * @return
	 */
	public String get(String key) {
		if (StringUtils.isBlank(key)) {
			return "";
		}
		String value = slaveJedis.get(key);
		return value;
	}
	
	/**
	 * 操作list：set
	 */
	public Long setList(String key, String...values) {
		
		if (StringUtils.isBlank(key) || values == null || values.length < 1) {
			return 0L;
		}
		return masterJedis.rpush(key, values);
	}
	
	/**
	 * 操作list: get
	 * @return 
	 */
	public List<String> getList(String key) {
		if (StringUtils.isBlank(key)) {
			return Collections.emptyList();
		}
		List<String> values = slaveJedis.lrange(key, 0, -1);
		return values;
	}
	
}
