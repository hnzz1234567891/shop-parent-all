package com.shop.controller;

import com.shop.core.base.ResultInfo;
import com.shop.core.constant.Constant;
import com.shop.core.exception.ParamException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.RequestMapping;

import com.shop.core.base.BaseController;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController extends BaseController {
	
	@RequestMapping("index")
	public String index() {
		return "index";
	}

	@RequestMapping("register")
	public String register(String redirectUrl, Model model) {

		model.addAttribute("redirectUrl", redirectUrl);
		return "user/register";
	}

	@RequestMapping("logout")
	@ResponseBody
	public ResultInfo logout(HttpServletRequest request) {
		request.getSession().removeAttribute(Constant.LOGIN_USER_KEY);
		return success("退出成功");
	}
	@RequestMapping("login")
	public String login(String redirectUrl, Model model) {
		model.addAttribute("redirectUrl", redirectUrl);
		return "user/login";
	}

	@RequestMapping("test1")
	public String test1(Integer id) {

		return "test";
	}

	@RequestMapping("test2")
	public void test2() {
		throw new IllegalArgumentException("IllegalArgumentException");
	}
}
