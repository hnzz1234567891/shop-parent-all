<!DOCTYPE html >
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <link href="/css/common.css" rel="stylesheet" type="text/css" />
    <link href="/css/goods.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/js/jquery.js"></script>
    <script type="text/javascript" src="/js/jquery.lazyload.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function() {
            var $headerCart = $("#headerCart");
            var $compareBar = $("#compareBar");
            var $compareForm = $("#compareBar form");
            var $compareSubmit = $("#compareBar a.submit");
            var $clearCompare = $("#compareBar a.clear");
            var $goodsForm = $("#goodsForm");
            var $orderType = $("#orderType");
            var $pageNumber = $("#pageNumber");
            var $pageSize = $("#pageSize");
            var $gridType = $("#gridType");
            var $listType = $("#listType");
            var $size = $("#layout a.size");
            var $previousPage = $("#previousPage");
            var $nextPage = $("#nextPage");
            var $sort = $("#sort a, #sort li");
            var $orderMenu = $("#orderMenu");
            var $startPrice = $("#startPrice");
            var $endPrice = $("#endPrice");
            var $result = $("#result");
            var $productImage = $("#result img");
            var $addCart = $("#result a.addCart");
            var $exchange = $("#result a.exchange");
            var $addFavorite = $("#result a.addFavorite");
            var $addCompare = $("#result a.addCompare");

            var layoutType = getCookie("layoutType");
            if (layoutType == "listType") {
                $listType.addClass("currentList");
                $result.removeClass("grid").addClass("list");
            } else {
                $gridType.addClass("currentGrid");
                $result.removeClass("list").addClass("grid");
            }

            $gridType.click(function() {
                var $this = $(this);
                if (!$this.hasClass("currentGrid")) {
                    $this.addClass("currentGrid");
                    $listType.removeClass("currentList");
                    $result.removeClass("list").addClass("grid");
                    addCookie("layoutType", "gridType");
                }
                return false;
            });

            $listType.click(function() {
                var $this = $(this);
                if (!$this.hasClass("currentList")) {
                    $this.addClass("currentList");
                    $gridType.removeClass("currentGrid");
                    $result.removeClass("grid").addClass("list");
                    addCookie("layoutType", "listType");
                }
                return false;
            });

            $size.click(function() {
                var $this = $(this);
                $pageNumber.val(1);
                var pageSize = $this.attr("pageSize");
                $pageSize.val(pageSize);
                $goodsForm.submit();
                return false;
            });

            $previousPage.click(function() {
                $pageNumber.val(1);
                $goodsForm.submit();
                return false;
            });

            $nextPage.click(function() {
                $pageNumber.val(2);
                $goodsForm.submit();
                return false;
            });

            $orderMenu.hover(
                    function() {
                        $(this).children("ul").show();
                    }, function() {
                        $(this).children("ul").hide();
                    }
            );

            $sort.click(function() {
                var $this = $(this);
                if ($this.hasClass("current")) {
                    $orderType.val("");
                } else {
                    $orderType.val($this.attr("orderType"));
                }
                $pageNumber.val(1);
                $goodsForm.submit();
                return false;
            });

            $startPrice.add($endPrice).focus(function() {
                $(this).siblings("button").show();
            });

            $startPrice.add($endPrice).keypress(function(event) {
                return (event.which >= 48 && event.which <= 57) || (event.which == 46 && $(this).val().indexOf(".") < 0) || event.which == 8 || event.which == 13;
            });

            $goodsForm.submit(function() {
                if ($orderType.val() == "" || $orderType.val() == "topDesc") {
                    $orderType.prop("disabled", true);
                }
                if ($pageNumber.val() == "" || $pageNumber.val() == "1") {
                    $pageNumber.prop("disabled", true);
                }
                /*
                if ($pageSize.val() == "" || $pageSize.val() == "20") {
                    $pageSize.prop("disabled", true);
                }*/
                if ($startPrice.val() == "" || !/^\d+(\.\d+)?$/.test($startPrice.val())) {
                    $startPrice.prop("disabled", true);
                }
                if ($endPrice.val() == "" || !/^\d+(\.\d+)?$/.test($endPrice.val())) {
                    $endPrice.prop("disabled", true);
                }
                if ($goodsForm.serializeArray().length < 1) {
                    location.href = location.pathname;
                    return false;
                }
            });

            $productImage.lazyload({
                threshold: 100,
                effect: "fadeIn"
            });

            // 加入购物车
            $addCart.click(function() {
                var $this = $(this);
                var productId = $this.attr("productId");
                $.ajax({
                    url: "/cart/add",
                    type: "POST",
                    data: {goodsId: productId, quantity: 1},
                    dataType: "json",
                    cache: false,
                    success: function(message) {
                        if (message.resultCode == 1) {
                            var $image = $this.closest("li").find("img");
                            var cartOffset = $headerCart.offset();
                            var imageOffset = $image.offset();
                            $image.clone().css({
                                width: 170,
                                height: 170,
                                position: "absolute",
                                "z-index": 20,
                                top: imageOffset.top,
                                left: imageOffset.left,
                                opacity: 0.8,
                                border: "1px solid #dddddd",
                                "background-color": "#eeeeee"
                            }).appendTo("body").animate({
                                width: 30,
                                height: 30,
                                top: cartOffset.top,
                                left: cartOffset.left,
                                opacity: 0.2
                            }, 1000, function() {
                                $(this).remove();
                            });
                            $.message('success', message.result);
                            $headerCart.find('em').html(parseInt($headerCart.find('em').html()) + 1);
                        } else if (message.resultCode == -1) {
                            $.message('error', message.resultMessage);
                            setTimeout(function(){
                                loginRegister("login");
                            }, 1000)
                        } else {
                            $.message('error', message.resultMessage);
                        }
                    }
                });
                return false;
            });

            // 积分兑换
            $exchange.click(function() {
                var productId = $(this).attr("productId");
                $.ajax({
                    url: "/shopxx/order/check_exchange.jhtml",
                    type: "GET",
                    data: {productId: productId, quantity: 1},
                    dataType: "json",
                    cache: false,
                    success: function(message) {
                        if (message.type == "success") {
                            location.href = "/shopxx/order/checkout.jhtml?type=exchange&productId=" + productId + "&quantity=1";
                        } else {
                            $.message(message);
                        }
                    }
                });
                return false;
            });

            // 添加商品收藏
            $addFavorite.click(function() {
                var goodsId = $(this).attr("goodsId");
                $.ajax({
                    url: "/shopxx/member/favorite/add.jhtml",
                    type: "POST",
                    data: {goodsId: goodsId},
                    dataType: "json",
                    cache: false,
                    success: function(message) {
                        $.message(message);
                    }
                });
                return false;
            });

            // 对比栏
            var compareGoods = getCookie("compareGoods");
            var compareGoodsIds = compareGoods != null ? compareGoods.split(",") : [];
            if (compareGoodsIds.length > 0) {
                $.ajax({
                    url: "/shopxx/goods/compare_bar.jhtml",
                    type: "GET",
                    data: {goodsIds: compareGoodsIds},
                    dataType: "json",
                    cache: true,
                    success: function(data) {
                        $.each(data, function (i, item) {
                            var thumbnail = item.thumbnail != null ? item.thumbnail : "/shopxx/upload/image/default_thumbnail.jpg";
                            $compareBar.find("dt").after(
                                    '<dd> <input type="hidden" name="goodsIds" value="' + item.id + '" \/> <a href="' + escapeHtml(item.url) + '" target="_blank"> <img src="' + escapeHtml(thumbnail) + '" \/> <span title="' + escapeHtml(item.name) + '">' + escapeHtml(abbreviate(item.name, 50)) + '<\/span> <\/a> <strong>' + currency(item.price, true) + '<del>' + currency(item.marketPrice, true) + '<\/del><\/strong> <a href="javascript:;" class="remove" goodsId="' + item.id + '">[删除]<\/a> <\/dd>'					);
                        });
                        $compareBar.fadeIn();
                    }
                });

                $.each(compareGoodsIds, function(i, goodsId) {
                    $addCompare.filter("[goodsId='" + goodsId + "']").addClass("selected");
                });
            }

            // 移除对比
            $compareBar.on("click", "a.remove", function() {
                var $this = $(this);
                var goodsId = $this.attr("goodsId");
                $this.closest("dd").remove();
                for (var i = 0; i < compareGoodsIds.length; i ++) {
                    if (compareGoodsIds[i] == goodsId) {
                        compareGoodsIds.splice(i, 1);
                        break;
                    }
                }
                $addCompare.filter("[goodsId='" + goodsId + "']").removeClass("selected");
                if (compareGoodsIds.length == 0) {
                    $compareBar.fadeOut();
                    removeCookie("compareGoods");
                } else {
                    addCookie("compareGoods", compareGoodsIds.join(","));
                }
                return false;
            });

            $compareSubmit.click(function() {
                if (compareGoodsIds.length < 2) {
                    $.message("warn", "至少需要两个对比商品");
                    return false;
                }

                $compareForm.submit();
                return false;
            });

            // 清除对比
            $clearCompare.click(function() {
                $addCompare.removeClass("selected");
                $compareBar.fadeOut().find("dd:not(.action)").remove();
                compareGoodsIds = [];
                removeCookie("compareGoods");
                return false;
            });

            // 添加对比
            $addCompare.click(function() {
                var $this = $(this);
                var goodsId = $this.attr("goodsId");
                if ($.inArray(goodsId, compareGoodsIds) >= 0) {
                    return false;
                }
                if (compareGoodsIds.length >= 4) {
                    $.message("warn", "最多只允许添加4个对比商品");
                    return false;
                }
                $.ajax({
                    url: "/shopxx/goods/add_compare.jhtml",
                    type: "GET",
                    data: {goodsId: goodsId},
                    dataType: "json",
                    cache: false,
                    success: function(data) {
                        if (data.message.type == "success") {
                            $this.addClass("selected");
                            var thumbnail = data.thumbnail != null ? data.thumbnail : "/shopxx/upload/image/default_thumbnail.jpg";
                            $compareBar.show().find("dd.action").before(
                                    '<dd> <input type="hidden" name="goodsIds" value="' + data.id + '" \/> <a href="' + escapeHtml(data.url) + '" target="_blank"> <img src="' + escapeHtml(thumbnail) + '" \/> <span title="' + escapeHtml(data.name) + '">' + escapeHtml(abbreviate(data.name, 50)) + '<\/span> <\/a> <strong>' + currency(data.price, true) + '<del>' + currency(data.marketPrice, true) + '<\/del><\/strong> <a href="javascript:;" class="remove" goodsId="' + data.id + '">[删除]<\/a> <\/dd>'					);
                            compareGoodsIds.unshift(goodsId);
                            addCookie("compareGoods", compareGoodsIds.join(","));
                        } else {
                            $.message(data.message);
                        }
                    }
                });
                return false;
            });

            $.pageSkip = function(pageNumber) {
                $pageNumber.val(pageNumber);
                console.log($goodsForm.serialize());
                $goodsForm.submit();
                return false;
            }

            // 品牌搜索
            $brands = $(".brand");
            $brands.click(function() {
                var brandId = $(this).attr('brandid');
                if ($(this).hasClass('current')) {
                    $(this).removeClass('current');
                    $("#brandId").val('');
                } else {
                    $("#brandId").val(brandId);
                }

                $pageNumber.val(1);
                $goodsForm.submit();
                return false;
            });

            // 属性搜索
            $(".attribute").click(function() {
                var attributeValue = $(this).html();
                var id = $(this).attr('id');
                if ($(this).hasClass("current")) {
                    $(this).removeClass("current");
                    $("#attributeValue" + id).val('');
                } else {
                    $(this).addClass("current")
                    $("#attributeValue" + id).val(attributeValue);
                }

                $goodsForm.submit();
                return false;
            });


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
                </li>
                <li id="headerLogin" class="headerLogin">
                    <a href="javascript:loginRegister('login')">登录</a>|
                </li>
                <li id="headerRegister" class="headerRegister">
                    <a href="javscript:loginRegister('register')">注册</a>
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
                    <a href="javascript:loginRegister('login')">购物车</a>
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
<div class="container goodsList">
    <div id="compareBar" class="compareBar">
        <form action="/shopxx/goods/compare.jhtml" method="get">
            <dl>
                <dt>对比栏</dt>
                <dd class="action">
                    <a href="javascript:;" class="submit">对 比</a>
                    <a href="javascript:;" class="clear">清 空</a>
                </dd>
            </dl>
        </form>
    </div>
    <div class="row">
        <div class="span2">
            <div class="hotProductCategory">
                <dl class="odd clearfix">
                    <dt>
                        <a href="/goods/list/1">手机数码</a>
                    </dt>
                    <dd>
                        <a href="/goods/list/7">
                            手机通讯
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/8">
                            手机配件
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/9">
                            摄影摄像
                        </a>
                    </dd>
                </dl>
                <dl class="odd clearfix">
                    <dt>
                        <a href="/goods/list/2">电脑办公</a>
                    </dt>
                    <dd>
                        <a href="/goods/list/14">
                            电脑整机
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/15">
                            电脑配件
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/16">
                            电脑外设
                        </a>
                    </dd>
                </dl>
                <dl class="odd clearfix">
                    <dt>
                        <a href="/goods/list/3">家用电器</a>
                    </dt>
                    <dd>
                        <a href="/goods/list/21">
                            生活电器
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/22">
                            厨卫电器
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/23">
                            个护健康
                        </a>
                    </dd>
                </dl>
                <dl class="odd clearfix">
                    <dt>
                        <a href="/goods/list/4">服装鞋靴</a>
                    </dt>
                    <dd>
                        <a href="/goods/list/26">
                            品质男装
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/27">
                            时尚女装
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/28">
                            精品内衣
                        </a>
                    </dd>
                </dl>
                <dl class="odd clearfix">
                    <dt>
                        <a href="/goods/list/5">化妆护理</a>
                    </dt>
                    <dd>
                        <a href="/goods/list/31">
                            面部护肤
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/32">
                            身体护肤
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/33">
                            口腔护理
                        </a>
                    </dd>
                </dl>
                <dl class="odd clearfix">
                    <dt>
                        <a href="/goods/list/6">家居家装</a>
                    </dt>
                    <dd>
                        <a href="/goods/list/36">
                            家纺布艺
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/37">
                            家居照明
                        </a>
                    </dd>
                    <dd>
                        <a href="/goods/list/38">
                            家装建材
                        </a>
                    </dd>
                </dl>
            </div>		<div class="hotBrand clearfix">
            <dl>
                <dt>热门品牌</dt>
                <dd>
                    <a href="/goods/list/7?brandId=1" title="苹果">
                        <img src="http://image.demo.shopxx.net/4.0/201501/a8275260-f9fa-4e20-8173-35b755fabb14.gif" alt="苹果" />
                        <span>苹果</span>
                    </a>
                </dd>
                <dd class="even">
                    <a href="/goods/list/7?brandId=2" title="三星">
                        <img src="http://image.demo.shopxx.net/4.0/201501/8aa08a42-f5b3-4f52-bea0-5ee8bd123b0c.gif" alt="三星" />
                        <span>三星</span>
                    </a>
                </dd>
                <dd>
                    <a href="/goods/list/7?brandId=3" title="索尼">
                        <img src="http://image.demo.shopxx.net/4.0/201501/dd75c116-51a7-4fbd-b014-6cf4bedcd0bb.gif" alt="索尼" />
                        <span>索尼</span>
                    </a>
                </dd>
                <dd class="even">
                    <a href="/goods/list/7?brandId=4" title="华为">
                        <img src="http://image.demo.shopxx.net/4.0/201501/2a5efa56-c4cd-4984-b11a-d56cadca6cff.gif" alt="华为" />
                        <span>华为</span>
                    </a>
                </dd>
                <dd>
                    <a href="/goods/list/7?brandId=5" title="魅族">
                        <img src="http://image.demo.shopxx.net/4.0/201501/72657c6c-d279-4952-ac20-1abcff776b07.gif" alt="魅族" />
                        <span>魅族</span>
                    </a>
                </dd>
                <dd class="even">
                    <a href="/goods/list/7?brandId=6" title="佳能">
                        <img src="http://image.demo.shopxx.net/4.0/201501/081d4e29-b631-4a49-8672-792a1308ce97.gif" alt="佳能" />
                        <span>佳能</span>
                    </a>
                </dd>
            </dl>
        </div>

        </div>
        <div class="span10">
            <div class="breadcrumb">
                <ul>
                    <li>
                        <a href="/index">首页</a>
                    </li>
                    <li><a href="/goods/list/1">手机数码</a></li>
                    <li>手机通讯</li>
                </ul>
            </div>
            <form id="goodsForm" action="/goods/list/7" method="get">
                <input type="hidden" id="keyword" name="keyword" value="" />
                <input type="hidden" id="orderType" name="sort" value="create_date.desc" />
                <input type="hidden" id="pageNumber" name="page" value="1" />
                <input type="hidden" id="pageSize" name="pageSize" value="10" />
                <input type="hidden" id="brandId" name="brandId" value="" />
                <div id="filter" class="filter">
                    <div class="title">筛选商品</div>
                    <div class="content">
                        <dl class="clearfix">
                            <dt>分类:</dt>
                            <dd>
                                <a href="/goods/list/41">手机</a>
                            </dd>
                            <dd>
                                <a href="/goods/list/42">对讲机</a>
                            </dd>
                            <dd class="moreOption" title="更多">&nbsp;</dd>
                        </dl>

                        <dl class="clearfix">
                            <dt>品牌:</dt>
                            <dd>
                                <a href="javascript:;" class="brand" brandid="1">苹果</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="brand" brandid="2">三星</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="brand" brandid="3">索尼</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="brand" brandid="4">华为</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="brand" brandid="5">魅族</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="brand" brandid="6">佳能</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="brand" brandid="7">尼康</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="brand" brandid="8">松下</a>
                            </dd>
                            <dd class="moreOption" title="更多">&nbsp;</dd>
                        </dl>
                    </div>
                    <div class="content">

                        <dl class="clearfix">
                            <dt>
                                <input type="hidden" name="attributeValue0" id="attributeValue0">
                                <span title="屏幕尺寸">屏幕尺寸:</span>
                            </dt>
                            <dd>
                                <a href="javascript:;" class="attribute current" id="0">3英寸以下</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="attribute" id="0">3-4英寸</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="attribute" id="0">4-5英寸</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="attribute" id="0">5-6英寸</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="attribute" id="0">6-7英寸</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="attribute" id="0">7-8英寸</a>
                            </dd>
                            <dd>
                                <a href="javascript:;" class="attribute" id="0">8英寸以上</a>
                            </dd>
                            <dd class="moreOption" title="更多">&nbsp;</dd>
                        </dl>

                    </div>
                    <div id="moreFilter" class="moreFilter">
                        &nbsp;
                    </div>
                </div>

                <div class="bar">
                    <div id="layout" class="layout">
                        <label>布局:</label>
                        <a href="javascript:;" id="gridType" class="gridType">
                            <span>&nbsp;</span>
                        </a>
                        <a href="javascript:;" id="listType" class="listType">
                            <span>&nbsp;</span>
                        </a>
                        <label>数量:</label>
                        <a href="javascript:;" class="size" pageSize="20">
                            <span>20</span>
                        </a>
                        <a href="javascript:;" class="size" pageSize="40">
                            <span>40</span>
                        </a>
                        <a href="javascript:;" class="size" pageSize="80">
                            <span>80</span>
                        </a>
                        <span class="page">
									<label>共20个商品 1/2</label>
										<a href="javascript:;" id="nextPage" class="nextPage">
											<span>下一页</span>
										</a>
								</span>
                    </div>
                    <div id="sort" class="sort">
                        <div id="orderMenu" class="orderMenu">
                            <span>日期降序</span>
                            <ul>
                                <li orderType="is_top.desc">置顶降序</li>
                                <li orderType="price.asc">价格升序 </li>
                                <li orderType="price.desc">价格降序</li>
                                <li orderType="sales.desc">销量降序</li>
                                <li orderType="score.desc">评分降序</li>
                                <li orderType="create_date.desc">日期降序</li>
                            </ul>
                        </div>
                        <a href="javascript:;" class="asc" orderType="price.asc">价格</a>
                        <a href="javascript:;" class="desc" orderType="sales.desc">销量</a>
                        <a href="javascript:;" class="desc" orderType="score.desc">评分</a>
                        <input type="text" id="startPrice" name="startPrice" class="startPrice" value="" maxlength="16" title="价格过滤最低价格" onpaste="return false" />
                        <label>-</label>
                        <input type="text" id="endPrice" name="endPrice" class="endPrice" value="" maxlength="16" title="价格过滤最高价格" onpaste="return false" />
                        <button type="submit">确 定</button>
                    </div>
                </div>
                <div id="result" class="result grid clearfix">
                    <ul>
                        <li>
                            <a href="/goods/content/20">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/c06f23b9-699a-4582-80b0-72b1f7542dcc-thumbnail.jpg" />
                                <div>
                                    <span title="中兴 V5 Max">中兴 V5 Max</span>
                                    <em title="64位处理理，性能强劲">64位处理理，性能强劲</em>
                                </div>
                            </a>
                            <strong>
                                ￥999
                                <del>￥1198.8</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="20">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="20">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="20">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/19">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/36191a74-0fc6-400e-8b1c-0e5cc6f4ed88-thumbnail.jpg" />
                                <div>
                                    <span title="酷派 8908">酷派 8908</span>
                                    <em title="1300万堆栈式摄像头，丰富拍照功能">1300万堆栈式摄像头，丰富拍照功能</em>
                                </div>
                            </a>
                            <strong>
                                ￥800
                                <del>￥960</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="19">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="19">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="19">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/18">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/d959ab2a-caf9-4a5e-94ff-193d6e561a4d-thumbnail.jpg" />
                                <div>
                                    <span title="酷派 8670">酷派 8670</span>
                                    <em title="智能、便捷、人性化">智能、便捷、人性化</em>
                                </div>
                            </a>
                            <strong>
                                ￥600
                                <del>￥720</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="18">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="18">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="18">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/17">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/296785e4-1b10-4676-bc20-7b4d76a6f86a-thumbnail.jpg" />
                                <div>
                                    <span title="HTC Desire 820u">HTC Desire 820u</span>
                                    <em title="4G双网双通全兼容，5.5英寸高清屏幕">4G双网双通全兼容，5.5英寸高清屏幕</em>
                                </div>
                            </a>
                            <strong>
                                ￥1800
                                <del>￥2160</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="17">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="17">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="17">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/16">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/eb929582-aa50-4835-a3fd-bf518724b011-thumbnail.jpg" />
                                <div>
                                    <span title="HTC Desire 816v">HTC Desire 816v</span>
                                    <em title="至薄时尚外观，超强拍摄体验">至薄时尚外观，超强拍摄体验</em>
                                </div>
                            </a>
                            <strong>
                                ￥1600
                                <del>￥1920</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="16">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="16">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="16">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/15">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/d61e4a9e-ee3f-4508-ba8f-cd07539c4072-thumbnail.jpg" />
                                <div>
                                    <span title="OPPO R8007 R1S">OPPO R8007 R1S</span>
                                    <em title="纤薄轻盈，流光镜面，极至美学">纤薄轻盈，流光镜面，极至美学</em>
                                </div>
                            </a>
                            <strong>
                                ￥2600
                                <del>￥3120</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="15">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="15">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="15">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/14">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/7d70f669-8373-41fe-b386-939a121c54b2-thumbnail.jpg" />
                                <div>
                                    <span title="三星 Galaxy S4 I9507V">三星 Galaxy S4 I9507V</span>
                                    <em title="手势感应，动态照片">手势感应，动态照片</em>
                                </div>
                            </a>
                            <strong>
                                ￥1999
                                <del>￥2398</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="14">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="14">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="14">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/13">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/8ad872a6-55f9-49b2-9575-1d079cd9aed6-thumbnail.jpg" />
                                <div>
                                    <span title="三星 G3559">三星 G3559</span>
                                    <em title="时尚外观设计，4.5英寸显示屏">时尚外观设计，4.5英寸显示屏</em>
                                </div>
                            </a>
                            <strong>
                                ￥600
                                <del>￥720</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="13">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="13">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="13">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/12">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/fa45c26a-9bde-4be0-8bfb-fd254511dea1-thumbnail.jpg" />
                                <div>
                                    <span title="华为 Ascend P7-L09">华为 Ascend P7-L09</span>
                                    <em title="4G极速芯片，7层镀膜复合工艺">4G极速芯片，7层镀膜复合工艺</em>
                                </div>
                            </a>
                            <strong>
                                ￥2500
                                <del>￥3000</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="12">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="12">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="12">&nbsp;</a>
                            </div>
                        </li>
                        <li>
                            <a href="/goods/content/11">
                                <img src="/upload/image/blank.gif" data-original="http://image.demo.shopxx.net/4.0/201501/e127d29f-9874-4d9b-a25d-02861d23c68b-thumbnail.jpg" />
                                <div>
                                    <span title="华为 荣耀">华为 荣耀</span>
                                    <em title="八核处理器，1300万堆栈式摄像头">八核处理器，1300万堆栈式摄像头</em>
                                </div>
                            </a>
                            <strong>
                                ￥1200
                                <del>￥1440</del>
                            </strong>
                            <div class="action">
                                <a href="javascript:;" class="addCart" productId="11">加入购物车</a>
                                <a href="javascript:;" class="addFavorite" title="收藏" goodsId="11">&nbsp;</a>
                                <a href="javascript:;" class="addCompare" title="对比" goodsId="11">&nbsp;</a>
                            </div>
                        </li>
                    </ul>
                </div>
                <div class="pagination">
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
</div></body>
</html>