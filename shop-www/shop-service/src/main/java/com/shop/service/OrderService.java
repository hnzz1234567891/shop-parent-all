package com.shop.service;

import com.shop.core.constant.OrderStatus;
import com.shop.core.constant.OrderType;
import com.shop.core.constant.PayConstant;
import com.shop.core.dto.PayCallbackDto;
import com.shop.core.model.*;
import com.shop.core.util.AssertUtil;
import com.shop.core.util.IdWorker;
import com.shop.core.util.MathUtil;
import com.shop.core.util.pay.Md5Util;
import com.shop.core.util.pay.PayUtil;
import com.shop.core.vo.PayRequestVo;
import com.shop.dao.OrderDao;
import com.shop.dao.OrderItemDao;
import com.shop.dao.ProductDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by TW on 2017/6/19.
 */
@Service
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private ReceiverService receiverService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductDao productDao;

    public String add(Integer loginUserId, String cartItemIds,
                      Integer receiverId, String memo) throws Exception {

//        a) 基本参数校验
        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");
        AssertUtil.isTrue(StringUtils.isBlank(cartItemIds), "请选择商品");
        AssertUtil.isTrue(receiverId == null || receiverId < 1, "请选择收货地址");
//        c) 收货地址校验
        Receiver receiver = receiverService.findById(receiverId);
        AssertUtil.isTrue(receiver == null || !receiver.getMember().equals(loginUserId), "该地址不存在");

//        b）明细中的商品校验：商品是否存在，库存是否足够
        List<CartItem> cartItems = cartService.findByIds(cartItemIds, loginUserId);

        BigDecimal totalAmount = BigDecimal.ZERO; // 总金额
        int totalQuantity = 0; // 总数量
        long totalRewardPoints = 0L; // 总积分
        int totalWeight = 0;  // 总重量
        List<OrderItem> orderItems = new ArrayList<>(); // 明细集合
        for (CartItem cartItem: cartItems) {
            Product product = cartItem.getProductInfo();
            AssertUtil.isTrue(product == null, "某个不存在");
            AssertUtil.isTrue(cartItem.getQuantity() == null || cartItem.getQuantity() < 1, "请选择商品数量");
            AssertUtil.isTrue(product.getAvailableStock() < cartItem.getQuantity(),
                    product.getName() + "库存为："+ product.getAvailableStock() +"，请修改数量");
            // add()=加 multiply()=乘
            totalAmount = totalAmount.add(product.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
            totalQuantity += cartItem.getQuantity();
            totalRewardPoints += product.getRewardPoint() * cartItem.getQuantity();
            totalWeight += product.getWeight() * cartItem.getQuantity();
        }
//        e) 添加订单（生成唯一的订单编号）：
        String sn = generateOrderSn();
        Order order = insertOrder(sn,totalAmount, totalRewardPoints, 0L,
                totalWeight, totalQuantity, receiver, memo, loginUserId);
//        f) 添加订单明细
        addOrderItems(cartItems, order.getId());

//        h) 从购物车中删除
        cartService.deleteByIds(cartItemIds);

//        i) 返回订单编号
        return sn;
    }

    /**
     * 根据订单编号获取支付请求数据
     * @param orderNo
     * @return
     */
    public PayRequestVo findByOrderNo(String orderNo, Integer loginUserId) {

        // 参数验证
        AssertUtil.isTrue(StringUtils.isBlank(orderNo), "订单号不能为空");
        // 查询订单
        Order order = orderDao.findByOrderNo(orderNo, loginUserId);
        AssertUtil.isTrue(order == null, "该订单不存在，请确认后重试");
        // 验证订单的状态
        AssertUtil.isTrue(OrderStatus.pendingPayment.getStatus() != order.getStatus(),
                "该订单支付状态不正确，请确认后重试");

        // 生成一个签名sign
        String amountStr = MathUtil.setScale( order.getAmount()).toString();
        String sign = PayUtil.buildSign(orderNo, amountStr);

        // 构建一个返回实体
        PayRequestVo payRequestVo = new PayRequestVo();
        payRequestVo.setSign(sign);
        payRequestVo.setUserSeller(PayConstant.USER_SELLER);
        payRequestVo.setTotalFee(amountStr);
        payRequestVo.setSubject(PayConstant.SUBJECT);
        payRequestVo.setReturnUrl(PayConstant.RETURN_URL);
        payRequestVo.setPartner(PayConstant.PARTNER);
        payRequestVo.setOutOrderNo(orderNo);
        payRequestVo.setNotifyUrl(PayConstant.NOTIFY_URL);
        payRequestVo.setBody(PayConstant.DESC);
        return payRequestVo;
    }

    /**
     * 处理回调
     * @param payCallbackDto
     */
    public Map<String, Object> handlerCallback(PayCallbackDto payCallbackDto, Integer loginUserId) {
//        1)	回调参数验证
        AssertUtil.isTrue(loginUserId == null || loginUserId < 1, "请登录");
        String total_fee = payCallbackDto.getTotal_fee(); //	交易金额
        AssertUtil.isTrue(total_fee == null, PayConstant.CALLBACK_FAILURE);
        String out_order_no = payCallbackDto.getOut_order_no(); // 商户订单号
        AssertUtil.isTrue(StringUtils.isBlank(out_order_no), PayConstant.CALLBACK_FAILURE);
        String sign = payCallbackDto.getSign(); // 服务端校验码
        AssertUtil.isTrue(StringUtils.isBlank(sign), PayConstant.CALLBACK_FAILURE);
        String trade_no = payCallbackDto.getTrade_no(); // 云通付交易订单号
        AssertUtil.isTrue(StringUtils.isBlank(trade_no), PayConstant.CALLBACK_FAILURE);
        String trade_status = payCallbackDto.getTrade_status(); // 交易结果（TRADE_SUCCESS说明支付成功）
        AssertUtil.isTrue(StringUtils.isBlank(trade_status), PayConstant.CALLBACK_FAILURE);

//        2)	签名认证：将out_order_no、total_fee、trade_status、云通付PID、云通付KEY的值连接起来，进行md5加密，而后与sign进行对比
        String preStr = out_order_no + total_fee + trade_status + PayConstant.PARTNER + PayConstant.KEY;
        Md5Util md5Util = new Md5Util();
        String generateSign = md5Util.encode(preStr, null);
        AssertUtil.isTrue(!sign.equals(generateSign), "签名验证失败，请联系客服");
//        3)	支付状态验证trade_status= TRADE_SUCCESS
        AssertUtil.isTrue(!trade_status.equals(PayConstant.TRADE_SUCCESS), PayConstant.CALLBACK_FAILURE);
//        4)	订单验证
        Order order = orderDao.findByOrderNo(out_order_no, loginUserId);
        AssertUtil.isTrue(order == null, "订单异常，请联系客服");
        AssertUtil.isTrue(order.getStatus() != OrderStatus.pendingPayment.getStatus(), "订单状态异常，请联系客服");
//        5)	订单金额比对
        BigDecimal amount = order.getAmount();
        // 将金额精度精确到2位 四舍五入在转化为字符串
        String priceStr = MathUtil.setScale(amount).toString();
        AssertUtil.isTrue(!total_fee.equals(priceStr), "订单金额不正确，请联系客服");
//        6)	更新订单状态
        int upt = orderDao.updateStatus(out_order_no, OrderStatus.pendingShipment.getStatus());
        AssertUtil.isTrue(upt == 0, "支付回调失败，请联系客服");
        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", out_order_no);
        result.put("money", priceStr);

        return  result;
    }

    /**
     * 插入数据库
     * @param sn
     * @param totalPrice
     * @param totalRewardPoint
     * @param totalExchangePoint
     * @param totalWeight
     * @param totalQuantity
     * @param receiver
     * @param memo
     * @param loginUserId
     * @return
     */
    private Order insertOrder(String sn, BigDecimal totalPrice, Long totalRewardPoint,
                              Long totalExchangePoint, Integer totalWeight, Integer totalQuantity,
                              Receiver receiver, String memo, Integer loginUserId) {
        Order order = new Order();
        order.setSn(sn);
        order.setType(OrderType.general.getType());
        order.setPrice(MathUtil.setScale(totalPrice)); // 四舍五入 精度保留两位
        order.setFee(BigDecimal.ZERO);
        order.setFreight(BigDecimal.ZERO);
        order.setPromotionDiscount(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setAmountPaid(BigDecimal.ZERO);
        order.setRefundAmount(BigDecimal.ZERO);
        order.setRewardPoint(totalRewardPoint);
        order.setExchangePoint(totalExchangePoint);
        order.setWeight(totalWeight);
        order.setQuantity(totalQuantity);
        order.setShippedQuantity(0);
        order.setReturnedQuantity(0);
        order.setExpire(new Date(new Date().getTime() + 1800000)); // 半小时过期

        // 订单地址
        order.setConsignee(receiver.getConsignee());
        order.setAreaName(receiver.getAreaName());
        order.setAddress(receiver.getAddress());
        order.setZipCode(receiver.getZipCode());
        order.setPhone(receiver.getPhone());
        order.setArea(receiver.getArea());

        order.setMemo(memo);
        order.setIsUseCouponCode(false);
        order.setIsExchangePoint(false);
        order.setIsAllocatedStock(false);
        order.setMember(loginUserId);
        order.setAmount(MathUtil.setScale(totalPrice)); // 总金额
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setTax(BigDecimal.ZERO);

        // 保存订单
        orderDao.insert(order);
        return order;
    }

    /**
     * 生成订单编号(时间戳 + 自增序列)
     * @return
     * @throws Exception
     */
    private String generateOrderSn() throws Exception {
        Integer lastOrderId = orderDao.findLastOrderId();
        if (lastOrderId == null) {
            lastOrderId = 0;
        }
        IdWorker idWorker = new IdWorker(lastOrderId);
        String sn = idWorker.nextId();
        return sn;
    }

    /**
     * 添加明细以及更新库存
     * @param cartItems
     * @param orderId
     */
    private void addOrderItems(List<CartItem> cartItems, Integer orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProductInfo();
            OrderItem orderItem = new OrderItem();
            orderItem.setSn(product.getSn());
            orderItem.setName(product.getName());
            orderItem.setType(product.getType());
            orderItem.setPrice(product.getPrice());
            orderItem.setWeight(product.getWeight());
            orderItem.setIsDelivery(product.getIsDelivery());
            orderItem.setThumbnail(product.getThumbnail());
            orderItem.setQuantity(cartItem.getQuantity()); // 购买商品数量
            orderItem.setShippedQuantity(0);
            orderItem.setReturnedQuantity(0);
            orderItem.setProduct(cartItem.getProduct()); // 商品ID
            orderItem.setOrders(orderId);
            orderItem.setSpecifications(product.getSpecificationValues()); // 规格项
            orderItems.add(orderItem);
            // g) 扣库存(如果订单未支付或者支付失败后都有回收订单的机制)
            int upt = productDao.updateAllocatedStock(product.getId(), product.getAllocatedStock() + cartItem.getQuantity());
            AssertUtil.isTrue(upt == 0, "商品["+ product.getName() +"]库存不足"); // 避免高并发情况下库存多扣
        }
        orderItemDao.insertBatch(orderItems); // 批量插入
    }
}
