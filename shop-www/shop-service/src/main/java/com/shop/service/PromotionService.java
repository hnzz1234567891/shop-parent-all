package com.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.exception.ParamException;
import com.shop.core.model.Promotion;
import com.shop.dao.PromotionDao;

@Service
public class PromotionService {
	
	@Autowired
	private PromotionDao promotionDao;

	public List<Promotion> findList(Integer productCategoryId, Integer count) {
		// 基本参数验证
//		if (productCategoryId == null || productCategoryId < 1) {
//			throw new ParamException("请选择分类");
//		}
		if (count == null) {
			count = 1;
		}
		List<Promotion> promotions = null;
		if(productCategoryId == null || productCategoryId < 1) {
			promotions = promotionDao.findAllList(count);
		} else {
			promotions = promotionDao.findList(productCategoryId, count);
		}
		return promotions;
	}
	
}
