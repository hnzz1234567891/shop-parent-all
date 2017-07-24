package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.FriendLink;

public interface FriendLinkDao {
	
	@Select("select id, name, logo, url from xx_friend_link order by orders limit #{count}")
	List<FriendLink> findAll(@Param(value="count")Integer count);

}
