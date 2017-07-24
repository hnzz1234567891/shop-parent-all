package com.shop.directive;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.shop.core.model.AdPosition;
import com.shop.service.AdPositionService;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class AdPositionDirective extends BaseDirective {
	
	@Autowired
	private AdPositionService adPositionService;
	
	@Autowired
	private FreeMarkerConfigurer freemarkerConfig;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		
		// 获取页面传入的参数
		Integer positionId = getParameter(params, "positionId", Integer.class);
		
		// 获取数据
		AdPosition adPosition = adPositionService.findById(positionId);
		
		// 输出数据
		String templateContent = adPosition.getTemplate();
		StringReader reader = new StringReader(templateContent);
		Configuration configuration = freemarkerConfig.getConfiguration();
		
		Template template = new Template("adTemplate", reader, configuration);
		Map<String, AdPosition> dataModel = new HashMap<>();
		dataModel.put("adPosition", adPosition);
		template.process(dataModel, env.getOut());
		
	}
	
}
