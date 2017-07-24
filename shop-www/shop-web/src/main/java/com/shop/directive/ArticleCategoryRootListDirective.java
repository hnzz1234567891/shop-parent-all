package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.model.ArticleCategory;
import com.shop.service.ArticleCategoryService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class ArticleCategoryRootListDirective extends BaseDirective {
	
	@Autowired
	private ArticleCategoryService articleCategoryService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		Integer count = getParameter(params, "count", Integer.class);
		List<ArticleCategory> articleCategories = articleCategoryService.findRootList(count);
		setVariable(env, body, "articleCategories", articleCategories);
	}
}
