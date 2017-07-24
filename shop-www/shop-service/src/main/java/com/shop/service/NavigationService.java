package com.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.exception.ParamException;
import com.shop.core.model.Navigation;
import com.shop.dao.NavigationDao;

@Service
public class NavigationService {
	
	@Autowired
	private NavigationDao navigationDao;
	
	/**
	 * 查询顶部导航
	 * @return
	 */
	public List<Navigation> find(Integer position) {
		// 非空判断
		if (position == null) {
			throw new ParamException("请输入导航位置");
		}
		List<Navigation> navigations = navigationDao.find(position);
		return navigations;
	}

}
