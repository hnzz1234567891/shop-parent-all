<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>${good.name}</title>
    <meta name="author" content="${good.name}" />
    <meta name="copyright" content="${good.name}" />
    <meta name="keywords" content="${good.name}" />
    <meta name="description" content="${good.name}" />
    <link href="${ctx}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/css/goods.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.tools.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.jqzoom.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function() {
            var $headerCart = $("#headerCart");
            var $historyGoods = $("#historyGoods");
            var $clearHistoryGoods = $("#historyGoods a.clear");
            var $zoom = $("#zoom");
            var $thumbnailScrollable = $("#thumbnailScrollable");
            var $thumbnail = $("#thumbnailScrollable a");
            var $dialogOverlay = $("#dialogOverlay");
            var $preview = $("#preview");
            var $previewClose = $("#preview a.close");
            var $previewScrollable = $("#previewScrollable");
            var $price = $("#price");
            var $marketPrice = $("#marketPrice");
            var $rewardPoint = $("#rewardPoint");
            var $exchangePoint = $("#exchangePoint");
            var $specification = $("#specification dl");
            var $specificationTips = $("#specification div");
            var $specificationValue = $("#specification a");
            var $productNotifyForm = $("#productNotifyForm");
            var $productNotify = $("#productNotify");
            var $productNotifyEmail = $("#productNotify input");
            var $addProductNotify = $("#addProductNotify");
            var $quantity = $("#quantity");
            var $increase = $("#increase");
            var $decrease = $("#decrease");
            var $addCart = $("#addCart");
            var $exchange = $("#exchange");
            var $addFavorite = $("#addFavorite");
            var $window = $(window);
            var $bar = $("#bar ul");
            var $introductionTab = $("#introductionTab");
            var $parameterTab = $("#parameterTab");
            var $reviewTab = $("#reviewTab");
            var $consultationTab = $("#consultationTab");
            var $introduction = $("#introduction");
            var $parameter = $("#parameter");
            var $review = $("#review");
            var $addReview = $("#addReview");
            var $consultation = $("#consultation");
            var $addConsultation = $("#addConsultation");
            var barTop = $bar.offset().top;
            var barWidth = $bar.width();
            var productId = ${defaultProduct.id};
        [#if products?has_content]
            var productData = {};
            [#list products as product]
                productData["${product.getSpecificationValueIds()?join(",")}"] = {
                    id: ${product.id},
                    price: ${product.price},
                    marketPrice: ${product.marketPrice},
                    rewardPoint: ${product.rewardPoint},
                    exchangePoint: ${product.exchangePoint},
                    availableStock: ${product.getAvailableStock()}
                }
            [/#list]
            // 锁定规格值
            lockSpecificationValue();
        [/#if]

            $zoom.jqzoom({
                zoomWidth: 368,
                zoomHeight: 368,
                title: false,
                preloadText: null,
                preloadImages: false
            });

            // 商品缩略图滚动
            $thumbnailScrollable.scrollable();

            $thumbnail.hover(function() {
                var $this = $(this);
                if ($this.hasClass("current")) {
                    return false;
                }

                $thumbnail.removeClass("current");
                $this.addClass("current").click();
            });

            var previewScrollable = $previewScrollable.scrollable({
                keyboard: true
            });

            // 商品图片预览
            $zoom.click(function() {
                $preview.show().find("img[data-original]").each(function() {
                    var $this = $(this);
                    $this.attr("src", $this.attr("data-original")).removeAttr("data-original");
                });
                previewScrollable.data("scrollable").seekTo($thumbnail.filter(".current").index(), 0);
                $dialogOverlay.show(); // 调用弹窗
                return false;
            });

            $previewClose.click(function() {
                $preview.hide();
                $dialogOverlay.hide();
            });

            // 规格值选择
            $specificationValue.click(function() {
                var $this = $(this);
                if ($this.hasClass("locked")) {
                    return false;
                }
//                if ($this.hasClass("selected")) {
//                    $this.removeClass("selected");
//                } else {
//                    $this.addClass("selected");
//                }
                $this.toggleClass("selected").parent().siblings().children("a").removeClass("selected"); //
                lockSpecificationValue();
                return false;
            });

            // 锁定规格值
            function lockSpecificationValue() {
//                var currentSpecificationValueIds = $specification.map(function() {
//                    $selected = $(this).find("a.selected");
//                    return $selected.size() > 0 ? $selected.attr("val") : [null];
//                }).get();
                var currentSpecificationValueIds = [];
                $("#specification dl").each(function () {
                    var selectedA = $(this).find("a.selected");
                    var id = selectedA.attr("val");
                    currentSpecificationValueIds.push(id);
                });
//                $specification.each(function () {
//                    var val = $(this).find("a.selected").attr('val');
//                    currentSpecificationValueIds.push(val);
//                });

                console.log("currentSpecificationValueIds: "+ currentSpecificationValueIds);

                $specification.each(function(i) {
                    $(this).find("a").each(function(j) {
                        var $this = $(this); // 获取到a标签
                        var specificationValueIds = currentSpecificationValueIds.slice(0);
                        specificationValueIds[i] = $this.attr("val");
                        if (isValid(specificationValueIds)) {
                            $this.removeClass("locked");
                        } else {
                            $this.addClass("locked");
                        }
                    });
                });
                var product = productData[currentSpecificationValueIds.join(",")];
                if (product != null) {
                    productId = product.id;
                    $price.text(currency(product.price, true));
                    $marketPrice.text(currency(product.marketPrice, true));
                    $rewardPoint.text(product.rewardPoint);
                    $exchangePoint.text(product.exchangePoint);
                    if (product.availableStock  < 1) {
                        if ($addProductNotify.val() == "确定登记") {
                            $productNotify.show();
                        }
                        $addProductNotify.show();
                        $quantity.closest("dl").hide();
                        $addCart.hide();
                        $exchange.hide();
                    } else {
                        $productNotify.hide();
                        $addProductNotify.hide();
                        $quantity.closest("dl").show();
                        $addCart.show();
                        $exchange.show();
                    }
                } else {
                    productId = null;
                }
            }

            // 判断规格值ID是否有效
            function isValid(specificationValueIds) {
                for(var key in productData) {
                    var ids = key.split(",");
                    if (match(specificationValueIds, ids)) {
                        return true;
                    }
                }
                return false;
            }

            // 判断数组是否配比
            function match(array1, array2) {
                if (array1.length != array2.length) {
                    return false;
                }
                for(var i = 0; i < array1.length; i ++) {
                    if (array1[i] != null && array2[i] != null && array1[i] != array2[i]) {
                        return false;
                    }
                }
                return true;
            }

            // 到货通知
            $addProductNotify.click(function() {
                if (productId == null) {
                    $specificationTips.fadeIn(150).fadeOut(150).fadeIn(150);
                    return false;
                }
                if ($addProductNotify.val() == "到货通知我") {
                    $addProductNotify.val("确定登记");
                    $productNotify.show();
                    $productNotifyEmail.focus();
                    if ($.trim($productNotifyEmail.val()) == "") {
                        $.ajax({
                            url: "/shopxx/product_notify/email.jhtml",
                            type: "GET",
                            dataType: "json",
                            cache: false,
                            success: function(data) {
                                $productNotifyEmail.val(data.email);
                            }
                        });
                    }
                } else {
                    $productNotifyForm.submit();
                }
                return false;
            });

            // 到货通知表单验证
            $productNotifyForm.validate({
                rules: {
                    email: {
                        required: true,
                        email: true
                    }
                },
                submitHandler: function(form) {
                    $.ajax({
                        url: "/shopxx/product_notify/save.jhtml",
                        type: "POST",
                        data: {productId: productId, email: $productNotifyEmail.val()},
                        dataType: "json",
                        cache: false,
                        beforeSend: function() {
                            $addProductNotify.prop("disabled", true);
                        },
                        success: function(data) {
                            if (data.message.type == "success") {
                                $addProductNotify.val("到货通知我");
                                $productNotify.hide();
                            }
                            $.message(data.message);
                        },
                        complete: function() {
                            $addProductNotify.prop("disabled", false);
                        }
                    });
                }
            });

            // 购买数量
            $quantity.keypress(function(event) {
                return (event.which >= 48 && event.which <= 57) || event.which == 8;
            });

            // 增加购买数量
            $increase.click(function() {
                var quantity = $quantity.val();
                if (/^\d*[1-9]\d*$/.test(quantity)) {
                    $quantity.val(parseInt(quantity) + 1);
                } else {
                    $quantity.val(1);
                }
            });

            // 减少购买数量
            $decrease.click(function() {
                var quantity = $quantity.val();
                if (/^\d*[1-9]\d*$/.test(quantity) && parseInt(quantity) > 1) {
                    $quantity.val(parseInt(quantity) - 1);
                } else {
                    $quantity.val(1);
                }
            });

            // 加入购物车
            $addCart.click(function() {
                if (productId == null) {
                    $specificationTips.fadeIn(150).fadeOut(150).fadeIn(150);
                    return false;
                }
                var quantity = $quantity.val();
                if (!/^\d*[1-9]\d*$/.test(quantity)) {
                    $.message("warn", "购买数量必须为正整数");
                    return false;
                }

                $.ajax({
                    url: "${ctx}/cart/add",
                    type: "POST",
                    data: {productId: productId, quantity: quantity},
                    dataType: "json",
                    cache: false,
                    success: function(message) {
                        if(message.resultCode == 0) { // 失败
                            $.message("warn", message.resultMessage);
                            return false;
                        }
                        if(message.resultCode == -1) { // 未登录
                            $.message("warn", message.resultMessage);
                            setTimeout(function() {
                                toRegisterLogin("login");
                            }, 1000);
                            return false;
                        }
                        // 动画效果
                        var $image = $zoom.find("img");
                        var cartOffset = $headerCart.offset();
                        var imageOffset = $image.offset();
                        $image.clone().css({
                            width: 300,
                            height: 300,
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

                        var $totalQuanityEm = $("#headerCart").find('em');
                        var totalQuanity = parseInt($totalQuanityEm.html()); // 获取显示的数字
                        $totalQuanityEm.html(totalQuanity + parseInt(quantity)); // 重新赋值
                        $.message('success', message.result);
                    }
                });
            });

            // 添加商品收藏
            $addFavorite.click(function() {
                $.ajax({
                    url: "/shopxx/member/favorite/add.jhtml",
                    type: "POST",
                    data: {goodsId: 2},
                    dataType: "json",
                    cache: false,
                    success: function(message) {
                        $.message(message);
                    }
                });
                return false;
            });

            $bar.width(barWidth);

            $window.scroll(function() {
                var scrollTop = $(this).scrollTop();
                if (scrollTop > barTop) {
                    $bar.addClass("fixed");
                    var introductionTop = $introduction.size() > 0 ? $introduction.offset().top - 36 : null;
                    var parameterTop = $parameter.size() > 0 ? $parameter.offset().top - 36 : null;
                    var reviewTop = $review.size() > 0 ? $review.offset().top - 36 : null;
                    var consultationTop = $consultation.size() > 0 ? $consultation.offset().top - 36 : null;
                    if (consultationTop != null && scrollTop >= consultationTop) {
                        $bar.find("li").removeClass("current");
                        $consultationTab.addClass("current");
                    } else if (reviewTop != null && scrollTop >= reviewTop) {
                        $bar.find("li").removeClass("current");
                        $reviewTab.addClass("current");
                    } else if (parameterTop != null && scrollTop >= parameterTop) {
                        $bar.find("li").removeClass("current");
                        $parameterTab.addClass("current");
                    } else if (introductionTop != null && scrollTop >= introductionTop) {
                        $bar.find("li").removeClass("current");
                        $introductionTab.addClass("current");
                    }
                } else {
                    $bar.removeClass("fixed").find("li").removeClass("current");
                }
            });

            // 发表商品评论
            $addReview.click(function() {
                if ($.checkLogin()) {
                    return true;
                } else {
                    $.redirectLogin("/shopxx/review/add/2.jhtml", "必须登录后才能发表商品评论");
                    return false;
                }
            });

            // 发表商品咨询
            $addConsultation.click(function() {
                if ($.checkLogin()) {
                    return true;
                } else {
                    $.redirectLogin("/shopxx/consultation/add/2.jhtml", "必须登录后才能发表商品咨询");
                    return false;
                }
            });

            // 浏览记录
            var historyGoods = getCookie("historyGoods");
            var historyGoodsIds = historyGoods != null ? historyGoods.split(",") : [];
            for (var i = 0; i < historyGoodsIds.length; i ++) {
                if (historyGoodsIds[i] == 2) {
                    historyGoodsIds.splice(i, 1);
                    break;
                }
            }
            historyGoodsIds.unshift(2);
            if (historyGoodsIds.length > 6) {
                historyGoodsIds.pop();
            }
            addCookie("historyGoods", historyGoodsIds.join(","));
            $.ajax({
                url: "/shopxx/goods/history.jhtml",
                type: "GET",
                data: {goodsIds: historyGoodsIds},
                dataType: "json",
                cache: true,
                success: function(data) {
                    $.each(data, function (i, item) {
                        var thumbnail = item.thumbnail != null ? item.thumbnail : "/shopxx/upload/image/default_thumbnail.jpg";
                        $historyGoods.find("dt").after(
                                '<dd> <img src="' + escapeHtml(thumbnail) + '" \/> <a href="' + escapeHtml(item.url) + '" title="' + escapeHtml(item.name) + '">' + escapeHtml(abbreviate(item.name, 30)) + '<\/a> <strong>' + currency(item.price, true) + '<\/strong> <\/dd>'				);
                    });
                }
            });

            // 清空浏览记录
            $clearHistoryGoods.click(function() {
                $historyGoods.remove();
                removeCookie("historyGoods");
            });

            // 点击数
            $.ajax({
                url: "${ctx}/goods/hits/${goods.id}",
                type: "GET",
                cache: true
            });

        });
    </script>
</head>
<body>
<div id="dialogOverlay" class="dialogOverlay"></div>
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
[#include "includer/header.ftl" /]
<div class="container goodsContent">
    <div class="row">
        <div class="span2">
            [#include "includer/hot_categories.ftl"]
            [#include "includer/hot_brands.ftl"]
            [#include "includer/hot_goods.ftl"]
            [#include "includer/hot_promotions.ftl"]
        </div>
        <div class="span10">
            <div class="breadcrumb">
                <ul>
                    <li>
                        <a href="${ctx}/index">首页</a>
                    </li>
                    [@product_category_parent_list productCategoryId=productCategory.id ]
                        [#if parentCategories?has_content ]
                            [#list parentCategories as productCategory ]
                                <li><a href="${ctx}/goods/list/${productCategory.id}">${productCategory.name}</a></li>
                            [/#list]
                        [/#if]
                    [/@product_category_parent_list]
                    <li>${productCategory.name}</li>
                </ul>
            </div>
            <div class="productImage">

                <a href="" id="zoom" rel="gallery">
                    <img class="medium" src="${productImages[0].medium}" />
                </a>
                <a href="javascript:;" class="prev">&nbsp;</a>
                [#--图片放大的小图--]
                <div id="thumbnailScrollable" class="scrollable">
                    <div class="items">
                    [#list productImages as productImage]
                        <a class="[#if productImage_index==0 ]current[/#if]" href="javascript:;" rel="{gallery: 'gallery', smallimage: '${productImage.thumbnail}', largeimage: '${productImage.large}'}">
                            <img src="${productImage.thumbnail}" title="${productImage.title}" />
                        </a>
                    [/#list]
                    </div>
                </div>
                <a href="javascript:;" class="next">&nbsp;</a>
            </div>
            [#--点击图片发大后的DIV--]
            <div id="preview" class="preview">
                <a href="javascript:;" class="close">&nbsp;</a>
                <a href="javascript:;" class="prev">&nbsp;</a>
                <div id="previewScrollable" class="scrollable">
                    <div class="items">
                    [#list productImages as productImage]
                        <img src="${ctx}/upload/image/blank.gif" data-original="${productImage.large}" title="${productImage.title}" />
                    [/#list]
                    </div>
                </div>
                <a href="javascript:;" class="next">&nbsp;</a>
            </div>
            <div class="info">
                <h1>
                    ${good.name}
                    <em>${good.caption}</em>
                </h1>
                <dl>
                    <dt>编号:</dt>
                    <dd>
                    ${good.sn}
                    </dd>
                </dl>

            [#if good.type > 0 ]
                <dl>
                    <dt>商品类别:</dt>
                    <dd>
                        [#if good.type == 1 ]
                            兑换商品
                        [#elseif good.type == 2 ]
                            赠品
                        [/#if]
                    </dd>
                </dl>
            [/#if]

            [#if good.type == 0][#--普通商品--]
                <dl>
                    <dt>销售价:</dt>
                    <dd>
                        <strong id="price">￥${good.price}</strong>
                    </dd>
                    <dd>
                        <span>
                            (<em>市场价:</em>
                            <del id="marketPrice">￥${good.marketPrice}</del>)
                        </span>
                    </dd>
                </dl>

                [#if defaultProduct.rewardPoint > 0][#--如果有赠送积分--]
                    <dl>
                        <dt>赠送积分:</dt>
                        <dd id="rewardPoint">
                            ${defaultProduct.rewardPoint}
                        </dd>
                    </dl>
                [/#if]
            [#else]
                [#if good.type == 1][#--积分商品--]
                    <dl>
                        <dt>兑换积分:</dt>
                        <dd>
                            <strong id="exchangePoint">${defaultProduct.exchangePoint}</strong>
                        </dd>
                    </dl>
                [/#if]
                <dl>
                    <dt>市场价:</dt>
                    <dd id="marketPrice">
                        ${defaultProduct.marketPrice}
                    </dd>
                </dl>
            [/#if]

            </div>
            <div class="action">
            [#if specificationValues?has_content ]
                <div id="specification" class="specification clearfix">
                    <div class="title">请选择商品规格</div>
                    [#list specificationValues as specificationValue ]
                    [#assign productSpecificationValue=defaultProduct.specificationValueList[specificationValue_index]]
                    <dl>
                        <dt>
                            <span title="${specificationValue.name}">${specificationValue.name}:</span>
                        </dt>
                        [#list specificationValue.entries as entry]
                        <dd>
                            [#if entry.isSelected]
                            <a href="javascript:;" val="${entry.id}" [#if productSpecificationValue.id=entry.id ]class="selected"[/#if] >
                                ${entry.value}<span title="点击取消选择">&nbsp;</span>
                            </a>
                            [/#if]
                        </dd>
                        [/#list]
                    </dl>
                    [/#list]


                </div>
            [/#if]

                <form id="productNotifyForm" action="${ctx}/product_notify/save" method="post">
                    <dl id="productNotify" class="productNotify hidden">
                        <dt>您的E-mail:</dt>
                        <dd>
                            <input type="text" name="email" maxlength="200" />
                        </dd>
                    </dl>
                </form>
                <dl class="quantity">
                    <dt>数量:</dt>
                    <dd>
                        <input type="text" id="quantity" name="quantity" value="1" maxlength="4" onpaste="return false;" />
                        <div>
                            <span id="increase" class="increase">&nbsp;</span>
                            <span id="decrease" class="decrease">&nbsp;</span>
                        </div>
                    </dd>
                    <dd>
                    ${good.unit}
                    </dd>
                </dl>
                <div class="buy">
                    <input type="button" id="addProductNotify" class="addProductNotify hidden" value="到货通知我" />
                    <input type="button" id="addCart" class="addCart" value="加入购物车" />
                    <a href="javascript:;" id="addFavorite">收藏</a>
                </div>
            </div>
            <div class="share">
                <div id="bdshare" class="bdshare_t bds_tools get-codes-bdshare">
                    <a class="bds_qzone"></a>
                    <a class="bds_tsina"></a>
                    <a class="bds_tqq"></a>
                    <a class="bds_renren"></a>
                    <a class="bds_t163"></a>
                    <span class="bds_more"></span>
                    <a class="shareCount"></a>
                </div>
            </div>
            <div id="bar" class="bar">
                <ul>
                    <li id="introductionTab">
                        <a href="#introduction">商品介绍</a>
                    </li>
                    <li id="parameterTab">
                        <a href="#parameter">商品参数</a>
                    </li>
                    <li id="reviewTab">
                        <a href="#review">商品评论</a>
                    </li>
                    <li id="consultationTab">
                        <a href="#consultation">商品咨询</a>
                    </li>
                </ul>
            </div>
            <div id="introduction" name="introduction" class="introduction">
                <div class="title">
                    <strong>商品介绍</strong>
                </div>
                <div>
                    ${good.introduction}
                </div>
            </div>
            <div id="parameter" name="parameter" class="parameter">
                <div class="title">
                    <strong>商品参数</strong>
                </div>
                <table>
                    [#list parameterValues as parameterValue]
                        <tr>
                            <th class="group" colspan="2">${parameterValue.group}</th>
                        </tr>
                        [#list parameterValue.entries as entry]
                        <tr>
                            <th>${entry.name}</th>
                            <td>${entry.value}</td>
                        </tr>
                        [/#list]
                    [/#list]

                </table>
            </div>
            <div id="review" name="review" class="review">
                <div class="title">商品评论</div>
                <div class="content clearfix">
                    <p>
                        暂无商品评论信息 <a href="/review/add/1" id="addReview">[发表商品评论]</a>
                    </p>
                </div>
            </div>
            <div id="consultation" name="consultation" class="consultation">
                <div class="title">商品咨询</div>
                <div class="content">
                    <p>
                        暂无商品咨询信息 <a href="/consultation/add/1" id="addConsultation">[发表商品咨询]</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

[#include "includer/footer.ftl"]

<script type="text/javascript" id="bdshare_js" data="type=tools&amp;uid=0"></script>
<script type="text/javascript" id="bdshell_js"></script>
<script type="text/javascript">
    document.getElementById("bdshell_js").src = "http://bdimg.share.baidu.com/static/js/shell_v2.js?cdnversion=" + Math.ceil(new Date() / 3600000)
</script>
</body>
</html>