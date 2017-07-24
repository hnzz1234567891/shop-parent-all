package com.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.model.ArticleCategory;
import com.shop.dao.ArticleCategoryDao;

@Service
public class ArticleCategoryService {
	
	@Autowired
	private ArticleCategoryDao articleCategoryDao;

	public List<ArticleCategory> findRootList(Integer count) {
		if (count == null) {
			count = 2;
		}
		List<ArticleCategory> articleCategories = articleCategoryDao.findRootList(count);
		return articleCategories;
	}
	
}
