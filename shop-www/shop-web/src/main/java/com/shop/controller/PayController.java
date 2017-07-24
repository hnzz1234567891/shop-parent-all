package com.shop.controller;

import com.shop.core.annotation.IsLogin;
import com.shop.core.base.BaseController;
import com.shop.core.base.ResultInfo;
import com.shop.core.dto.PayCallbackDto;
import com.shop.core.model.Area;
import com.shop.core.util.LoginIdentityUtil;
import com.shop.core.vo.PayRequestVo;
import com.shop.service.AreaService;
import com.shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * Created by TW on 2017/6/10.
 */
@Controller
@RequestMapping("pay")
public class PayController extends BaseController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("page/{orderNo}")
    @IsLogin
    public String index(@PathVariable String orderNo, Model model, HttpServletRequest request) {
        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);
        PayRequestVo payRequestVo = orderService.findByOrderNo(orderNo, loginUserId);
        model.addAttribute("payRequestVo", payRequestVo);
        return "pay/pay";
    }
//    total_fee
//            out_order_no
//    sign
//            trade_no
//    trade_status

    @RequestMapping("return_url")
    @IsLogin
    public String returnUrl(PayCallbackDto payCallbackDto, HttpServletRequest request, Model model) {
        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);
        Map<String, Object> result = orderService.handlerCallback(payCallbackDto, loginUserId);
        model.addAllAttributes(result);
        return "pay/pay_success";
    }

}
