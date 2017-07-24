package com.shop.core.vo;

/**
 * Created by TW on 2017/6/14.
 */
public class GoodsDetailVo extends GoodsVo {
//    SELECT t.id, t.`name`, t.market_price, t.price,
//    t.caption, t.parameter_values, t.product_images,
//    t.product_category, t.specification_items, t.sn,
//    t.introduction, t.image, t.type, t.unit FROM xx_goods t;

    private String parameterValues;
    private String productImages;
    private Integer productCategory;
    private String specificationItems;
    private String sn;
    private String introduction;
    /** 类型 0=普通商品 1=积分兑换 2=赠品*/
    private Integer type;
    private String unit;

    public String getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(String parameterValues) {
        this.parameterValues = parameterValues;
    }

    public String getProductImages() {
        return productImages;
    }

    public void setProductImages(String productImages) {
        this.productImages = productImages;
    }

    public Integer getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(Integer productCategory) {
        this.productCategory = productCategory;
    }

    public String getSpecificationItems() {
        return specificationItems;
    }

    public void setSpecificationItems(String specificationItems) {
        this.specificationItems = specificationItems;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
