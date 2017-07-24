package com.shop.dao;

import com.shop.core.model.Area;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by TW on 2017/6/18.
 */
public interface AreaDao {

    @Select("select t.id, t.full_name, t.`name`, t.tree_path, t.parent from xx_area t where grade = 0 ORDER BY orders")
    List<Area> findRoots();

    @Select("select t.id, t.full_name, t.`name`, t.tree_path, t.parent from xx_area t where parent=#{parentId} ORDER BY orders")
    List<Area> findChildren(@Param(value="parentId")Integer parentId);

    @Select("select t.id, t.full_name, t.`name`, t.tree_path, t.parent from xx_area t where id=#{id}")
    Area findById(Integer area);
}
