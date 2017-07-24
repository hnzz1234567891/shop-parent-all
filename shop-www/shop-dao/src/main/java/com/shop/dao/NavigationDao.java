package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.Navigation;

public interface NavigationDao {
	
	@Select("select id, name, url, is_blank_target from xx_navigation where position = #{position} order by orders")
	List<Navigation> find(@Param(value="position") Integer position);


}
