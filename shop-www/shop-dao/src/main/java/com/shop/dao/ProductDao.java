package com.shop.dao;

import com.shop.core.model.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by TW on 2017/6/14.
 */
public interface ProductDao {

    Product findDefaultProduct(@Param(value = "goodsId") Integer goodsId);

    List<Product> findGoodsProducts(@Param(value = "goodsId")Integer goodsId);

    @Select("select id, allocated_stock,cost,exchange_point,market_price,"
            + "price,reward_point,sn, specification_values, stock from "
            + "xx_product where id = #{productId}")
    Product findById(@Param(value = "productId") Integer productId);

    @Update("update xx_product set allocated_stock = #{allocatedStock} where id = #{productId} and stock >= #{allocatedStock}")
    int updateAllocatedStock(@Param(value = "productId")Integer id, @Param(value = "allocatedStock")int allocatedStock);
}
