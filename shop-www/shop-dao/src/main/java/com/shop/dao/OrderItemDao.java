package com.shop.dao;

import com.shop.core.model.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by TW on 2017/6/19.
 */
public interface OrderItemDao {

    void insertBatch(@Param(value = "orderItems") List<OrderItem> orderItems);
}
