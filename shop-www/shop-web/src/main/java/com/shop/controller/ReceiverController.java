package com.shop.controller;

import com.shop.core.annotation.IsLogin;
import com.shop.core.base.BaseController;
import com.shop.core.base.ResultInfo;
import com.shop.core.model.Area;
import com.shop.core.model.Receiver;
import com.shop.core.util.LoginIdentityUtil;
import com.shop.service.AreaService;
import com.shop.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by TW on 2017/6/10.
 */
@Controller
@RequestMapping("receiver")
public class ReceiverController extends BaseController {

    @Autowired
    private ReceiverService receiverService;

    @RequestMapping("add")
    @ResponseBody
    @IsLogin
    public ResultInfo add(String consignee, Integer area, String address,
                          String zipCode, String phone, Boolean isDefault,
                          HttpServletRequest request) {
        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);
        Receiver receiver = receiverService.add(consignee, area, address,
                zipCode, phone, isDefault, loginUserId);
        return success(receiver);
    }

}
