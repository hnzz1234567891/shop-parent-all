package com.shop.controller;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shop.core.annotation.IsLogin;
import com.shop.core.base.*;
import com.shop.core.constant.Constant;
import com.shop.core.constant.GoodsOrders;
import com.shop.core.dto.GoodsDto;
import com.shop.core.model.CartItem;
import com.shop.core.model.ProductCategory;
import com.shop.core.util.LoginIdentityUtil;
import com.shop.core.vo.GoodsVo;
import com.shop.core.vo.LoginIndentity;
import com.shop.service.CartService;
import com.shop.service.GoodsService;
import com.shop.service.ProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by TW on 2017/6/10.
 */
@Controller
@RequestMapping("cart")
public class CartController extends BaseController {

    @Autowired
    private CartService cartService;

    @RequestMapping("add")
    @ResponseBody
    @IsLogin
    public ResultInfo add(Integer productId, Integer quantity,
                          HttpServletRequest request, Integer goodsId) {
        cartService.add(productId, quantity, LoginIdentityUtil.getLoginUserId(request), goodsId);

        return success("添加成功");
    }

    @IsLogin
    @RequestMapping("count")
    @ResponseBody
    public ResultInfo countQuantity(HttpServletRequest request) {
        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);
        Integer count = cartService.countQuantity(loginUserId);
        return success(count);
    }


    @IsLogin
    @RequestMapping("list")
    public String list(BaseDto baseDto, HttpServletRequest request, Model model) {

        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);
        PageList<CartItem> result = cartService.selectForPage(baseDto, loginUserId);
        model.addAttribute("result", result);
        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(result.getPaginator(), pageInfo);
        model.addAttribute("paginator", pageInfo);

        return "cart/list";
    }

    @IsLogin
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateQuantity(Integer id, Integer quantity, HttpServletRequest request) {

        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);

        cartService.updateQuantity(loginUserId, id, quantity);

        return success("更新成功");
    }

    @IsLogin
    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo delete(Integer id, HttpServletRequest request) {
        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);
        cartService.delete(loginUserId, id);
        return success("删除成功");
    }

    @IsLogin
    @RequestMapping("clear")
    @ResponseBody
    public ResultInfo clear(HttpServletRequest request) {
        Integer loginUserId = LoginIdentityUtil.getLoginUserId(request);
        cartService.deleteAll(loginUserId);
        return success("清空成功");
    }

}
