package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.Brand;

public interface BrandDao {
	
	@Select("SELECT id, name, logo FROM xx_brand t1 LEFT JOIN xx_product_category_brand "
			+ "t2 on t1.id=t2.brands WHERE t2.product_categories=#{productCategoryId} order by orders limit #{count}")
	List<Brand> findList(@Param(value="productCategoryId")Integer productCategoryId, 
			@Param(value="count")Integer count);
	
	@Select("SELECT id, name, logo FROM xx_brand t1 order by orders limit #{count}")
	List<Brand> findAllList(@Param(value="count")Integer count);

}
