package com.qimeixun.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chenshouyang
 * @date 2020/4/2717:46
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LoginCheck {
    /**
     * 权限字符串(为空则不校验)
     * @return
     */
    String permission() default "";
}
