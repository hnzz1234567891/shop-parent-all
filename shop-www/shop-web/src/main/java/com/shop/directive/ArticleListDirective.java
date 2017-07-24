package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.model.Article;
import com.shop.service.ArticleService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class ArticleListDirective extends BaseDirective {
	
	
	@Autowired
	private ArticleService articleService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		
		// 获取页面传入的参数
		Integer categoryId = getParameter(params, "categoryId", Integer.class);
		Integer count = getParameter(params, "count", Integer.class);
		List<Article> articles = articleService.findByCategoryId(categoryId, count);
		
		setVariable(env, body, "articles", articles);
		
	}
	
}
