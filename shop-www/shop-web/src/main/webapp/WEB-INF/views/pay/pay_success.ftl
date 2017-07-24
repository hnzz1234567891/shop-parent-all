<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>订单完成</title>
    <link href="${ctx}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/css/order.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>

</head>
<body>
[#include "includer/header.ftl"]
<div class="container checkout">
    <div class="row">
        <div class="span12">
            <div class="step">
                <ul>
                    <li>查看购物车</li>
                    <li>订单结算</li>
                    <li class="current">订单完成</li>
                </ul>
            </div>
            <div>
            [#if resultCode == 0]
            ${message}
            [#else]
                支付成功，订单号为：${orderNo}, 金额：${money}
            [/#if]
            </div>
        </div>
    </div>
</div>
[#include "includer/footer.ftl"]
</body>
</html>