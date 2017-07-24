package com.shop.service;

import com.shop.core.exception.ParamException;
import com.shop.core.model.Product;
import com.shop.core.util.AssertUtil;
import com.shop.dao.ProductDao;
import com.sun.xml.internal.ws.policy.AssertionSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by TW on 2017/6/14.
 */
@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;

    public Product findDefaultProduct(Integer goodsId) {
        if (goodsId == null || goodsId < 1) {
            throw new ParamException("请选择货品");
        }
        Product product = productDao.findDefaultProduct(goodsId);
        AssertUtil.isTrue(product == null, "默认商品不存在，请联系客服");
        return product;
    }

    public List<Product> findGoodsProducts(Integer goodsId) {
        if (goodsId == null || goodsId < 1) {
            throw new ParamException("请选择货品");
        }
        List<Product> products = productDao.findGoodsProducts(goodsId);
        return products;

    }

    /**
     * 根据主键获取商品信息
     * @param productId
     * @return
     */
    public Product findById(Integer productId) {
        AssertUtil.isTrue(productId == null || productId < 1, "请选择商品");
        Product product = productDao.findById(productId);
        AssertUtil.isTrue(product == null, "该商品不存在，请重新选择");
        return product;
    }
}
