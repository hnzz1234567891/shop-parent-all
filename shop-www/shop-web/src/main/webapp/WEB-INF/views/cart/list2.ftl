<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>购物车</title>
    <link href="/css/common.css" rel="stylesheet" type="text/css" />
    <link href="/css/cart.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/js/jquery.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>

    <style>
        #cartTable th {
            text-align:center;
        }
        #cartTable tr td {
            text-align:center;
        }
    </style>

    <script type="text/javascript">
        $().ready(function() {

            var $quantity = $("#cartTable input[name='quantity']");
            var $checkbox = $("#cartTable input[name='id']");
            var $increase = $("#cartTable span.increase");
            var $decrease = $("#cartTable span.decrease");
            var $delete = $("#cartTable a.delete");
            var $selectAll = $("#selectAll")
            var $gift = $("#gift");
            var $promotion = $("#promotion");
            var $effectiveRewardPoint = $("#effectiveRewardPoint");
            var $effectivePrice = $("#effectivePrice");
            var $clear = $("#clear");
            var $submit = $("#submit");
            var timeouts = {};

            // 全选
            $selectAll.click(function () {
                if ($(this).attr('checked')) {
                    $checkbox.attr('checked', $(this).attr('checked'));
                    var totalPrice = 0;
                    $checkbox.each(function() {
                        var id = $(this).val();
                        var subTotal = parseInt($("#subtotal_" + id).val());
                        totalPrice += subTotal;
                    });
                    $("#effectivePrice").html(currency(totalPrice, true, true));
                } else {
                    $checkbox.attr('checked', false);
                    $("#effectivePrice").html(currency(0, true, true));
                }
            });

            $checkbox.click(function () {

                var totalPrice = $("#effectivePrice").html();
                totalPrice = totalPrice.substring(0, totalPrice.indexOf("."));
                totalPrice = parseInt(totalPrice.replace(/[^0-9]/ig,""));
                var id = $(this).val();
                var subTotal = parseInt($("#subtotal_" + id).val());
                if($(this).attr('checked')) {
                    totalPrice += subTotal;
                } else {
                    totalPrice -= subTotal;
                }
                $("#effectivePrice").html(currency(totalPrice, true, true));
            });

            // 数量
            $quantity.keypress(function(event) {
                return (event.which >= 48 && event.which <= 57) || event.which == 8;
            });

            // 增加数量
            $increase.click(function() {
                var $quantity = $(this).parent().siblings("input");
                var quantity = $quantity.val();
                if (/^\d*[1-9]\d*$/.test(quantity)) {
                    $quantity.val(parseInt(quantity) + 1);
                } else {
                    $quantity.val(1);
                }
                // 更新总数量
                updateCartAmount(1);
                edit($quantity);
            });

            // 减少数量
            $decrease.click(function() {
                var $quantity = $(this).parent().siblings("input");
                var quantity = $quantity.val();
                if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 1) {
                    $quantity.val(parseInt(quantity) - 1);
                    // 更新总数量
                    updateCartAmount(-1);
                } else {
                    $quantity.val(1);
                }
                edit($quantity);
            });

            // 编辑数量
            $quantity.on("input propertychange change", function(event) {
                var quantity = $(this).val();
                var oldValue = parseInt($(this).attr('oldValue'));
                if (isNaN(quantity) || quantity < 1) {
                    $.message('warn', '请输入正确的数量');
                    $(this).val(oldValue);
                    return;
                }
                if (event.type != "propertychange" || event.originalEvent.propertyName == "value") {
                    // 更新总数量
                    updateCartAmount(quantity - oldValue);
                    edit($(this));
                }
            });

            // 编辑数量
            function edit($quantity) {
                var quantity = $quantity.val();
                if (!/^\d*[1-9]\d*$/.test(quantity)) {
                    $.message('error', "商品数量必须是整数");
                    return false;
                }
                if (parseInt(quantity) < 1) {
                    $.message('error', "商品数量必须大于0");
                    return false;
                }
                // 获取主键
                var id = $quantity.attr('itemid');
                $.ajax({
                    url: "edit",
                    type: "POST",
                    data: {id: id, quantity: quantity},
                    dataType: "json",
                    cache: false,
                    beforeSend: function() {
                        $submit.prop("disabled", true);
                    },
                    success: function(data) {
                        if (data.resultCode == 0) {
                            $.message('error', data.resultMessage);
                            setTimeout(function() {
                                location.reload(true);
                            }, 3000);
                            return;
                        } else {
                            $quantity.attr('oldvalue', quantity);
                        }
                    },
                    complete: function() {
                        $submit.prop("disabled", false);
                    }
                });
            }

            // 删除
            $delete.click(function() {
                if (confirm("您确定要删除吗？")) {
                    var $this = $(this);
                    var $tr = $this.closest("tr");
                    var id = $tr.find("input[name='id']").val();
                    var url = "delete/" + id;
                    $.ajax({
                        url: url,
                        type: "POST",
                        data: {},
                        dataType: "json",
                        cache: false,
                        beforeSend: function() {
                            $submit.prop("disabled", true);
                        },
                        success: function(data) {
                            if (data.resultCode == 0) {
                                $.message('error', data.resultMessage);
                            } else {
                                $.message('success', data.resultMessage);
                            }
                            setTimeout(function() {
                                location.reload(true);
                            }, 1000);
                            return;
                        },
                        complete: function() {
                            $submit.prop("disabled", false);
                        }
                    });
                }
                return false;
            });

            // 清空
            $clear.click(function() {
                if (confirm("您确定要清空吗？")) {
                    $.ajax({
                        url: "clear",
                        type: "POST",
                        dataType: "json",
                        cache: false,
                        data: {},
                        success: function(data) {
                            location.reload(true);
                        }
                    });
                }
                return false;
            });

            // 提交
            $submit.click(function() {
                var cartItemIds = [];
                $("#cartTable input[name='id']:checked").each(function() { // 获取选中的checkbox
                    cartItemIds.push($(this).val());
                });
                if (cartItemIds.length < 1) {
                    $.message('warn', "请选择商品进行提交");
                }
                var url = "/order/checkout?cartItemIds=" + cartItemIds.join(",");
                window.location.href = url;
            });

            // 右上角数量中数量的更新
            function updateCartAmount(amount) {
                var $headerEm = $("#headerCart em");
                var totalAmount = parseInt($headerEm.html());
                if (isNaN(totalAmount)) {
                    totalAmount = 0;
                }
                $headerEm.html(totalAmount + amount);
            }

            $.pageSkip = function(pageNumber) {
                var $pageNumber = $("#pageNumber");
                $pageNumber.val(pageNumber);
                $("#paginatorForm").submit();
                return false;
            }
        });
    </script>
</head>
<body>
<script type="text/javascript">
    $().ready(function() {

        var $headerName = $("#headerName");
        var $headerLogin = $("#headerLogin");
        var $headerRegister = $("#headerRegister");
        var $headerLogout = $("#headerLogout");
        var $goodsSearchForm = $("#goodsSearchForm");
        var $keyword = $("#goodsSearchForm input");
        var defaultKeyword = "商品搜索";

        var username = getCookie("username");
        var nickname = getCookie("nickname");
        if ($.trim(nickname) != "") {
            $headerName.text(nickname).show();
            $headerLogout.show();
        } else if ($.trim(username) != "") {
            $headerName.text(username).show();
            $headerLogout.show();
        } else {
            $headerLogin.show();
            $headerRegister.show();
        }

        $keyword.focus(function() {
            if ($.trim($keyword.val()) == defaultKeyword) {
                $keyword.val("");
            }
        });

        $keyword.blur(function() {
            if ($.trim($keyword.val()) == "") {
                $keyword.val(defaultKeyword);
            }
        });

        $goodsSearchForm.submit(function() {
            if ($.trim($keyword.val()) == "" || $keyword.val() == defaultKeyword) {
                return false;
            }
        });

    });
</script>
<div class="header">
    <div class="top">
        <div class="topNav">
            <ul class="left">
                <li>
                    <span>您好，欢迎来到商HAI购</span>
                    <span id="headerName" class="headerName" style="display: inline;">abcde</span>
                </li>
                <li id="headerLogout" class="headerLogout" style="display: inline;">
                    <a href="/user/logout">[退出]</a>
                </li>
            </ul>
            <ul class="right">
                <li>
                    <a href="/member/index.jhtml"  >
                        会员中心
                    </a>|
                </li>
                <li>
                    <a href="/article/list/3"  >
                        帮助中心
                    </a>|
                </li>

                <li id="headerCart" class="headerCart">
                    <a href="/cart/list">购物车</a>
                    (<em>0</em>)
                </li>
            </ul>
        </div>
    </div>
    <div class="container">
        <div class="row">
            <div class="span3">
                <a href="/index">
                    <img src="/upload/image/logo.gif" alt="尚HAI购" />
                </a>
            </div>
            <div class="span6">
                <div class="search">
                    <form id="goodsSearchForm" action="/goods/search" method="get">
                        <input name="keyword" class="keyword" value="" autocomplete="off" x-webkit-speech="x-webkit-speech" x-webkit-grammar="builtin:search" maxlength="30" />
                        <button type="submit">&nbsp;</button>
                    </form>
                </div>
                <div class="hotSearch">
                    热门搜索:
                    <a href="/goods/search?keyword=苹果">苹果</a>
                    <a href="/goods/search?keyword=三星">三星</a>
                    <a href="/goods/search?keyword=索尼">索尼</a>
                    <a href="/goods/search?keyword=华为">华为</a>
                    <a href="/goods/search?keyword=魅族">魅族</a>
                    <a href="/goods/search?keyword=佳能">佳能</a>
                    <a href="/goods/search?keyword=美的">美的</a>
                    <a href="/goods/search?keyword=华硕">华硕</a>
                    <a href="/goods/search?keyword=格力">格力</a>
                </div>
            </div>
            <div class="span3">
                <div class="phone">
                    <em>服务电话</em>
                    800-8888888
                </div>
            </div>
        </div>
        <div class="row">
            <div class="span12">
                <dl class="mainNav">
                    <dt>
                        <a href="/product_category">所有商品分类</a>
                    </dt>
                    <dd>
                        <a href="/" >
                            首页
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/1" >
                            手机数码
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/2.jhtml" >
                            电脑办公
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/3.jhtml" >
                            家用电器
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/4.jhtml" >
                            服装鞋靴
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/5.jhtml" >
                            化妆护理
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/241.jhtml" >
                            积分商城
                        </a>
                    </dd>
                </dl>
            </div>
        </div>
    </div>
</div>
<script>
    $.post("/cart/count", {}, function (data) {
        if (data.resultCode == 1) {
            var count = data.result;
            $("#headerCart").find('em').html(count);
        }
    });
    // 登录
    function loginRegister (url) {
        var redirectUrl = window.location.href; // 获取当前的url
        if (redirectUrl.indexOf("/login") > -1 || redirectUrl.indexOf("/register") > -1 ) {
            redirectUrl = '';
        }
        // encodeURIComponent转码
        window.location.href = "/"+ url +"?redirectUrl=" + encodeURIComponent(redirectUrl);
    }


</script>
<div class="container cart">
    <div class="row">
        <div class="span12">
            <div class="step">
                <ul>
                    <li class="current">查看购物车</li>
                    <li>订单结算</li>
                    <li>订单完成</li>
                </ul>
            </div>
            <table id="cartTable" class="cartTable">
                <tr>
                    <th>全选&nbsp;&nbsp;<input id="selectAll" type="checkbox" /></th>
                    <th>图片</th>
                    <th>商品</th>
                    <th>价格</th>
                    <th>数量</th>
                    <th>小计</th>
                    <th>操作</th>
                </tr>
                <input type="hidden" id="cartId" value="4" />
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="100" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="28" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/8f5b4f02-f716-4737-afa6-70606793ffc5-thumbnail.jpg" alt="三星 WB351F" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/24" title="三星 WB351F" target="_blank">三星 WB351F</a>
                    </td>
                    <td>
                        <input type="hidden" id="price_0" value=1500 />
                        ￥1500
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="28" id="quantity_0" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_28" value=1500 />
                        <span class="subtotal">￥1500</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="100" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="27" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg" alt="苹果 iPhone 6" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/2" title="苹果 iPhone 6" target="_blank">苹果 iPhone 6</a>
                        <span class="silver">[银色, 128GB]</span>
                    </td>
                    <td>
                        <input type="hidden" id="price_1" value=6800 />
                        ￥6800
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="27" id="quantity_1" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_27" value=6800 />
                        <span class="subtotal">￥6800</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="99" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="26" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg" alt="苹果 iPhone 6" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/2" title="苹果 iPhone 6" target="_blank">苹果 iPhone 6</a>
                        <span class="silver">[银色, 64GB]</span>
                    </td>
                    <td>
                        <input type="hidden" id="price_2" value=6100 />
                        ￥6100
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="26" id="quantity_2" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_26" value=6100 />
                        <span class="subtotal">￥6100</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="100" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="25" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg" alt="苹果 iPhone 6" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/2" title="苹果 iPhone 6" target="_blank">苹果 iPhone 6</a>
                        <span class="silver">[金色, 64GB]</span>
                    </td>
                    <td>
                        <input type="hidden" id="price_3" value=6100 />
                        ￥6100
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="25" id="quantity_3" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_25" value=6100 />
                        <span class="subtotal">￥6100</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="99" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="24" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/d7f59d79-1958-4059-852c-0d6531788b48-thumbnail.jpg" alt="苹果 iPhone 6" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/2" title="苹果 iPhone 6" target="_blank">苹果 iPhone 6</a>
                        <span class="silver">[金色, 16GB]</span>
                    </td>
                    <td>
                        <input type="hidden" id="price_4" value=5200 />
                        ￥5200
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="24" id="quantity_4" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_24" value=5200 />
                        <span class="subtotal">￥5200</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="97" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="23" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/d61e4a9e-ee3f-4508-ba8f-cd07539c4072-thumbnail.jpg" alt="OPPO R8007 R1S" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/15" title="OPPO R8007 R1S" target="_blank">OPPO R8007 R1S</a>
                    </td>
                    <td>
                        <input type="hidden" id="price_5" value=2600 />
                        ￥2600
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="23" id="quantity_5" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_23" value=2600 />
                        <span class="subtotal">￥2600</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="100" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="22" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/7d70f669-8373-41fe-b386-939a121c54b2-thumbnail.jpg" alt="三星 Galaxy S4 I9507V" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/14" title="三星 Galaxy S4 I9507V" target="_blank">三星 Galaxy S4 I9507V</a>
                    </td>
                    <td>
                        <input type="hidden" id="price_6" value=1999 />
                        ￥1999
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="22" id="quantity_6" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_22" value=1999 />
                        <span class="subtotal">￥1999</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="100" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="21" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/8ad872a6-55f9-49b2-9575-1d079cd9aed6-thumbnail.jpg" alt="三星 G3559" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/13" title="三星 G3559" target="_blank">三星 G3559</a>
                    </td>
                    <td>
                        <input type="hidden" id="price_7" value=600 />
                        ￥600
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="21" id="quantity_7" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_21" value=600 />
                        <span class="subtotal">￥600</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="98" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="20" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/296785e4-1b10-4676-bc20-7b4d76a6f86a-thumbnail.jpg" alt="HTC Desire 820u" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/17" title="HTC Desire 820u" target="_blank">HTC Desire 820u</a>
                    </td>
                    <td>
                        <input type="hidden" id="price_8" value=1800 />
                        ￥1800
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="20" id="quantity_8" oldValue="2" name="quantity" value="2" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_20" value=3600 />
                        <span class="subtotal">￥3600</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
                <tr>
                    <td width="60">
                        <input type="hidden" name="availableStock" value="100" />
                        <input type="hidden" name="productId" value="" />
                        <input type="checkbox" name="id" value="19" />
                    </td>
                    <td width="60">
                        <img src="http://image.demo.shopxx.net/4.0/201501/36191a74-0fc6-400e-8b1c-0e5cc6f4ed88-thumbnail.jpg" alt="酷派 8908" />
                    </td>
                    <td  style="text-align:left">
                        <a href="/goods/content/19" title="酷派 8908" target="_blank">酷派 8908</a>
                    </td>
                    <td>
                        <input type="hidden" id="price_9" value=800 />
                        ￥800
                    </td>
                    <td class="quantity" width="60">
                        <input type="text" itemId="19" id="quantity_9" oldValue="1" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span class="increase">&nbsp;</span>
                            <span class="decrease">&nbsp;</span>
                        </div>
                    </td>
                    <td width="140">
                        <input type="hidden" id="subtotal_19" value=800 />
                        <span class="subtotal">￥800</span>
                    </td>
                    <td>
                        <a href="javascript:;" class="delete">删除</a>
                    </td>
                </tr>
            </table>

            <form action="/cart/list" id="paginatorForm" >
                <div class="pagination">
                    <input type="hidden" name="page" id="pageNumber" value="1" />
                    <input type="hidden" name="pageSize" value="10" />
                    <span class="firstPage">&nbsp;</span>
                    <span class="previousPage">&nbsp;</span>

                    <span class="currentPage">1</span>
                    <a href="javascript: $.pageSkip(2);">2</a>

                    <a href="javascript: $.pageSkip(2);" class="nextPage">&nbsp;</a>

                    <a href="javascript: $.pageSkip(2);" class="lastPage">&nbsp;</a>
                </div>
            </form>

        </div>
    </div>
    <div class="row">
        <div class="span6">
            <dl id="gift" class="gift clearfix hidden">
            </dl>
        </div>
        <div class="span6">
            <div class="total">
                <em id="promotion"></em>
                <em>登录后确认是否享有优惠</em>
                赠送积分: <em id="effectiveRewardPoint">0</em>
                商品金额: <strong id="effectivePrice">￥0.00元</strong>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="span12">
            <div class="bottom">
                <a href="javascript:;" id="clear" class="clear">清空购物车</a>
                <a href="javascript:void(0)" id="submit" class="submit">提交订单</a>
            </div>
        </div>
    </div>
</div>

<div class="footer">
    <div class="service clearfix">
        <dl>
            <dt class="icon1">新手指南</dt>
            <dd>
                <a href="#">购物流程</a>
            </dd>
            <dd>
                <a href="#">会员注册</a>
            </dd>
            <dd>
                <a href="#">购买宝贝</a>
            </dd>
            <dd>
                <a href="#">支付货款</a>
            </dd>
            <dd>
                <a href="#">用户协议</a>
            </dd>
        </dl>
        <dl>
            <dt class="icon2">特色服务</dt>
            <dd>
                <a href="#">购物流程</a>
            </dd>
            <dd>
                <a href="#">会员注册</a>
            </dd>
            <dd>
                <a href="#">购买宝贝</a>
            </dd>
            <dd>
                <a href="#">支付货款</a>
            </dd>
            <dd>
                <a href="#">用户协议</a>
            </dd>
        </dl>
        <dl>
            <dt class="icon3">支付方式</dt>
            <dd>
                <a href="#">购物流程</a>
            </dd>
            <dd>
                <a href="#">会员注册</a>
            </dd>
            <dd>
                <a href="#">购买宝贝</a>
            </dd>
            <dd>
                <a href="#">支付货款</a>
            </dd>
            <dd>
                <a href="#">用户协议</a>
            </dd>
        </dl>
        <dl>
            <dt class="icon4">配送方式</dt>
            <dd>
                <a href="#">购物流程</a>
            </dd>
            <dd>
                <a href="#">会员注册</a>
            </dd>
            <dd>
                <a href="#">购买宝贝</a>
            </dd>
            <dd>
                <a href="#">支付货款</a>
            </dd>
            <dd>
                <a href="#">用户协议</a>
            </dd>
        </dl>
        <div class="qrCode">
            <img src="/images/qr_code.gif" alt="官方微信" />
            官方微信
        </div>
    </div>
    <div class="bottom">
        <div class="bottomNav">
            <ul>
                <li>
                    <a href=" target="_blank">关于我们</a>
                    |
                </li>
                <li>
                    <a href=" target="_blank">联系我们</a>
                    |
                </li>
                <li>
                    <a href=" target="_blank">诚聘英才</a>
                    |
                </li>
                <li>
                    <a href=" target="_blank">隐私政策</a>
                    |
                </li>
                <li>
                    <a href=" target="_blank">法律声明</a>
                    |
                </li>
                <li>
                    <a href=" target="_blank">客户服务</a>
                    |
                </li>
                <li>
                    <a href=" target="_blank">友情链接</a>

                </li>
            </ul>
        </div>
        <div class="info">
            <p>湘ICP备10000000号</p>
            <p>Copyright © 2015-2025 尚HAI购 版权所有</p>
            <ul>
                <li>
                    <a href="http://www.shopxx.net" target="_blank">
                        <img src="http://image.demo.shopxx.net/4.0/201501/1c675feb-e488-4fd5-a186-b28bb6de445a.gif" alt="SHOP++" />
                    </a>
                </li>
                <li>
                    <a href="http://www.alipay.com" target="_blank">
                        <img src="http://image.demo.shopxx.net/4.0/201501/ae13eddc-25ac-427a-875d-d1799d751076.gif" alt="支付宝" />
                    </a>
                </li>
                <li>
                    <a href="http://www.tenpay.com" target="_blank">
                        <img src="http://image.demo.shopxx.net/4.0/201501/adaa9ac5-9994-4aa3-a336-b65613c85d50.gif" alt="财付通" />
                    </a>
                </li>
                <li>
                    <a href="https://www.95516.com" target="_blank">
                        <img src="http://image.demo.shopxx.net/4.0/201501/41c18c8d-f69a-49fe-ace3-f16c2eb07983.gif" alt="中国银联" />
                    </a>
                </li>
                <li>
                    <a href="http://www.kuaidi100.com" target="_blank">
                        <img src="http://image.demo.shopxx.net/4.0/201501/ea46ca0a-e8f0-4e2c-938a-5cb19a07cb9a.gif" alt="快递100" />
                    </a>
                </li>
                <li>
                    <a href="http://www.cnzz.com" target="_blank">
                        <img src="http://image.demo.shopxx.net/4.0/201501/e12f226b-07f9-4895-bcc2-78dbe551964b.gif" alt="站长统计" />
                    </a>
                </li>
                <li>
                    <a href="http://down.admin5.com" target="_blank">
                        <img src="http://image.demo.shopxx.net/4.0/201501/fd9d6268-e4e2-41f6-856d-4cb8a49eadd1.gif" alt="A5下载" />
                    </a>
                </li>
                <li>
                    <a href="http://www.ccb.com" target="_blank">
                        <img src="http://image.demo.shopxx.net/4.0/201501/6c57f398-0498-4044-80d8-20f6c40d5cef.gif" alt="中国建设银行" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
</body>
</html>