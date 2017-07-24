package com.shop.core.constant;

import com.shop.core.model.ProductCategory;

/**
 * Created by TW on 2017/6/13.
 */
public enum ProductCategoryGrade {
    ROOT(0),
    FIRST(1),
    SECOND(2),
    THIRD(3);

    private int grade;

    ProductCategoryGrade() {

    }
    ProductCategoryGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }
}
