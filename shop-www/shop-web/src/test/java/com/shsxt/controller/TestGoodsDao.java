package com.shsxt.controller;

import com.shop.core.vo.GoodsDetailVo;
import com.shop.dao.GoodsDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by TW on 2017/6/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-servlet.xml"
        , "classpath:spring-mybatis.xml", "classpath:mybatis-conf.xml"})
public class TestGoodsDao {

    @Autowired
    private GoodsDao goodsDao;

    @Value("${sms.free.sign.name}")
    private String signName;

    @Test
    public void testFindById() {
        GoodsDetailVo goodsDetailVo = goodsDao.findById(1);
        System.out.println(goodsDetailVo.getName());


        System.out.println(signName);
    }

}
