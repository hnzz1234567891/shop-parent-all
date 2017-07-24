package com.shop.core.util;

import com.shop.core.constant.Constant;
import com.shop.core.vo.LoginIndentity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by TW on 2017/6/18.
 */
public class LoginIdentityUtil {

    public static LoginIndentity getIdentityFromSession(HttpServletRequest request) {
        LoginIndentity loginIndentity = (LoginIndentity)request.getSession()
                .getAttribute(Constant.LOGIN_USER_KEY);
        return  loginIndentity;
    }

    public static Integer getLoginUserId(HttpServletRequest request) {
        LoginIndentity loginIndentity = (LoginIndentity)request.getSession()
                .getAttribute(Constant.LOGIN_USER_KEY);
        if (loginIndentity != null) {
            return loginIndentity.getId();
        } else {
            return null;
        }
    }

}
