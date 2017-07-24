package com.shop.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.shop.core.model.ArticleCategory;

public interface ArticleCategoryDao {
	
	@Select("select id, name from xx_article_category where grade=0 order by orders limit #{count}")
	List<ArticleCategory> findRootList(@Param(value="count") Integer count);

}
