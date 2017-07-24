package com.shop.dao;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shop.core.dto.GoodsDto;
import com.shop.core.vo.GoodsDetailVo;
import com.shop.core.vo.GoodsVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.Goods;

public interface GoodsDao {
	
	@Select("SELECT t1.id, t1.`name`, t1.caption, t1.price, t1.market_price, t1.image " + 
			" FROM xx_goods t1 LEFT JOIN xx_product_category t2 ON t1.product_category = t2.id " + 
			" LEFT JOIN xx_goods_tag t3 on t1.id=t3.goods " + 
			" where t1.is_marketable=1 AND t2.tree_path LIKE ',${productCategoryId},%' AND t3.tags=#{tagId} " + 
			" LIMIT #{count}")
	List<Goods> findHotGoods(@Param(value="productCategoryId")Integer productCategoryId, 
			@Param(value="tagId")Integer tagId, @Param(value="count")Integer count);


	@Select("SELECT t1.id, t1.`name`, t1.caption, t1.price, t1.market_price, t1.image from xx_goods t1" +
			" LEFT JOIN xx_goods_tag t3 on t1.id=t3.goods " +
			" where t1.is_marketable=1 AND t3.tags=#{tagId} " +
			" LIMIT #{count}")
	List<Goods> findAllHotGoods(@Param(value="tagId")Integer tagId, @Param(value="count")Integer count);

//	@SelectProvider(type=GoodsProvider.class, method = "selectForPage")
	PageList<GoodsVo> selectForPage(GoodsDto goodsDto, PageBounds pageBounds);

    PageList<GoodsVo> list(GoodsDto goodsDto, PageBounds pageBounds);

	PageList<GoodsVo> findOtherList(GoodsDto goodsDto, PageBounds pageBounds);

	@Select("SELECT t.id, t.`name`, t.market_price, t.price," +
			"t.caption, t.parameter_values, t.product_images," +
			"t.product_category, t.specification_items, t.sn," +
			"t.introduction, t.image, t.type, t.unit FROM xx_goods t where id = #{id}")
	@ResultType(GoodsDetailVo.class)
	GoodsDetailVo findById(@Param(value = "id") Integer id);
}
