package com.shop.core.annotation;

import java.lang.annotation.*;

/**
 * Created by TW on 2017/6/17.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IsLogin {

}
