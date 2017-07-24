package com.shop.dao;

import com.shop.core.model.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * Created by TW on 2017/6/19.
 */
public interface OrderDao {

    @Select("select max(id) from xx_order")
    Integer findLastOrderId();

    void insert(Order order);

    @Select("SELECT id, amount, `status` FROM xx_order where sn = #{sn} and member = #{member}")
    Order findByOrderNo(@Param(value = "sn") String orderNo, @Param(value = "member") Integer member);

    @Update("update xx_order set status = #{status} where sn = #{sn}")
    int updateStatus(@Param(value = "sn")String out_order_no, @Param(value = "status")int status);
}
