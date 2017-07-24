package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.ProductCategory;

public interface ProductCategoryDao {
	
	@Select("select id, name from xx_product_category where grade = 0 order by orders limit #{count}")
	List<ProductCategory> findRootList(@Param(value = "count") Integer count);

	@Select("select id, name from xx_product_category where parent = #{parentId} order by orders limit #{count}")
	List<ProductCategory> findChildrenList(@Param(value = "parentId") Integer parentId, @Param(value = "count") Integer count);
	
	@Select("select id, name from xx_product_category where parent = #{parentId} order by orders")
	List<ProductCategory> findAllChildrenList(@Param(value = "parentId") Integer parentId);

	@Select("select id, name, tree_path, grade from xx_product_category where id = #{id}")
    ProductCategory findById(@Param(value = "id") Integer productCategoryId);

	@Select("select id, name, tree_path, grade from xx_product_category where id in (${ids})")
    List<ProductCategory> findByIds(@Param(value = "ids") String ids);
}