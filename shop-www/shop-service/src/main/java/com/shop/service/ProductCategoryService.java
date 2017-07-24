package com.shop.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.shop.core.constant.ProductCategoryGrade;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.exception.ParamException;
import com.shop.core.model.ProductCategory;
import com.shop.core.vo.ProductCategoryVo;
import com.shop.dao.ProductCategoryDao;

@Service
public class ProductCategoryService {
	
	@Autowired
	private ProductCategoryDao productCategoryDao;

	/**
	 * 查询根据分类
	 * @return
	 */
	public List<ProductCategoryVo> findRootList(Integer count) {
		if (count == null) {
			count = 6;
		}
		List<ProductCategory> productCategories = productCategoryDao.findRootList(count);
		List<ProductCategoryVo> productCategoryVos = build(productCategories);
		return productCategoryVos;
	}

	/**
	 * 根据父级分类获取子级分类
	 * @param parentId
	 * @param count
	 * @return
	 */
	public List<ProductCategoryVo> findChildrenList(Integer parentId, Integer count) {
		if (parentId == null || parentId < 1) {
			throw new ParamException("请输入父级分类");
		}
		List<ProductCategory> productCategories = null;
		if (count == null) { // 如果为空获取所有子分类
			productCategories = productCategoryDao.findAllChildrenList(parentId);
		} else {
			productCategories = productCategoryDao.findChildrenList(parentId, count);
		}
		List<ProductCategoryVo> productCategoryVos = build(productCategories);
		return productCategoryVos;
	}

	/**
	 * 根据主键Id获取分类
	 * @param productCategoryId
	 * @return
	 */
	public ProductCategory findById(Integer productCategoryId) {
		if(productCategoryId == null || productCategoryId < 1) {
			throw new ParamException("请选择分类");
		}
		ProductCategory productCategory = productCategoryDao.findById(productCategoryId);
		if (productCategory == null) {
			throw new ParamException("该分类不存在");
		}
		return  productCategory;

	}


	public List<ProductCategory> findParentList(Integer currentCategoryId) {
		// 获取当前分类
		ProductCategory currentCategory = findById(currentCategoryId);
		// 先判断是否是根级分类
		if (ProductCategoryGrade.ROOT.getGrade() == currentCategory.getGrade()) {
			return Collections.emptyList();
		} else if (ProductCategoryGrade.FIRST.getGrade() == currentCategory.getGrade()) { // ,1,
			String treePath = currentCategory.getTreePath();
			treePath = treePath.substring(1, treePath.lastIndexOf(","));
			Integer parentId = Integer.parseInt(treePath);
			// 获取父级分类
			ProductCategory parentCategory = findById(parentId);
			List<ProductCategory> productCategories = new ArrayList<>();
			productCategories.add(parentCategory);
			return productCategories;

		} else { // ,1,7,
			String treePath = currentCategory.getTreePath();
			treePath = treePath.substring(1, treePath.lastIndexOf(","));
			List<ProductCategory> productCategories = productCategoryDao.findByIds(treePath);
			return productCategories;
		}

	}
	
	
	private List<ProductCategoryVo> build(List<ProductCategory> productCategories) {
		List<ProductCategoryVo> productCategoryVos = new ArrayList<>();
		for (ProductCategory productCategory : productCategories) {
			ProductCategoryVo productCategoryVo = new ProductCategoryVo();
//			productCategoryVo.setId(id);
//			productCategoryVo.setName(name);
			BeanUtils.copyProperties(productCategory, productCategoryVo); // 对象之间的copy
			productCategoryVos.add(productCategoryVo);
		}
		return productCategoryVos;
	}
}
