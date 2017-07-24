package com.shop.cache.service;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.cache.config.RedisClient;
import com.shop.cache.constant.Constant;
import com.shop.core.exception.ParamException;

@Service
public class HotSearchKeyService {
	
	@Autowired
	private RedisClient redisClient;
	
	/**
	 * 添加热门关键字
	 * @param keywords--e.g:三星,索尼
	 */
	public Long add(String keywords) {
		
		// 基本参数验证
		if (StringUtils.isBlank(keywords)) {
			throw new ParamException(101, "请输入热门的关键字");
		}
		String[] keywordArr = keywords.split(",");
		Long result = redisClient.setList(Constant.HOT_SEARCH_KEY, keywordArr);
		
		return result;
	}

	/**
	 * 获取热门关键字
	 * @return
	 */
	public List<String> get() {
		List<String> result = redisClient.getList(Constant.HOT_SEARCH_KEY);
		return result;
	}
}
