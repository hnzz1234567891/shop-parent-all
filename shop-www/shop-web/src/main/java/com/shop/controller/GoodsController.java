package com.shop.controller;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shop.core.base.BaseController;
import com.shop.core.base.PageInfo;
import com.shop.core.base.ResultListInfo;
import com.shop.core.constant.GoodsOrders;
import com.shop.core.dto.GoodsDto;
import com.shop.core.model.Goods;
import com.shop.core.model.ProductCategory;
import com.shop.core.vo.GoodsDetailVo;
import com.shop.core.vo.GoodsVo;
import com.shop.service.GoodsService;
import com.shop.service.ProductCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by TW on 2017/6/10.
 */
@Controller
@RequestMapping("goods")
public class GoodsController extends BaseController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping("search")
    public String search( GoodsDto goodsDto, Model model) {
        PageList<GoodsVo> result = goodsService.search(goodsDto);
        model.addAttribute("result", result);
        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(result.getPaginator(), pageInfo);
        model.addAttribute("paginator", pageInfo);
        model.addAttribute("goodsDto", goodsDto);
        GoodsOrders goodsOrders = GoodsOrders.findBySort(goodsDto.getSort());
        model.addAttribute("goodsOrders", goodsOrders);
        model.addAttribute("goodsAllOrders", GoodsOrders.values());
        return "goods/search";
    }

    @RequestMapping("searchJSON")
    @ResponseBody
    public ResultListInfo searchForJson(GoodsDto goodsDto) {
        PageList<GoodsVo> result = goodsService.search(goodsDto);
        return success(result);
    }

    @RequestMapping("list/{productCategoryId}")
    public String list(@PathVariable(value = "productCategoryId") Integer categoryId, Model model, GoodsDto goodsDto) {
        model.addAttribute("productCategoryId", categoryId);

        // 分页获取数据
//        Map<String, Object> result = goodsService.list(goodsDto, categoryId);
        Object[] result = goodsService.list(goodsDto, categoryId);
//        PageList<GoodsVo> pageList = (PageList<GoodsVo>)result.get("pageList");
        PageList<GoodsVo> pageList = (PageList<GoodsVo>)result[0];
        model.addAttribute("result", pageList);
        model.addAttribute("productCategory", result[1]);

        PageInfo pageInfo = new PageInfo();
        BeanUtils.copyProperties(pageList.getPaginator(), pageInfo);
        model.addAttribute("paginator", pageInfo);
        model.addAttribute("goodsDto", goodsDto);
        GoodsOrders goodsOrders = GoodsOrders.findBySort(goodsDto.getSort());
        model.addAttribute("goodsOrders", goodsOrders);
        model.addAttribute("goodsAllOrders", GoodsOrders.values());
        return "goods/list";
    }

    @RequestMapping("content/{id}")
    public String findById(@PathVariable Integer id, Model model) {

        Map<String, Object> result = goodsService.findById(id);
        model.addAllAttributes(result);
        ProductCategory productCategory = (ProductCategory)result.get("productCategory");
        model.addAttribute("productCategoryId", productCategory.getId());
        return "goods/detail";

    }

    @RequestMapping("content/{id}.json")
    @ResponseBody
    public Map<String, Object> findByIdJson(@PathVariable Integer id) {

        Map<String, Object> result = goodsService.findById(id);
        return result;
    }
}
