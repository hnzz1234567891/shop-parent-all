package com.shop.cache.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@SuppressWarnings("resource")
public class RedisConfig {
	
	@Value("${redis.maxIdle}")
	private Integer maxIdle;
	@Value("${redis.maxTotal}")
	private Integer maxTotal;
	@Value("${redis.maxWaitMills}")
	private Integer maxWaitMills;
	@Value("${redis.timeout}")
	private Integer timeout;
	
	@Value("${redis.masterHost}")
	private String masterHost;
	@Value("${redis.masterPort}")
	private Integer masterPort;
	@Value("${redis.masterPassword}")
	private String masterPassword;
	
	
	@Value("${redis.slaveHost}")
	private String slaveHost;
	@Value("${redis.slavePort}")
	private Integer slavePort;
	@Value("${redis.slavePassword}")
	private String slavePassword;
	
	@Bean(name="master.jedis")
	public Jedis initMasterJedis() {
		// 连接池配置
		JedisPoolConfig config = initJedisPoolConfig();
		
		// 创建主服务器连接池
		JedisPool jedisPool = new JedisPool(config, masterHost, masterPort, timeout, masterPassword);
		
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}
	
	@Bean(name="slave.jedis")
	public Jedis initSlaveJedis() {
		// 连接池配置
		JedisPoolConfig config = initJedisPoolConfig();
		
		// 创建主服务器连接池
		JedisPool jedisPool = new JedisPool(config, slaveHost, slavePort, timeout, slavePassword);
		
		Jedis jedis = jedisPool.getResource();
		return jedis;
	}
	
	private JedisPoolConfig initJedisPoolConfig() {
		// 连接池配置
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(maxIdle); // 最大空闲数量
		config.setMaxTotal(maxTotal); // 最大的连接数
		config.setMaxWaitMillis(maxWaitMills); // 链接的等待时间
		return config;
	}
	
}
