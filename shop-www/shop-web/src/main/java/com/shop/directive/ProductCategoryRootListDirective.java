package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.vo.ProductCategoryVo;
import com.shop.service.ProductCategoryService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class ProductCategoryRootListDirective extends BaseDirective {
	
	@Autowired
	private ProductCategoryService productCategoryService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		// 获取参数
		Integer count = getParameter(params, "count", Integer.class);
		// 获取数据
		List<ProductCategoryVo> productCategories = productCategoryService.findRootList(count);
		// 输出
		setVariable(env, body, "productCategories", productCategories);
	}
	
	
}
