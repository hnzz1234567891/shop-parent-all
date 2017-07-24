package com.shsxt.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shop.cache.config.RedisClient;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application.xml"})
public class TestRedisClient {
	
	@Autowired
	private RedisClient redisClient;
	
	@Test
	public void testSet() {
		redisClient.set("况任阳1", "况任阳1");
	}
	
	@Test
	public void testGet() {
		String value = redisClient.get("况任阳1");
		System.out.println("value==" + value);
	}
	
}
