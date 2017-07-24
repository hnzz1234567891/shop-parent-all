package com.shop.dao;

import org.apache.ibatis.annotations.Param;

import com.shop.core.model.AdPosition;

public interface AdPositionDao {
	
	AdPosition findById(@Param(value="positionId") Integer positionId);
	
}
