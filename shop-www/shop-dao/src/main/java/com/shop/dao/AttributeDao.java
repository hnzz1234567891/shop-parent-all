package com.shop.dao;

import com.shop.core.model.Attribute;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by TW on 2017/6/13.
 */
public interface AttributeDao {

    @Select("SELECT t.id, t.`name`, t.`options`, t.property_index, t.product_category " +
            " FROM xx_attribute t where product_category = #{productCategoryId} ORDER BY orders")
    List<Attribute> findByCategoryId(@Param(value = "productCategoryId") Integer productCategoryId);
}
