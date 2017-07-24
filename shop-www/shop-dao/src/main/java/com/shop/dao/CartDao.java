package com.shop.dao;

import com.shop.core.model.Cart;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by TW on 2017/6/17.
 */
public interface CartDao {
    @Select("select id, expire, cart_key, member from xx_cart where member = #{memberId} and version = 0")
    Cart findMemberCart(@Param(value = "memberId") Integer memberId);

    @Insert("INSERT INTO xx_cart (create_date, modify_date, version, expire, cart_key, member) "
            + "VALUES(#{createDate},#{modifyDate},#{version},#{expire},#{cartKey},#{member})")
    @Options(useGeneratedKeys=true, keyProperty = "id")
    void insert(Cart cart);
}
