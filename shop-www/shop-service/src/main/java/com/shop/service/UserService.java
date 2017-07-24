package com.shop.service;

import java.util.ArrayList;
import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.miemiedev.mybatis.paginator.domain.Paginator;
import com.shop.core.base.BaseDto;
import com.shop.core.dto.MemberDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.model.User;
import com.shop.dao.UserDao;

@Service
public class UserService {
	
	@Autowired
	private UserDao userDao;

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	public Integer add(User user) {
		// 参数验证
		// TODO
		// 用户名唯一验证
		// TODO
		int count = userDao.insert(user);
		System.out.println("插入记录数：" + count);
		
		return user.getUserId();
	}
	
	public PageList<User> find(String uname, BaseDto baseDto) {

		PageList<User> users = userDao.find(uname, baseDto.buildPageBounds());

		//获得分页结果集
		Paginator paginator = users.getPaginator();
		logger.info("paginator:{}", paginator);
		return users;
	}
}
