<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <title>会员登录</title>
    <link href="${ctx}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/css/login.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/js/jquery.validate.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
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
                        .attr("src", "${ctx}/kaptcha/getKaptchaImage?timestamp=" + new Date().getTime()).fadeIn();
                //$captchaImage.attr("src", "${ctx}/kaptcha/getKaptchaImage?timestamp=" + new Date().getTime());
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
                    url: "${ctx}/user/login",
                    type: "post",
                    dataType: "json",
                    data : {userName:username, password: password, verifyCode: verifyCode},//表单序列化
                    cache: false,
                    beforeSend: function() {
                        $submit.attr("disabled", true);
                    },
                    success: function(data) {
                        if (data.resultCode == 0) {
                            alert(data.resultMessage);
                            $submit.attr("disabled", false);
                            $captchaImage.attr("src", "${ctx}/kaptcha/getKaptchaImage?timestamp=" + new Date().getTime());
                        } else { // 登录成功就跳转
                            if ($isRememberUsername.prop("checked")) {
                                addCookie("memberUsername", $username.val(), {expires: 7 * 24 * 60 * 60});
                            } else {
                                removeCookie("memberUsername");
                            }
                            var redirectUrl = "${redirectUrl}";
                            if (redirectUrl == null || redirectUrl == '') {
                                redirectUrl = "${ctx}/index";
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
[#include "includer/header.ftl"]
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
                                        <img id="captchaImage" class="captchaImage" src="${ctx}/kaptcha/getKaptchaImage" title="点击更换验证码" style="width:80px;height:30px;" />
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
                                        &nbsp;&nbsp;<a href="${ctx}/user/find_password">找回密码</a>
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
                                            <a href="${ctx}/register">立即注册</a>
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
[#include "includer/footer.ftl"]
</body>
</html>