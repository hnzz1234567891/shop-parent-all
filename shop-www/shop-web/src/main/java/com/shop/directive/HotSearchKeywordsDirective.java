package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.shop.constant.UrlConstant;
import com.shop.core.base.ResultInfo;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class HotSearchKeywordsDirective extends BaseDirective {
	
	@Value("${cache.service.domain}")
	private String cacheServiceDomain;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body)
			throws TemplateException, IOException {
		
		// 获取数据 --》通过RestTemplate进行api接口调用
		RestTemplate restTemplate = new RestTemplate();
		ResultInfo resultInfo = restTemplate.getForObject(cacheServiceDomain + UrlConstant.HOT_SEARCH_KEYWORDS_URL, ResultInfo.class);
		List<String> keywords = (List<String>) resultInfo.getResult();
		
		// 数据输出
		BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_21).build();
		TemplateModel result = beansWrapper.wrap(keywords); // 将Java对象转化为freemarker的模板模型
		setVariable(env, body, "keywords", result);
	}

}
