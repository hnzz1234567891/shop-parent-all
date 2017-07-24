package com.shsxt.cache;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:application.xml"})
public class TestRedis {
	
//	@Autowired
//	@Qualifier(value="master.jedis")
	@Resource(name="master.jedis")
	private Jedis masterJedis;
	@Resource(name="slave.jedis")
	private Jedis slaveJedis;
	
	@Test
	public void testSet() {
		masterJedis.set("况任阳1", "况任阳1");
	}
	
	@Test
	public void testGet() {
		String value = slaveJedis.get("况任阳1");
		System.out.println("value==" + value);
	}
	
}
