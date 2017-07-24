package com.shop.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shop.core.base.BaseController;
import com.shop.core.base.ResultInfo;
import com.shop.core.model.Area;
import com.shop.service.AreaService;

/**
 * Created by TW on 2017/6/10.
 */
@Controller
@RequestMapping("area")
public class AreaController extends BaseController {

    @Autowired
    private AreaService areaService;

    @RequestMapping("list")
    @ResponseBody
    public ResultInfo findAreas(Integer parentId) {

        List<Area> areas = areaService.findAreas(parentId);

        return success(areas);
    }

}
