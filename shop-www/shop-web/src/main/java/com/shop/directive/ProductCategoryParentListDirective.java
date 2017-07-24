package com.shop.directive;

import com.shop.core.model.AdPosition;
import com.shop.core.model.ProductCategory;
import com.shop.service.AdPositionService;
import com.shop.service.ProductCategoryService;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class ProductCategoryParentListDirective extends BaseDirective {
	
	@Autowired
	private ProductCategoryService productCategoryService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		
		// 获取页面传入的参数
		Integer currentCategoryId = getParameter(params, "productCategoryId", Integer.class);
		
		// 获取数据
		List<ProductCategory> productCategories = productCategoryService.findParentList(currentCategoryId);

		setVariable(env, body, "parentCategories", productCategories);
	}
	
}
