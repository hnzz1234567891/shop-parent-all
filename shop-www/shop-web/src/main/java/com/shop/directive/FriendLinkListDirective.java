package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.model.FriendLink;
import com.shop.service.FriendLinkService;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class FriendLinkListDirective extends BaseDirective {
	
	@Autowired
	private FriendLinkService friendLinkService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		
		Integer count = getParameter(params, "count", Integer.class);
		// 获取数据
		List<FriendLink> friendLinks = friendLinkService.findList(count);
		setVariable(env, body, "friendLinks", friendLinks);
	}
	
}
