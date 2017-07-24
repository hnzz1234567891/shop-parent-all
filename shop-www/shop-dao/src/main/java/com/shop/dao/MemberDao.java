package com.shop.dao;

import com.shop.core.model.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by TW on 2017/6/14.
 */
public interface MemberDao {

    @Select("select id, username, password, phone, email from xx_member where ${columnName} = #{columnValue}")
    Member findByColumn(@Param(value = "columnName") String columnName, @Param(value = "columnValue")String columnValue);

    @Insert("INSERT INTO xx_member (username, password, email, phone, gender, name, mobile, create_date, modify_date, version, "
            + " amount,balance, is_enabled, is_locked, lock_key, login_failure_count, point, register_ip, member_rank) "
            + " VALUES (#{username}, #{password}, #{email}, #{phone}, #{gender}, #{name}, #{mobile}, now(), now(), 0, 0, 0, 1, 0, '', 0, 0, '',1)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Member member);

    @Select("select id, username, password, phone, email from xx_member where username = #{userName} or email = #{userName}")
    Member findByUserNameOrEmail(@Param(value = "userName") String userName);
}
