package com.shop.directive;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public abstract class BaseDirective implements TemplateDirectiveModel {
	
	private static Logger logger = LoggerFactory.getLogger(BaseDirective.class);
	
	/**
	 * 获取参数
	 * @param params
	 * @param parameterName
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> T getParameter(Map params, String parameterName, Class<?> clazz)  {
		// 获取参数
		TemplateModel templateModel = (TemplateModel) params.get(parameterName);
		// 转换器：wrap():将java类型转化为freemarker data model, unwrap()：freemarker data model转化为java类型
		BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_21).build();
		T result = null;
		try {
			result = (T) beansWrapper.unwrap(templateModel, clazz);
		} catch (TemplateModelException e) {
			logger.error("获取参数异常：{}", e);
			e.printStackTrace();
		}
		
		return result;
	}
	
	/**
	 * 数据输出
	 * @param env
	 * @param body
	 * @param keyName
	 * @param data
	 */
	protected void setVariable(Environment env, TemplateDirectiveBody body, String keyName, Object data) {
		
		try {
			if (body != null) {
				BeansWrapper beansWrapper = new BeansWrapperBuilder(Configuration.VERSION_2_3_21).build();
				TemplateModel result = beansWrapper.wrap(data); // 将Java对象转化为freemarker的模板模型
				env.setVariable(keyName, result); // 设置输出的key
				// 输出
				body.render(env.getOut());  
			} else {
				env.getOut().write(JSON.toJSONString(data)); // 输出json
			}
		} catch (IOException | TemplateException e) {
			logger.error("获取参数异常：{}", e);
			e.printStackTrace();
		}
		
	}
	
}
