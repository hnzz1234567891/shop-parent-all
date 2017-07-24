package com.shop.cache.controller;

import com.shop.core.annotation.IsLogin;
import com.shop.core.util.LoginIdentityUtil;
import com.shop.core.vo.LoginIndentity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TW on 2017/6/22.
 */
@RestController
@RequestMapping("user")
public class UserController {

    @GetMapping("find_from_session")
    public LoginIndentity find(HttpServletRequest request) {
        return LoginIdentityUtil.getIdentityFromSession(request);
    }
}
