package com.shop.service;

import com.shop.core.model.Area;
import com.shop.dao.AreaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by TW on 2017/6/18.
 */
@Service
public class AreaService {


    @Autowired
    private AreaDao areaDao;

    /**
     * 根据父级查询子级
     * @param parentId
     * @return
     */
    public List<Area> findAreas(Integer parentId) {
        List<Area> areas = null;

        if(parentId == null || parentId < 1) { // 查询根据地区
            areas = areaDao.findRoots();
        } else {
            areas = areaDao.findChildren(parentId);
        }
        return areas;
    }
}
