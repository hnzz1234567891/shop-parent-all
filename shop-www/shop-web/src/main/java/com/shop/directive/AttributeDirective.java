package com.shop.directive;

import com.shop.core.model.AdPosition;
import com.shop.core.model.Attribute;
import com.shop.service.AdPositionService;
import com.shop.service.AttributeService;
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
public class AttributeDirective extends BaseDirective {
	
	@Autowired
	private AttributeService attributeService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		
		// 获取页面传入的参数
		Integer productCategoryId = getParameter(params, "productCategoryId", Integer.class);
		
		// 获取数据
		List<Attribute> attributes = attributeService.findByCategoryId(productCategoryId);
		setVariable(env, body, "attributes", attributes);
	}
	
}
