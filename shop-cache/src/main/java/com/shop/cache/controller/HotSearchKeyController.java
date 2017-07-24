package com.shop.cache.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shop.cache.service.HotSearchKeyService;
import com.shop.core.base.BaseController;
import com.shop.core.base.ResultInfo;
import com.shop.core.exception.ParamException;

@RestController
@RequestMapping("hot_search")
public class HotSearchKeyController extends BaseController {
	
	@Autowired
	private HotSearchKeyService hotSearchKeyService;
	
	@RequestMapping("add")
	public ResultInfo add(String keywords) {
		
		try {
			Long resultCount = hotSearchKeyService.add(keywords);
			return success(resultCount);
		} catch (ParamException e) {
			int code = e.getResultCode();
			String message = e.getMessage();
			ResultInfo result = failure(code, message);
			return result;
		}
	}
	
	@RequestMapping("get")
	public ResultInfo get() {
		List<String> result = hotSearchKeyService.get();
		return success(result);
	}
}
