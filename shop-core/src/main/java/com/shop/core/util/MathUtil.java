package com.shop.core.util;

import java.math.BigDecimal;

/**
 * Created by TW on 2017/6/19.
 */
public class MathUtil {

    public static BigDecimal setScale(BigDecimal value) {
        return value.setScale(2, BigDecimal.ROUND_HALF_UP);
    }
}
