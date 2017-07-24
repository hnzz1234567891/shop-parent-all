package com.shop.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shop.core.model.CartItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by TW on 2017/6/17.
 */
public interface CartItemDao {

    @Insert("insert into xx_cart_item (create_date, modify_date, version, quantity, cart, product"
            + " ) values (#{createDate}, #{modifyDate}, 0, #{quantity}, #{cart}, #{product})")
    void insert(CartItem cartItem);

    @Select("select id, quantity, cart, product from xx_cart_item "
            + "where product = #{productId} and cart = #{cartId} and version = 0")
    CartItem findCartProduct(@Param(value = "productId") Integer productId, @Param(value = "cartId") Integer cartId);

    @Update("update xx_cart_item set quantity = quantity + #{quantity}, modify_date = now() where id = #{id}")
    void updateQuantity(@Param(value = "id")Integer id, @Param(value = "quantity")Integer quantity);

    @Update("update xx_cart_item set quantity = #{quantity}, modify_date = now() where id = #{id}")
    void editQuantity(@Param(value = "id")Integer id, @Param(value = "quantity")Integer quantity);

    @Select("SELECT sum(quantity) FROM xx_cart_item i LEFT JOIN xx_cart c " +
            " on i.cart=c.id where member = #{memberId} and i.version=0;")
    Integer count(@Param(value = "memberId") Integer memberId);

    // 分页查询购物车的商品信息
    PageList<CartItem> selectForPage(PageBounds pageBounds, @Param(value = "cartId") Integer cartId);

    @Select("select id, quantity, cart, product from xx_cart_item "
            + "where id = #{id} and cart = #{cartId} and version = 0")
    CartItem findById(@Param(value = "id") Integer id, @Param(value = "cartId")Integer cartId);

    @Update("update xx_cart_item set version = -1 where id = #{id} ")
    void delete(@Param(value = "id") Integer id);

    @Update("update xx_cart_item set version = -1 where cart = #{cartId} and version = 0 ")
    void deleteCartProduct(@Param(value = "cartId") Integer cartId);

    List<CartItem> findByIds(@Param(value = "ids")String ids, @Param(value = "cartId")Integer cartId);

    @Update("update xx_cart_item set version = -2 where id in (${ids}) and version = 0 ")
    void deleteByIds(@Param(value = "ids")String cartItemIds);
}
