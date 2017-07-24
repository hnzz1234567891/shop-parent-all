package com.shop.controller;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shop.core.annotation.IsLogin;
import com.shop.core.base.BaseController;
import com.shop.core.base.BaseDto;
import com.shop.core.base.PageInfo;
import com.shop.core.base.ResultInfo;
import com.shop.core.model.CartItem;
import com.shop.core.model.Receiver;
import com.shop.core.util.LoginIdentityUtil;
import com.shop.service.CartService;
import com.shop.service.OrderService;
import com.shop.service.ReceiverService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by TW on 2017/6/10.
 */
@Controller
@RequestMapping("order")
public class OrderController extends BaseController {

    @Autowired
    private CartService cartService;
    @Autowired
    private ReceiverService receiverService;
    @Autowired
    private OrderService orderService;

    @RequestMapping("checkout")
    @IsLogin
    public String checkout(String cartItemIds, HttpServletRequest request, Model model) {
        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);

        // 获取用户的地址信息
        List<Receiver> receivers = receiverService.findUserReceiver(loginUserId);
        model.addAttribute("receivers", receivers);
        // 获取购买的商品信息
        List<CartItem> cartItems = cartService.findByIds(cartItemIds, loginUserId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("cartItemIds", cartItemIds);
        return "order/checkout";
    }

    @RequestMapping("create")
    @IsLogin
    @ResponseBody
    public ResultInfo add(HttpServletRequest request,
                               String cartItemIds, Integer receiverId, String memo)
            throws Exception {
        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);
        String orderNo = orderService.add(loginUserId, cartItemIds, receiverId, memo);
        return success(orderNo);
    }
}
