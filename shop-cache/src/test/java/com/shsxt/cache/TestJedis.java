package com.shsxt.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class TestJedis {
	public static void main(String[] args) {
//		testOriginal();
		testPool();
	}
	
	public static void testOriginal() {
		Jedis jedis = new Jedis("localhost");
		jedis.auth("123456");
		jedis.set("abcd", "hahaha");
		String value = jedis.get("abcd");
		System.out.println(value);
	}
	
	@SuppressWarnings("resource")
	public static void testPool() {
		// 连接池配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(10); // 最大空闲数量
		config.setMaxTotal(100); // 最大的连接数
		config.setMaxWaitMillis(1000); // 链接的等待时间
		
		// 创建主服务器连接池
		JedisPool jedisPool = new JedisPool(config, "localhost", 6379, 1000, "123456");
		
		Jedis jedis = jedisPool.getResource();
		jedis.set("李秋晨", "李秋晨");
		
		// 从服务器连接池
		JedisPool slaveJedisPool = new JedisPool(config, "localhost", 6370, 1000, "123456");
		String value = slaveJedisPool.getResource().get("李秋晨");
		System.out.println(value);
	}
	
	
}
