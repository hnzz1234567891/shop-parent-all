package com.shop.directive;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.shop.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shop.core.model.Goods;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class GoodsListDirective extends BaseDirective {
	
	@Autowired
	private GoodsService goodsService;

	@SuppressWarnings("rawtypes")
	@Override
	public void execute(Environment env, Map params, TemplateModel[] loopVars, 
			TemplateDirectiveBody body)
	throws TemplateException, IOException {
		
		// 获取页面传入的参数
		Integer productCategoryId = getParameter(params, "productCategoryId", Integer.class);
		Integer tagId = getParameter(params, "tagId", Integer.class);
		Integer count = getParameter(params, "count", Integer.class);
		
		// 获取数据
		List<Goods> goods = goodsService.findHotGoods(productCategoryId, tagId, count);
		// TODO 加个VO
		setVariable(env, body, "goods", goods);
	}
	
}
