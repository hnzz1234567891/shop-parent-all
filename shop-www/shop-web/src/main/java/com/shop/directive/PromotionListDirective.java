package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.model.Promotion;
import com.shop.service.PromotionService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class PromotionListDirective extends BaseDirective {
	
	@Autowired
	private PromotionService promotionService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		// 获取参数
		Integer count = getParameter(params, "count", Integer.class);
		Integer productCategoryId = getParameter(params, "productCategoryId", Integer.class);
		// 获取数据
		List<Promotion> promotions = promotionService.findList(productCategoryId, count);
		// 输出
		setVariable(env, body, "promotions", promotions);
	}
	
}
