package com.shop.service;

import java.util.List;

import com.shop.core.constant.ProductCategoryGrade;
import com.shop.core.model.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.model.Brand;
import com.shop.dao.BrandDao;

@Service
public class BrandService {
	
	@Autowired
	private BrandDao brandDao;
	@Autowired
	private ProductCategoryService productCategoryService;

	public List<Brand> findList(Integer productCategoryId, Integer count) {
		// 基本参数验证
//		if (productCategoryId == null || productCategoryId < 1) {
//			throw new ParamException("请选择分类");
//		}
		if (count == null) {
			count = 4;
		}
		List<Brand> brands = null;
		if (productCategoryId == null || productCategoryId < 1) {
			brands = brandDao.findAllList(count);
		} else {

			// 先判断是否是根级分类
			ProductCategory productCategory = productCategoryService.findById(productCategoryId);
			if (productCategory.getGrade() != ProductCategoryGrade.ROOT.getGrade() ) { // 如果不是根级分类必须拿到根级分类ID
				String treePath = productCategory.getTreePath(); // ,1,2,3
				String[] treePathArr = treePath.split(",");
				Integer rootCategoryId = Integer.parseInt(treePathArr[1]);
				productCategoryId = rootCategoryId;
			}
			brands = brandDao.findList(productCategoryId, count);
		}
		return brands;
	}
	
}
