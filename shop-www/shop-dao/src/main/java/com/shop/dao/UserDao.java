package com.shop.dao;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.User;
import org.springframework.stereotype.Repository;


public interface UserDao {
	
//	@Insert("insert into xx_user (uname, pwd, nation, location, gender) values(#{uname}, #{pwd}, #{nation}, #{location}, #{gender})")
//	@Options(keyProperty="userId", useGeneratedKeys=true)
	int insert(User user);
	
//	@Select("select user_id, uname, pwd, nation, location, gender from xx_user where uname like '%${uname}%'")
	PageList<User> find(@Param(value="uname") String uname, PageBounds pageBounds);
	
}
