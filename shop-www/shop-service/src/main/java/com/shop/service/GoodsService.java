package com.shop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shop.core.constant.GoodsOrders;
import com.shop.core.constant.ProductCategoryGrade;
import com.shop.core.dto.GoodsDto;
import com.shop.core.model.*;
import com.shop.core.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.constant.Constant;
import com.shop.core.exception.ParamException;
import com.shop.dao.GoodsDao;

@Service
public class GoodsService {
	
	@Autowired
	private GoodsDao goodsDao;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private ProductService productService;

	public List<Goods> findHotGoods(Integer productCategoryId, 
			Integer tagId, Integer count) {
//		if (productCategoryId == null || productCategoryId < 1) {
//			 throw new ParamException("请选择分类");
//		}
		if (tagId == null || tagId < 1) {
			 throw new ParamException("请选择查询的标签");
		}
		if (count == null) {
			count = Constant.TEN;
		}
		List<Goods> goods = null;
		if (productCategoryId == null || productCategoryId < 1) {
			goods = goodsDao.findAllHotGoods(tagId, count);
		} else {
			goods = goodsDao.findHotGoods(productCategoryId, tagId, count);
		}

		return goods;
	}

	/**
	 * 关键字搜索
	 * @param goodsDto
	 * @return
	 */
	public PageList<GoodsVo> search(GoodsDto goodsDto) {
		if (StringUtils.isBlank(goodsDto.getSort())) {
			goodsDto.setSort(GoodsOrders.create_date.getSort());
		}
		PageList<GoodsVo> goodsPageList = goodsDao.selectForPage(goodsDto, goodsDto.buildPageBounds());
		return goodsPageList;
	}

	/**
	 * 分类查询货品
	 * @param goodsDto
	 * @param categoryId
	 * @return
	 */
//    public Map<String, Object> list(GoodsDto goodsDto, Integer categoryId) {
	public Object[] list(GoodsDto goodsDto, Integer categoryId) {
		if (categoryId == null || categoryId < 1) {
			throw new ParamException("请选择分类");
		}
		if (StringUtils.isBlank(goodsDto.getSort())) {
			goodsDto.setSort(GoodsOrders.create_date.getSort());
		}

		// 先分析如果这个分类
		ProductCategory productCategory = productCategoryService.findById(categoryId);
		goodsDto.setProductCategoryId(categoryId);
		PageList<GoodsVo> goodsPageList = null;
		if (productCategory.getGrade() == ProductCategoryGrade.SECOND.getGrade()) { // 可以直接查询出商品
			// select * from xx_goods where product_category = 121;
			goodsPageList = goodsDao.list(goodsDto, goodsDto.buildPageBounds());
		} else {
			// 获取第三级的子类
			// select * from xx_product_category where tree_path like ',1,%' and grade = 2;
			// select * from xx_goods where product_category in ( 121, 122);
			// select t1.* from xx_goods t1 left join xx_product_category t2 on t1.product_category = t2.id
			// where tree_path like ',1,%' and grade = 2
			String treePath = productCategory.getTreePath() + categoryId + ","; // ,1,9,
			goodsDto.setTreePath(treePath);
			goodsPageList = goodsDao.findOtherList(goodsDto, goodsDto.buildPageBounds());
		}
//		Map<String, Object> result = new HashMap();
//		result.put("pageList", goodsPageList);
//		result.put("productCategory", productCategory);
//		return result;
		return  new Object[]{goodsPageList, productCategory};
    }

	/**
	 * 根据ID查询
	 * @param id
	 */
	public Map<String, Object> findById(Integer id) {

		// 基本参数验证
		if(id == null || id < 1)  {
			throw new ParamException("请选择商品");
		}
		// 查询货品信息
		GoodsDetailVo goods = goodsDao.findById(id);
		if (goods == null) {
			throw new ParamException("该商品不存在");
		}
		// 格式化数据（JSON字符串转化成对象）: 图片字符串；规格项；基本参数

		//[{"title":null,"source":"http://image.demo.shopxx.net/4.0/201501/e39f89ce-e08a-4546-8aee-67d4427e86e2-source.jpg","large":"http://image.demo.shopxx.net/4.0/201501/e39f89ce-e08a-4546-8aee-67d4427e86e2-large.jpg","medium":"http://image.demo.shopxx.net/4.0/201501/e39f89ce-e08a-4546-8aee-67d4427e86e2-medium.jpg","thumbnail":"http://image.demo.shopxx.net/4.0/201501/e39f89ce-e08a-4546-8aee-67d4427e86e2-thumbnail.jpg","order":1},{"title":null,"source":"http://image.demo.shopxx.net/4.0/201501/00705b09-2f7c-438b-b0a2-cbe236c1407d-source.jpg","large":"http://image.demo.shopxx.net/4.0/201501/00705b09-2f7c-438b-b0a2-cbe236c1407d-large.jpg","medium":"http://image.demo.shopxx.net/4.0/201501/00705b09-2f7c-438b-b0a2-cbe236c1407d-medium.jpg","thumbnail":"http://image.demo.shopxx.net/4.0/201501/00705b09-2f7c-438b-b0a2-cbe236c1407d-thumbnail.jpg","order":2}]
		List<ProductImage> productImages = JSON.parseArray(goods.getProductImages(), ProductImage.class);
		List<SpecificationItem> specificationItems = JSON.parseArray(goods.getSpecificationItems(), SpecificationItem.class);

		// [{"group":"基本参数","entries":[{"name":"操作系统","value":"iOS"}],
		// ]
		List<ParameterValue> parameterValues = JSON.parseArray(goods.getParameterValues(), ParameterValue.class);

		// 查询默认的商品
		Product defaultProduct = productService.findDefaultProduct(id);

		// 查询货品下的所有商品
		List<Product> products = productService.findGoodsProducts(id);

		// 货品分类
		ProductCategory productCategory = productCategoryService.findById(goods.getProductCategory());

		// 构建一个返回结果

		Map<String, Object> result = new HashMap<>();
		result.put("good", goods);
		result.put("specificationValues", specificationItems);
		result.put("parameterValues", parameterValues);
		result.put("productImages", productImages);
		result.put("defaultProduct", defaultProduct);
		result.put("productCategory", productCategory);
		result.put("products", products);
		return result;
	}
}
