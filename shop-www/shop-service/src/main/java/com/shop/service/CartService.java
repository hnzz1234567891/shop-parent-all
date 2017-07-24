package com.shop.service;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.shop.core.base.BaseDto;
import com.shop.core.constant.Constant;
import com.shop.core.exception.ParamException;
import com.shop.core.model.Cart;
import com.shop.core.model.CartItem;
import com.shop.core.model.Product;
import com.shop.core.util.AssertUtil;
import com.shop.dao.CartDao;
import com.shop.dao.CartItemDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by TW on 2017/6/17.
 */
@Service
public class CartService {

    @Autowired
    private ProductService productService;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private CartItemDao carteItemDao;


    /**
     * 添加到购物车
     * @param productId 商品ID
     * @param quantity 数量
     * @param loginUserId 登录用户ID
     * @Param goodsId 货品ID
     */
    public void add(Integer productId, Integer quantity, Integer loginUserId, Integer goodsId) {

        // 基本参数验证
//        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");
//        AssertUtil.isTrue(productId == null || productId < 1, "请选择商品进行添加");
//        AssertUtil.isTrue(quantity == null || quantity < 1, "请选择商品数量");
        if (goodsId != null && goodsId > 1 && (productId == null || productId < 1)) { // 如果是列表页面传入的货品ID，则获取默认的商品
            Product defaultProduct = productService.findDefaultProduct(goodsId);
            productId = defaultProduct.getId();
        }
        checkParams(productId, quantity, loginUserId);


        // 判断商品是否存在，库存
        Product product = productService.findById(productId);
        // 库存不足
        AssertUtil.isTrue(quantity > product.getAvailableStock(), "该商品库存不足，请修改数量");
        // 查询用户购物车是否存在：不存在新建一个购物车
        Cart cart = cartDao.findMemberCart(loginUserId);
        if (cart == null) { // 新增购物车
//            cart = new Cart();
//            cart.setCartKey("");
//            cart.setExpire(new Date(new Date().getTime() + Cart.TIMEOUT));
//            cart.setMember(loginUserId);
//            cart.setCreateDate(new Date());
//            cart.setModifyDate(new Date());
//            cart.setVersion(0);
//            cartDao.insert(cart);
//            Integer cartId = cart.getId(); // 购物车ID
//
//            // 添加商品到购物车明细
//            CartItem cartItem = new CartItem();
//            cartItem.setCart(cartId);
//            cartItem.setProduct(productId);
//            cartItem.setQuantity(quantity);
//            cartItem.setCreateDate(new Date());
//            cartItem.setModifyDate(new Date());
//            cartItem.setVersion(0);
//            carteItemDao.insert(cartItem);
            addNewCart(productId, quantity, loginUserId);
            return;
        }

        // 存在购物车，添加购物车明细--》如果这个商品存在购物车就更新数量，如果不存在就添加到购物车
        Integer cartId = cart.getId();
        CartItem productCartItem = carteItemDao.findCartProduct(productId, cartId);
        if (productCartItem == null) { // 插入记录
            // 添加商品到购物车明细
            addCartItem(productId, quantity, cartId);
//            CartItem cartItem = new CartItem();
//            cartItem.setCart(cartId);
//            cartItem.setProduct(productId);
//            cartItem.setQuantity(quantity);
//            cartItem.setCreateDate(new Date());
//            cartItem.setModifyDate(new Date());
//            cartItem.setVersion(0);
//            carteItemDao.insert(cartItem);
        } else { // 更新数量
            carteItemDao.updateQuantity(productCartItem.getId(), quantity);
        }

    }

    /**
     * 统计用户购物车中商品的数量
     * @param loginUserId
     * @return
     */
    public Integer countQuantity(Integer loginUserId) {
        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");
        Integer count = carteItemDao.count(loginUserId);
        if (count == null) {
            count = 0;
        }
        return count;
    }

    /**
     * 分页获取用户的购车商品信息
     * @param baseDto
     * @param loginUserId
     * @return
     */
    public PageList<CartItem> selectForPage(BaseDto baseDto, Integer loginUserId) {
        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");

        // 获取用户购物车
        Cart cart = cartDao.findMemberCart(loginUserId);
        if (cart == null) {
            return new PageList<>();
        }
        // 添加默认排序
        baseDto.setSort("id.desc");
        PageList<CartItem> cartItems = carteItemDao.selectForPage(baseDto.buildPageBounds(), cart.getId());

        return cartItems;
    }

    /**
     * 更新购物车商品数量
     * @param loginUserId
     * @param id
     * @param quantity
     */
    public void updateQuantity(Integer loginUserId, Integer id, Integer quantity) {
        // 基本参数验证
        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");
        AssertUtil.isTrue(id == null || id < 1, "请选择购物车明细");
        AssertUtil.isTrue(quantity == null || quantity < 1, "请输入数量");

        // 验证购物车明细
        Cart cart = cartDao.findMemberCart(loginUserId);
        AssertUtil.isTrue(cart == null, "请往购物车中添加商品");
        CartItem cartItem = carteItemDao.findById(id, cart.getId());
        AssertUtil.isTrue(cartItem == null, "该明细不存在，请重试");
        // 商品库存判断
        Product product = productService.findById(cartItem.getProduct());
        AssertUtil.isTrue(product == null || product.getAvailableStock() < quantity,
                "该商品库存不够");
        // 更新商品数量
        carteItemDao.editQuantity(id, quantity);
    }

    /**
     * 删除明细
     * @param loginUserId
     * @param id
     */
    public void delete(Integer loginUserId, Integer id) {
        // 基本参数验证
        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");
        AssertUtil.isTrue(id == null || id < 1, "请选择购物车明细");

        // 验证购物车明细
        Cart cart = cartDao.findMemberCart(loginUserId);
        AssertUtil.isTrue(cart == null, "请往购物车中添加商品");
        CartItem cartItem = carteItemDao.findById(id, cart.getId());
        AssertUtil.isTrue(cartItem == null, "该明细不存在，请重试");

        // 删除
        carteItemDao.delete(id);
    }

    /**
     * 清空购物车
     * @param loginUserId
     */
    public void deleteAll(Integer loginUserId) {
        // 把购物车删除 （update xx_cart set version = -1 where member = #{loginUserId}）
        // 清空商品
        Cart cart = cartDao.findMemberCart(loginUserId);
        AssertUtil.isTrue(cart == null, "请往购物车中添加商品");

        carteItemDao.deleteCartProduct(cart.getId());
    }

    /**
     * 根据明细ID获取明细列表
     * @param cartItemIds
     * @return
     */
    public List<CartItem> findByIds(String cartItemIds, Integer loginUserId) {
        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");
        AssertUtil.notNull(cartItemIds, "请选择购买的商品");
        // 获取当前用户的购物车
        Cart cart = cartDao.findMemberCart(loginUserId);
        AssertUtil.isTrue(cart == null, "请往购物车中添加商品");


        List<CartItem> cartItems = carteItemDao.findByIds(cartItemIds, cart.getId());
        return cartItems;
    }

    /**
     * 基本参数验证
     * @param productId
     * @param quantity
     * @param loginUserId
     */
    private void checkParams(Integer productId, Integer quantity, Integer loginUserId) {
        if (loginUserId == null || loginUserId < 1) {
            throw new ParamException(Constant.LOGIN_CODE, "请登录");
        }
        AssertUtil.isTrue(productId == null || productId < 1, "请选择商品进行添加");
        AssertUtil.isTrue(quantity == null || quantity < 1, "请选择商品数量");
    }

    /**
     * 添加新的购物车
     * @param productId
     * @param quantity
     * @param loginUserId
     */
    private void addNewCart(Integer productId, Integer quantity, Integer loginUserId) {
        Cart cart = new Cart();
        cart.setCartKey("");
        cart.setExpire(new Date(new Date().getTime() + Cart.TIMEOUT));
        cart.setMember(loginUserId);
        cart.setCreateDate(new Date());
        cart.setModifyDate(new Date());
        cart.setVersion(0);
        cartDao.insert(cart);
        Integer cartId = cart.getId(); // 购物车ID

        // 添加商品到购物车明细
        if (productId != null) {
            addCartItem(productId, quantity, cartId);
        }

    }

    /**
     * 添加购物车明细
     * @param productId
     * @param quantity
     * @param cartId
     */
    private void addCartItem(Integer productId, Integer quantity, Integer cartId) {
        // 添加商品到购物车明细
        CartItem cartItem = new CartItem();
        cartItem.setCart(cartId);
        cartItem.setProduct(productId);
        cartItem.setQuantity(quantity);
        cartItem.setCreateDate(new Date());
        cartItem.setModifyDate(new Date());
        cartItem.setVersion(0);
        carteItemDao.insert(cartItem);
    }

    /**
     * 下单后从购物车中移除
     * @param cartItemIds
     */
    public void deleteByIds(String cartItemIds) {
        AssertUtil.isTrue(StringUtils.isBlank(cartItemIds), "请选择明细");
        carteItemDao.deleteByIds(cartItemIds);
    }
}
