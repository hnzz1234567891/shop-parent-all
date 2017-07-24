<!DOCTYPE html >
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>订单结算</title>
    <link href="${ctx}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/css/order.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.lSelect.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function() {

            var $dialogOverlay = $("#dialogOverlay");
            var $receiverForm = $("#receiverForm");
            var $receiverItem = $("#receiver ul");
            var $otherReceiverButton = $("#otherReceiverButton");
            var $newReceiverButton = $("#newReceiverButton");
            var $newReceiver = $("#newReceiver");
            var $areaId = $("#areaId");
            var $newReceiverSubmit = $("#newReceiverSubmit");
            var $newReceiverCancelButton = $("#newReceiverCancelButton");
            var $orderForm = $("#orderForm");
            var $receiverId = $("#receiverId");
            var $paymentMethod = $("#paymentMethod");
            var $shippingMethod = $("#shippingMethod");
            var $paymentMethodId = $("#paymentMethod input:radio");
            var $shippingMethodId = $("#shippingMethod input:radio");
            var $isInvoice = $("#isInvoice");
            var $invoiceTitle = $("#invoiceTitle");
            var $code = $("#code");
            var $couponCode = $("#couponCode");
            var $couponName = $("#couponName");
            var $couponButton = $("#couponButton");
            var $freight = $("#freight");
            var $tax = $("#tax");
            var $promotionDiscount = $("#promotionDiscount");
            var $couponDiscount = $("#couponDiscount");
            var $amount = $("#amount");
            var $useBalance = $("#useBalance");
            var $balance = $("#balance");
            var $submit = $("#submit");
            var amount = 3100;
            var amountPayable = 3100;
            var paymentMethodIds = {};
            paymentMethodIds["1"] = [ "1", "2" ];
            paymentMethodIds["2"] = [ "1", "2", "3" ];


            // 地区选择
            $areaId.lSelect({
                url: "${ctx}/area/list"
            });

            // 收货地址
            $("#receiver li").click(function() {
                $receiverId.val($(this).attr("receiverid"));
                $(this).addClass("selected").siblings().removeClass('selected');
            });

            // 其它收货地址
            $otherReceiverButton.click(function() {
                if ($receiverItem.attr("style") != null) {
                    $receiverItem.removeAttr("style");
                    $otherReceiverButton.html('更多');
                } else {
                    $receiverItem.height("auto");
                    $otherReceiverButton.html('更少');
                }
//                $otherReceiverButton.hide();
                $newReceiverButton.removeClass("hidden");
            });

            // 新收货地址
            $newReceiverButton.click(function() {
                $receiverItem.height("auto");
                $receiverForm.find("input:text").val("");
                $dialogOverlay.show();
                $newReceiver.show();
            });

            // 新收货地址取消
            $newReceiverCancelButton.click(function() {
                if ($receiverId.val() == "") {
                    $.message("warn", "必须新增一个收货地址");
                    return false;
                }
                $dialogOverlay.hide();
                $newReceiver.hide();
            });

            // 计算
            function calculate() {
                /*
                $.ajax({
                    url: "calculate.jhtml",
                    type: "GET",
                    data: $orderForm.serialize(),
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                        if (data.message.type == "success") {
                            $freight.text(currency(data.freight, true));
                            if (data.tax > 0) {
                                $tax.text(currency(data.tax, true)).parent().show();
                            } else {
                                $tax.parent().hide();
                            }
                            if (data.promotionDiscount > 0) {
                                $promotionDiscount.text(currency(data.promotionDiscount, true)).parent().show();
                            } else {
                                $promotionDiscount.parent().hide();
                            }
                            if (data.couponDiscount > 0) {
                                $couponDiscount.text(currency(data.couponDiscount, true)).parent().show();
                            } else {
                                $couponDiscount.parent().hide();
                            }
                            if (data.amount != amount) {
                                $balance.val("0");
                                amountPayable = data.amount;
                            } else {
                                amountPayable = data.amountPayable;
                            }
                            amount = data.amount;
                            $amount.text(currency(amount, true, true));
                            if (amount > 0) {
                                $useBalance.parent().show();
                            } else {
                                $useBalance.parent().hide();
                            }
                            if (amountPayable > 0) {
                                $paymentMethod.show();
                            } else {
                                $paymentMethod.hide();
                            }
                        } else {
                            $.message(data.message);
                            setTimeout(function() {
                                location.reload(true);
                            }, 3000);
                        }
                    }
                });*/
            }

            // 支付方式
            $paymentMethodId.click(function() {
                var $this = $(this);
                if ($this.prop("disabled")) {
                    return false;
                }
                $this.closest("dd").addClass("selected").siblings().removeClass("selected");
                var paymentMethodId = $this.val();
                $shippingMethodId.each(function() {
                    var $this = $(this);
                    if ($.inArray(paymentMethodId, paymentMethodIds[$this.val()]) >= 0) {
                        $this.prop("disabled", false);
                    } else {
                        $this.prop("disabled", true).prop("checked", false).closest("dd").removeClass("selected");
                    }
                });
                calculate();
            });

            // 配送方式
            $shippingMethodId.click(function() {
                var $this = $(this);
                if ($this.prop("disabled")) {
                    return false;
                }
                $this.closest("dd").addClass("selected").siblings().removeClass("selected");
                var shippingMethodId = $this.val();
                $paymentMethodId.each(function() {
                    var $this = $(this);
                    if ($.inArray($this.val(), paymentMethodIds[shippingMethodId]) >= 0) {
                        $this.prop("disabled", false);
                    } else {
                        $this.prop("disabled", true).prop("checked", false).closest("dd").removeClass("selected");
                    }
                });
                calculate();
            });

            // 开据发票
            $isInvoice.click(function() {
                if ($(this).prop("checked")) {
                    $invoiceTitle.prop("disabled", false).closest("tr").show();
                } else {
                    $invoiceTitle.prop("disabled", true).closest("tr").hide();
                }
                calculate();
            });

            // 发票抬头
            $invoiceTitle.focus(function() {
                if ($.trim($invoiceTitle.val()) == "个人") {
                    $invoiceTitle.val("");
                }
            });

            // 发票抬头
            $invoiceTitle.blur(function() {
                if ($.trim($invoiceTitle.val()) == "") {
                    $invoiceTitle.val("个人");
                }
            });

            // 优惠券
            $couponButton.click(function() {
                if ($code.val() == "") {
                    if ($.trim($couponCode.val()) == "") {
                        return false;
                    }
                    $.ajax({
                        url: "check_coupon.jhtml",
                        type: "GET",
                        data: {code : $couponCode.val()},
                        dataType: "json",
                        cache: false,
                        beforeSend: function() {
                            $couponButton.prop("disabled", true);
                        },
                        success: function(data) {
                            if (data.message.type == "success") {
                                $code.val($couponCode.val());
                                $couponCode.hide();
                                $couponName.text(data.couponName).show();
                                $couponButton.text("取 消");
                                calculate();
                            } else {
                                $.message('error', data.resultMessage);
                            }
                        },
                        complete: function() {
                            $couponButton.prop("disabled", false);
                        }
                    });
                } else {
                    $code.val("");
                    $couponCode.show();
                    $couponName.hide();
                    $couponButton.text("确 认");
                    calculate();
                }
            });

            // 使用余额
            $useBalance.click(function() {
                var $this = $(this);
                if ($this.prop("checked")) {
                    $balance.prop("disabled", false).parent().show();
                } else {
                    $balance.prop("disabled", true).parent().hide();
                }
                calculate();
            });

            // 余额
            $balance.keypress(function(event) {
                return (event.which >= 48 && event.which <= 57) || (event.which == 46 && $(this).val().indexOf(".") < 0) || event.which == 8;
            });

            // 余额
            $balance.change(function() {
                var $this = $(this);
                if (/^\d+(\.\d{0,2})?$/.test($this.val())) {
                    var max = 0 >= amount ? amount : 0;
                    if (parseFloat($this.val()) > max) {
                        $this.val(max);
                    }
                } else {
                    $this.val("0");
                }
                calculate();
            });

            // 订单提交
            $submit.click(function() {
                if (amountPayable > 0) {
                    if ($paymentMethodId.filter(":checked").size() <= 0) {
                        $.message("warn", "请选择支付方式");
                        return false;
                    }
                } else {
                    $paymentMethodId.prop("disabled", true);
                }

                if ($shippingMethodId.filter(":checked").size() <= 0) {
                    $.message("warn", "请选择配送方式");
                    return false;
                }

                if ($isInvoice.prop("checked") && $.trim($invoiceTitle.val()) == "") {
                    $.message("warn", "请填写发票抬头");
                    return false;
                }
                $.ajax({
                    url: "create",
                    type: "POST",
                    data: $orderForm.serialize(),
                    dataType: "json",
                    cache: false,
                    beforeSend: function() {
                        $submit.prop("disabled", true);
                    },
                    success: function(data) {
                        if (data.resultCode == 1) {
                            location.href = "${ctx}/pay/page/" + data.result;
                        } else {
                            $.message('error', data.resultMessage);
                            setTimeout(function() {
                                location.reload(true);
                            }, 3000);
                        }
                    },
                    complete: function() {
                        $submit.prop("disabled", false);
                    }
                });
            });

            // 收货地址表单验证--jquery validation
            $receiverForm.validate({
                rules: {
                    consignee: "required",
                    area: "required",
                    address: "required",
                    zipCode: {
                        required: true,
                        pattern: /^\d{6}$/
                    },
                    phone: {
                        required: true,
                        pattern: /^\d{3,4}-?\d{7,9}$/
                    }
                },
                submitHandler: function(form) {
                    if ($("#isDefault").attr('checked')) {
                        $("#_isDefault").val(true);
                    } else {
                        $("#_isDefault").val(false);
                    }
                    console.log($receiverForm.serialize());
                    $.ajax({
                        url: "${ctx}/receiver/add",
                        type: "POST",
                        data: $receiverForm.serialize(), // 表单序列化
                        dataType: "json",
                        cache: false,
                        beforeSend: function() {
                            $newReceiverSubmit.prop("disabled", true);
                            console.log($receiverForm.serialize())
                        },
                        success: function(data) {
                            if (data.resultCode == 1) { // 成功
                                $receiverId.val(data.result.id);
                                $receiverItem.show();
                                var receiver = data.result;
                                $('<li class="selected" receiverId="' + receiver.id + '"> <span> <strong>' + escapeHtml(receiver.consignee) + '<\/strong> 收 <\/span> <span>' + escapeHtml(receiver.areaName + receiver.address) + '<\/span> <span>' + escapeHtml(receiver.phone) + '<\/span> <\/li>'	).appendTo($receiverItem).siblings().removeClass("selected");
                                $dialogOverlay.hide();
                                $newReceiver.hide();
                                //calculate();
                            } else {
                                $.message("error", data.resultMessage);
                            }
                        },
                        complete: function() {
                            $newReceiverSubmit.prop("disabled", false);
                        }
                    });
                }
            });

        });
    </script>
</head>
<body>
<div id="dialogOverlay" class="dialogOverlay"></div>
[#include "includer/header.ftl"]
<div class="container checkout">
    <div class="row">
        <div class="span12">
            <div class="step">
                <ul>
                    <li>查看购物车</li>
                    <li class="current">订单结算</li>
                    <li>订单完成</li>
                </ul>
            </div>
            <form id="receiverForm" method="post">
                <div id="receiver" class="receiver">
                    <div class="title">收货地址</div>
                    [#assign defaultReceiverId = 0 /]
                    <ul class="clearfix" style="height: auto;">
                        [#if receivers?has_content]
                            [#list receivers as receiver ]
                                [#if receiver.isDefault ]
                                    [#assign defaultReceiverId = receiver.id /]
                                [/#if]

                                <li [#if receiver.isDefault ]class="selected"[/#if] receiverid="${receiver.id}">
                                    <span> <strong>${receiver.consignee}</strong> 收 </span>
                                    <span>${receiver.areaName}${receiver.address}</span>
                                    <span>${receiver.phone}</span>
                                </li>
                            [/#list]
                        [/#if]
                    </ul>
                    <div>
                        <a href="javascript:;" id="newReceiverButton" class="button">使用新地址</a>
                    </div>
                </div>
                <div id="newReceiver" class="newReceiver [#if receivers?has_content]hidden[/#if]">
                    <table>
                        <tr>
                            <th width="100">
                                <span class="requiredField">*</span>收货人:
                            </th>
                            <td>
                                <input type="text" id="consignee" name="consignee" class="text" maxlength="200" />
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>地区:
                            </th>
                            <td>
                                <span class="fieldSet">
                                    <input type="hidden" id="areaId" name="area" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>地址:
                            </th>
                            <td>
                                <input type="text" id="address" name="address" class="text" maxlength="200" />
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>邮编:
                            </th>
                            <td>
                                <input type="text" id="zipCode" name="zipCode" class="text" maxlength="200" />
                            </td>
                        </tr>
                        <tr>
                            <th>
                                <span class="requiredField">*</span>电话:
                            </th>
                            <td>
                                <input type="text" id="phone" name="phone" class="text" maxlength="200" />
                            </td>
                        </tr>
                        <tr>
                            <th>
                                是否默认:
                            </th>
                            <td>
                                <input type="checkbox" id="isDefault" />
                                <input type="hidden" name="isDefault" id="_isDefault" value=false />
                            </td>
                        </tr>
                        <tr>
                            <th>
                                &nbsp;
                            </th>
                            <td>
                                <input type="submit" id="newReceiverSubmit" class="button" value="确 定" />
                                <input type="button" id="newReceiverCancelButton" class="button" value="取 消" />
                            </td>
                        </tr>
                    </table>
                </div>
            </form>
        </div>
    </div>
    <form id="orderForm" action="create" method="post">
        <div class="row">
            <div class="span12">
                <input type="hidden" id="receiverId" name="receiverId" value="${defaultReceiverId}" />
                <input type="hidden" id="cartItemIds" name="cartItemIds" value="${cartItemIds}" />
                <dl id="paymentMethod" class="select">
                    <dt>支付方式</dt>
                    <dd>
                        <label for="paymentMethod_1">
                            <input type="radio" id="paymentMethod_1" name="paymentMethodId" value="1" />
                            <span>
											<em style="border-right: none; background: url(http://image.demo.shopxx.net/4.0/201501/b0b6da31-6abf-4824-8dfa-c1f251732e20.gif) center no-repeat;">&nbsp;</em>
										<strong>网上支付</strong>
									</span>
                            <span>支持支付宝、财付通、快钱以及大多数网上银行支付</span>
                        </label>
                    </dd>
                    <dd>
                        <label for="paymentMethod_2">
                            <input type="radio" id="paymentMethod_2" name="paymentMethodId" value="2" />
                            <span>
											<em style="border-right: none; background: url(http://image.demo.shopxx.net/4.0/201501/c0a00d6b-b144-43c3-ad49-ac226be9288f.gif) center no-repeat;">&nbsp;</em>
										<strong>银行汇款</strong>
									</span>
                            <span>支持工商银行、建设银行、农业银行汇款支付，收款时间一般为汇款后的1-2个工作日</span>
                        </label>
                    </dd>
                    <dd>
                        <label for="paymentMethod_3">
                            <input type="radio" id="paymentMethod_3" name="paymentMethodId" value="3" />
                            <span>
											<em style="border-right: none; background: url(http://image.demo.shopxx.net/4.0/201501/ac183b5f-edcb-48b7-9961-f50f1b16d45c.gif) center no-repeat;">&nbsp;</em>
										<strong>货到付款</strong>
									</span>
                            <span>由快递公司送货上门，您签收后直接将货款交付给快递员</span>
                        </label>
                    </dd>
                </dl>
                <dl id="shippingMethod" class="select">
                    <dt>配送方式</dt>
                    <dd>
                        <label for="shippingMethod_1">
                            <input type="radio" id="shippingMethod_1" name="shippingMethodId" value="1" />
                            <span>
												<em style="border-right: none; background: url(http://image.demo.shopxx.net/4.0/201501/473d43a5-f519-4d31-bc96-a90599aaf4a7.gif) center no-repeat;">&nbsp;</em>
											<strong>普通快递</strong>
										</span>
                            <span>系统将根据您的收货地址自动匹配快递公司进行配送，享受免运费服务</span>
                        </label>
                    </dd>
                    <dd>
                        <label for="shippingMethod_2">
                            <input type="radio" id="shippingMethod_2" name="shippingMethodId" value="2" />
                            <span>
												<em style="border-right: none; background: url(http://image.demo.shopxx.net/4.0/201501/769c0550-1f8f-4313-a2c8-f79c30162b96.gif) center no-repeat;">&nbsp;</em>
											<strong>顺丰速运</strong>
										</span>
                            <span>支持货到付款，不享受免运费服务</span>
                        </label>
                    </dd>
                </dl>
                <table>
                    <tr>
                        <th colspan="2">发票信息</th>
                    </tr>
                    <tr>
                        <td width="100">
                            开据发票:
                        </td>
                        <td>
                            <label for="isInvoice">
                                <input type="checkbox" id="isInvoice" name="isInvoice" value="true" />
                                (税金: 6%)
                            </label>
                        </td>
                    </tr>
                    <tr class="hidden">
                        <td width="100">
                            发票抬头:
                        </td>
                        <td>
                            <input type="text" id="invoiceTitle" name="invoiceTitle" class="text" value="个人" maxlength="200" disabled="disabled" />
                        </td>
                    </tr>
                </table>
                <table class="product">
                    <tr>
                        <th width="60">图片</th>
                        <th>商品</th>
                        <th>价格</th>
                        <th>数量</th>
                        <th>小计</th>
                    </tr>
                    [#assign totalPrice = 0 /]
                    [#assign totalPoints = 0 /]
                    [#if cartItems?has_content]
                        [#list cartItems as cartItem]
                            <tr>
                                <td>
                                    <img src="${cartItem.productInfo.thumbnail}" alt="${cartItem.productInfo.name}" />
                                </td>
                                <td>
                                    <a href="${ctx}/goods/content/${cartItem.productInfo.goods}" title="${cartItem.productInfo.name}"
                                       target="_blank">${cartItem.productInfo.name}</a>
                                    [#if cartItem.productInfo.getSpecifications()?has_content]
                                        <span class="silver">[${cartItem.productInfo.getSpecifications()?join(", ")}]</span>
                                    [/#if]
                                </td>
                                <td>
                                    ￥${cartItem.productInfo.price}
                                </td>
                                <td>
                                    ${cartItem.quantity}
                                </td>
                                <td>
                                    ￥${cartItem.productInfo.price*cartItem.quantity}
                                    [#assign totalPrice = totalPrice + cartItem.productInfo.price*cartItem.quantity /]
                                    [#assign totalPoints = totalPoints + cartItem.productInfo.rewardPoint*cartItem.quantity /]
                                </td>
                            </tr>
                        [/#list]
                    [/#if]
                </table>
            </div>
        </div>
        <div class="row">
            <div class="span6">
                <dl class="memo">
                    <dt>附言:</dt>
                    <dd>
                        <input type="text" name="memo" maxlength="200" />
                    </dd>
                </dl>
                <dl class="coupon">
                    <dt>优惠券:</dt>
                    <dd>
                        <input type="hidden" id="code" name="code" maxlength="200" />
                        <input type="text" id="couponCode" maxlength="200" />
                        <span id="couponName">&nbsp;</span>
                        <button type="button" id="couponButton">确 认</button>
                    </dd>
                </dl>
            </div>
            <div class="span6">
                <ul class="statistic">
                    <li>
                        <span>
                            运费: <em id="freight">￥0.00</em>
                        </span>
                        <span class="hidden">
                            税金: <em id="tax">￥0.00</em>
                        </span>
                        <span>
                            赠送积分: <em>${totalPoints}</em>
                        </span>
                    </li>
                    <li>
                        <span class="hidden">
                            促销折扣: <em id="promotionDiscount">￥0.00</em>
                        </span>
                        <span class="hidden">
                            优惠券折扣: <em id="couponDiscount">￥0.00</em>
                        </span>
                    </li>
                    <li>
                        <span>
                            订单金额: <strong id="amount">￥${totalPrice}</strong>
                        </span>
                    </li>
                </ul>
            </div>
        </div>
        <div class="row">
            <div class="span12">
                <div class="bottom">
                    <a href="${ctx}/cart/list" class="back">返回购物车</a>
                    <a href="javascript:;" id="submit" class="submit">提交订单</a>
                </div>
            </div>
        </div>
    </form>
</div>
[#include "includer/footer.ftl"]
</body>
</html>