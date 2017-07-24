<!DOCTYPE html>
<html>
<head>
    <link href="${ctx}/css/common.css" rel="stylesheet" type="text/css" />
    <link href="${ctx}/css/error.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="${ctx}/js/jquery.js"></script>
    <script type="text/javascript" src="${ctx}/js/common.js"></script>
</head>
<body>
    [#include "includer/header.ftl" /]
<div class="container error">
    <div class="row">
        <div class="span12">
            <div class="main">
                <dl>
                    <dt>错误信息</dt>
                    <dd>${result.resultMessage}</dd>
                    <dd>
                        [#if result.resultCode == -1 ]
                            <a href="javascript:void(0);" ><span id="seconds">3</span>s后跳转登录</a>
                        [#else]
                            <a href="javascript:void(0);" onclick="history.back(); return false;">返回上一页</a>
                        [/#if]
                    </dd>
                    <dd>
                        <a href="${ctx}/index">&gt;&gt;返回首页</a>
                    </dd>
                </dl>
            </div>
        </div>
    </div>
</div>
    [#include "includer/footer.ftl" /]
    <script>
        [#if result.resultCode == -1 ]
            // 代表登录
            var waitTime = 3;
            countdown();

            function countdown() {
                if (waitTime == 0) {
                    toRegisterLogin('login');
                } else {
                    $("#seconds").html(waitTime);
                }
                setTimeout(function () {
                    waitTime--;
                    countdown();
                }, 1000);
            }


        [/#if]

    </script>
</body>
</html>