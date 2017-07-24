package com.shop.directive;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.model.AdPosition;
import com.shop.service.AdPositionService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class AdDirective extends BaseDirective {
	
	@Autowired
	private AdPositionService adPositionService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		
		// 获取页面传入的参数
		Integer positionId = getParameter(params, "positionId", Integer.class);
		
		// 获取数据
		AdPosition adPosition = adPositionService.findById(positionId);
		// TODO 从xx_ad表中获取广告内容
		
		setVariable(env, body, "ads", adPosition.getAds());
	}
	
}
