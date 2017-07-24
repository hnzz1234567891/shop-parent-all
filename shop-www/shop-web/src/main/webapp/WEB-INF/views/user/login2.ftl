<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>会员登录</title>
    <link href="/css/common.css" rel="stylesheet" type="text/css" />
    <link href="/css/login.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/js/jquery.js"></script>
    <script type="text/javascript" src="/js/jquery.validate.js"></script>
    <script type="text/javascript" src="/js/common.js"></script>
    <script type="text/javascript">
        $().ready(function() {

            var $loginForm = $("#loginForm");
            var $username = $("#username");
            var $password = $("#password");
            var $captcha = $("#captcha");
            var $captchaImage = $("#captchaImage");
            var $isRememberUsername = $("#isRememberUsername");
            var $submit = $("input:submit");

            // 记住用户名
            if (getCookie("memberUsername") != null) {
                $isRememberUsername.prop("checked", true);
                $username.val(getCookie("memberUsername"));
                $password.focus();
            } else {
                $isRememberUsername.prop("checked", false);
                $username.focus();
            }

            // 更换验证码
            $captchaImage.click(function() {
                $captchaImage.hide()
                        .attr("src", "/kaptcha/getKaptchaImage?timestamp=" + new Date().getTime()).fadeIn();
                //$captchaImage.attr("src", "/kaptcha/getKaptchaImage?timestamp=" + new Date().getTime());
            });

            $submit.click(function() {
                // 基本的参数验证
                var username = $username.val();
                if (username == null || username == '') {
                    alert("请输入用户名");
                    return;
                }
                var password = $password.val();
                if (password == null || password == '') {
                    alert("请输入密码");
                    return;
                }
                var verifyCode = $captcha.val();
                if (verifyCode == null || verifyCode == '') {
                    alert("请输入验证码");
                    return;
                }
                $.ajax({
                    url: "/user/login",
                    type: "post",
                    dataType: "json",
                    data : {userName:username, password: password, verifyCode: verifyCode},
                    cache: false,
                    beforeSend: function() {
                        $submit.attr("disabled", true);
                    },
                    success: function(data) {
                        if (data.resultCode == 0) {
                            alert(data.resultMessage);
                            $submit.attr("disabled", false);
                            $captchaImage.attr("src", "/kaptcha/getKaptchaImage?timestamp=" + new Date().getTime());
                        } else { // 登录成功就跳转
                            if ($isRememberUsername.prop("checked")) {
                                addCookie("memberUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
                            } else {
                                removeCookie("memberUsername");
                            }

                            var redirectUrl = "";
                            if (redirectUrl == null || redirectUrl == '') {
                                redirectUrl = "/index";
                            }
                            window.location.href = redirectUrl;
                        }
                    }
                });
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
                    <a href="/login">登录</a>|
                </li>
                <li id="headerRegister" class="headerRegister">
                    <a href="/register">注册</a>
                </li>
            </ul>
            <ul class="right">
                <li>
                    <a href="/member/index.jhtml" >
                        会员中心
                    </a>|
                </li>
                <li>
                    <a href="/article/list/3" >
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
                    <a href="/goods/search?keyword=华硕">华硕</a>
                    <a href="/goods/search?keyword=美的">美的</a>
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
                        <a href="" >
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
</script></div>
<div class="container login">
    <div class="row">
        <div class="span6">
            <img src="http://image.demo.shopxx.net/4.0/201501/b601918c-e775-4453-8abd-25f453bf5901.jpg" width="500" height="300" alt="服务宣传" />
        </div>
        <div class="span6">
            <div class="wrap">
                <div class="main">
                    <div class="title">
                        <strong>会员登录</strong>USER LOGIN
                    </div>
                    <form id="loginForm" method="post">
                        <table>
                            <tr>
                                <th>
                                    用户名/E-mail:
                                </th>
                                <td>
                                    <input type="text" id="username" name="username" class="text" maxlength="200" />
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    密 码:
                                </th>
                                <td>
                                    <input type="password" id="password" name="password" class="text" maxlength="200" autocomplete="off" />
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    验证码:
                                </th>
                                <td>
											<span class="fieldSet">
												<input type="text" id="captcha" name="verifyCode" class="text captcha" maxlength="4" autocomplete="off" />
												<img id="captchaImage" class="captchaImage" src="/kaptcha/getKaptchaImage" title="点击更换验证码" style="width:80px;height:30px;" />
											</span>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    &nbsp;
                                </th>
                                <td>
                                    <label>
                                        <input type="checkbox" id="isRememberUsername" name="isRememberUsername" value="true" />记住用户名
                                    </label>
                                    <label>
                                        &nbsp;&nbsp;<a href="/user/find_password">找回密码</a>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <th>
                                    &nbsp;
                                </th>
                                <td>
                                    <input type="submit" class="submit" value="登 录" />
                                </td>
                            </tr>
                            <tr class="register">
                                <th>
                                    &nbsp;
                                </th>
                                <td>
                                    <dl>
                                        <dt>还没有注册账号？</dt>
                                        <dd>
                                            立即注册即可体验在线购物！
                                            <a href="/register">立即注册</a>
                                        </dd>
                                    </dl>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
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
            <@navigation_list position = 3>
                <#list navigations as navigation>
                    <li>
                        <a href=""<#if navigation.isBlankTarget> target="_blank"</#if>></a>
                        <#if navigation_has_next>|</#if>
                    </li>
                </#list>
            </@navigation_list>
            </ul>
        </div>
        <div class="info">
            <p>湘ICP备10000000号</p>
            <p>Copyright © 2005-2015 尚HAI购 版权所有</p>
        <@friend_link_list count = 10>
            <ul>
                <#list friendLinks as friendLink>
                    <li>
                        <a href="" target="_blank">
                            <img src="" alt="" />
                        </a>
                    </li>
                </#list>
            </ul>
        </@friend_link_list>
        </div>
    </div>
</div>
</body>
</html>