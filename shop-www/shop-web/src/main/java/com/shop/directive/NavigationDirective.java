package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.model.Navigation;
import com.shop.service.NavigationService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class NavigationDirective extends BaseDirective {
	
	@Autowired
	private NavigationService navigationService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		
		// 获取参数
		Integer position = getParameter(params, "position", Integer.class);
		// 调用service方法获取数据
		List<Navigation> navigations = navigationService.find(position);
		// 输出
		setVariable(env, body, "navigations", navigations);
	}
	
}
