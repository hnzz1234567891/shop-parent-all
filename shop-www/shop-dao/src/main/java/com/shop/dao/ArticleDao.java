package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.Article;

public interface ArticleDao {
	
	@Select("select id, title from xx_article where article_category = #{categoryId} "
			+ "and is_publication = 1 limit #{count}")
	List<Article> findByCategoryId(@Param(value="categoryId")Integer categoryId,
			@Param(value="count")Integer count);

}
