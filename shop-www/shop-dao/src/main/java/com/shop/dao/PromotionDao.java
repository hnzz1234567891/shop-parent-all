package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.Promotion;

public interface PromotionDao {
	
	@Select("SELECT id, image, title FROM xx_promotion t1 LEFT JOIN xx_product_category_promotion t2 "
			+ " on t1.id=t2.promotions WHERE t2.product_categories=#{productCategoryId} order by orders limit #{count}")
	List<Promotion> findList(@Param(value="productCategoryId")Integer productCategoryId, 
			@Param(value="count")Integer count);

	@Select("SELECT id, image, title FROM xx_promotion t1 order by orders limit #{count}")
    List<Promotion> findAllList(Integer count);
}
