<div class="hotProductCategory">
[@product_category_root_list count = 6 ]
    [#list productCategories as productCategory]
        <dl class="odd clearfix">
            <dt>
                <a href="${ctx}/goods/list/${productCategory.id}">${productCategory.name}</a>
            </dt>
            [@product_category_children_list parentId=productCategory.id ]
                [#list productCategories as productCategory]
                    <dd>
                        <a href="${ctx}/goods/list/${productCategory.id}">${productCategory.name}</a>
                    </dd>
                [/#list]
            [/@product_category_children_list]
        </dl>
    [/#list]
[/@product_category_root_list]
</div>


