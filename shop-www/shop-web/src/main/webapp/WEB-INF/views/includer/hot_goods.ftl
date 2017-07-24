[@goods_list count=3 tagId=3 productCategoryId=productCategoryId ]
    [#if goods?has_content]
    <div class="hotGoods">
        <dl>
            <dt>热销商品</dt>
            [#list goods as goods]
                <dd>
                    <a href="${ctx}/goods/content/${goods.id}">
                        <img src="${goods.image}" alt="${goods.name}" />
                        <span title="${goods.name}">${goods.name}</span>
                    </a>
                    <strong>
                        ￥${goods.price}
                        <del>￥${goods.marketPrice}</del>
                    </strong>
                </dd>
            [/#list]
        </dl>
    </div>
    [/#if]
[/@goods_list]