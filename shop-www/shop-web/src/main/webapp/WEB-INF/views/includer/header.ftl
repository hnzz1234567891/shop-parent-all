<div class="header">
	<div class="top">
		<div class="topNav">
			<ul class="left">
				<li>
					<span>您好，欢迎来到商HAI购</span>
				</li>
				[#if LOGIN_USER?exists ]
					<span id="headerName" class="headerName" style="display: inline;">${LOGIN_USER.username}</span>
				[/#if]
				[#if LOGIN_USER?has_content]
                    <li id="headerLogout" class="headerLogout" style="display: inline;">
                        <a href="javascript:logout()">[退出]</a>
                    </li>
				[#else ]
					<li id="headerLogin" class="headerLogin" style="display: block">
						<a href="javascript:toRegisterLogin('login');">登录</a>|
					</li>
					<li id="headerRegister" class="headerRegister" style="display: block">
						<a href="javascript:toRegisterLogin('register');">注册</a>
					</li>
				[/#if]
			</ul>
			<ul class="right">
				[@navigation_list position=0 ]
					[#list navigations as navigation ]
						<li>
							<a href="${ctx}${navigation.url}" [#if navigation.isBlankTargert == 1 ]target="_blank"[/#if]>
								${navigation.name}
							</a>|
						</li>
					[/#list]
				[/@navigation_list]
						
				<li id="headerCart" class="headerCart">
					<a href="${ctx}/cart/list">购物车</a>
					(<em>0</em>)
				</li>
			</ul>
		</div>
	</div>
	<div class="container">
		<div class="row">
			<div class="span3">
				<a href="${ctx}/index">
					<img src="${ctx}/upload/image/logo.gif" alt="尚HAI购" />
				</a>
			</div>
			<div class="span6">
				<div class="search">
					<form id="goodsSearchForm" action="[#if productCategoryId?has_content ]${ctx}/goods/list/${productCategoryId}[#else ]${ctx}/goods/search[/#if]" method="get">
						<input name="keyword" class="keyword" value="${goodsDto.keyword}" autocomplete="off" x-webkit-speech="x-webkit-speech" x-webkit-grammar="builtin:search" maxlength="30" />
						<button type="submit">&nbsp;</button>
					</form>
				</div>
				<div class="hotSearch">
					热门搜索:
					[@hot_search_keywords]
						[#list keywords as keyword ]
							[#if productCategoryId?has_content ]
								<a href="${ctx}/goods/list/${productCategoryId}?keyword=${keyword}">${keyword}</a>
							[#else ]
                                <a href="${ctx}/goods/search?keyword=${keyword}">${keyword}</a>
							[/#if]
						[/#list]
					[/@hot_search_keywords]
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
					[@navigation_list position=1 ]
						[#list navigations as navigation ]
							<dd>
								<a href="${ctx}${navigation.url}"  [#if navigation.isBlankTargert == 1 ]target="_blank"[/#if]>
									${navigation.name}
								</a>
							</dd>
						[/#list]
					[/@navigation_list]
				</dl>
			</div>
		</div>
	</div>
</div>
<script>
	function toRegisterLogin(url) {
		// 获取到当前的路径
        var currentUrl = window.location.href;
        if (currentUrl.indexOf("/login") > -1 || currentUrl.indexOf("/register") > -1 ) {
            redirectUrl = '';
        }
        // 跳转 encodeURIComponent转码
        window.location.href = "${ctx}/"+ url +"?redirectUrl=" + encodeURIComponent(currentUrl);
    }

    // 退出
    function logout() {
		$.post("${ctx}/logout", {}, function (resp) {
			if (resp.resultCode == 1) {
			    window.location.reload();
			} else {
			    alert(resp.resultMessage);
			}
        });
	}

	[#if LOGIN_USER?exists]
		$.post("${ctx}/cart/count", {}, function (data) {
			if (data.resultCode == 1) {
				var count = data.result;
				$("#headerCart").find('em').html(count);
			}
		});
	[/#if]
</script>