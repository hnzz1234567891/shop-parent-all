package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.model.Brand;
import com.shop.service.BrandService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class BrandListDirective extends BaseDirective {
	
	@Autowired
	private BrandService brandService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		// 获取参数
		Integer count = getParameter(params, "count", Integer.class);
		Integer productCategoryId = getParameter(params, "productCategoryId", Integer.class);
		// 获取数据
		List<Brand> brands = brandService.findList(productCategoryId, count);
		// 输出
		setVariable(env, body, "brands", brands);
	}
	
}
