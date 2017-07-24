package com.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.core.constant.Constant;
import com.shop.core.exception.ParamException;
import com.shop.core.model.Article;
import com.shop.dao.ArticleDao;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleDao articleDao;
	
	/**
	 * 根据分类获取文章
	 * @param categoryId
	 * @return
	 */
	public List<Article> findByCategoryId(Integer categoryId, Integer count) {
		if(categoryId == null || categoryId < 1) {
			throw new ParamException("请选择分类");
		}
		if(count == null || categoryId < 1) {
			count = Constant.SIX;
		}
		List<Article> articles = articleDao.findByCategoryId(categoryId, count);
		
		return articles;
	}
}
