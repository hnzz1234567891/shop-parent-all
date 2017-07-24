package com.shop.controller;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.code.kaptcha.Constants;
import com.shop.core.base.BaseController;
import com.shop.core.base.BaseDto;
import com.shop.core.base.ResultInfo;
import com.shop.core.base.ResultListInfo;
import com.shop.core.constant.Constant;
import com.shop.core.dto.MemberDto;
import com.shop.core.model.User;
import com.shop.core.vo.LoginIndentity;
import com.shop.service.MemberService;
import com.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private MemberService memberService;

	@PostMapping("register")
	public ResultInfo register(MemberDto memberDto, HttpServletRequest request) {
		// 获取图片验证码内容 短信验证码内容
		HttpSession session = request.getSession();
		String sessionVerifyCode = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY); // 从session中获取图片验证码文字
		String sessionPhoneVerifyCode = (String)session.getAttribute(Constant.VERIFY_CODE_KEY); // 从session中获取短信验证码的内容
		LoginIndentity loginIndentity = memberService.insert(memberDto, sessionVerifyCode, sessionPhoneVerifyCode);
		session.setAttribute(Constant.LOGIN_USER_KEY, loginIndentity);
		return success("恭喜你注册成功");
	}

	@RequestMapping("login")
	public ResultInfo login(String userName, String password, String verifyCode, HttpServletRequest request) {
		// 获取图片验证码内容 短信验证码内容
		HttpSession session = request.getSession();
		String sessionVerifyCode = (String)session.getAttribute(Constants.KAPTCHA_SESSION_KEY); // 从session中获取图片验证码
		LoginIndentity loginIndentity = memberService.login(userName, password, verifyCode, sessionVerifyCode);
		session.setAttribute(Constant.LOGIN_USER_KEY, loginIndentity);
		return success("恭喜登录成功");
	}

	@GetMapping("add")
	public Map<String, Object> add(User user) {
		Integer userId = userService.add(user);
		Map<String, Object> result = new HashMap<>();
		result.put("resultCode", 200);
		result.put("resultMessage", "Success");
		result.put("result", userId);
		return result;
	}
	
	@RequestMapping("find/{uname}")
	public ResultListInfo find(@PathVariable String uname, BaseDto baseDto) {
		PageList<User> users = userService.find(uname, baseDto);

		return success(users);
	}
	
}
