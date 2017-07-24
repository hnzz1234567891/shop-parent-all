package com.shop.core.constant;

import com.shop.core.model.Goods;

/**
 * Created by TW on 2017/6/10.
 */
public enum GoodsOrders {

    create_date("create_date.desc", "日期降序"),
    price_asc("price.asc", "价格升序"),
    price_desc("price.desc", "价格降序"),
    sales("sales.desc", "销量降序"),
    is_top("is_top.desc", "置顶降序"),
    score("score.desc", "评分降序");

    private String sort; // create_date.desc-->日期降序， is_top.desc 置顶降序
    private String showSort;

    private GoodsOrders(String sort, String showSort) {
        this.sort = sort;
        this.showSort = showSort;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getShowSort() {
        return showSort;
    }

    public void setShowSort(String showSort) {
        this.showSort = showSort;
    }

    public static GoodsOrders findBySort(String sort) {
        for (GoodsOrders goodsOrders: GoodsOrders.values()) {
            if(goodsOrders.getSort().equals(sort)) {
                return  goodsOrders;
            }
        }
        return  null;
    }
}
