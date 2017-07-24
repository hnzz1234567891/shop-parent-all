package com.shop.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.exception.ParamException;
import com.shop.core.model.AdPosition;
import com.shop.dao.AdPositionDao;

@Service
public class AdPositionService {
	
	@Autowired
	private AdPositionDao adPositionDao;
	
	/**
	 * 根据ID获取广告位内容
	 * @param positionId
	 * @return
	 */
	public AdPosition findById(Integer positionId) {
		if (positionId == null || positionId < 1) {
			throw new ParamException("请选择广告位置");
		}
		AdPosition adPosition = adPositionDao.findById(positionId);
		return adPosition;
	}

	
}
