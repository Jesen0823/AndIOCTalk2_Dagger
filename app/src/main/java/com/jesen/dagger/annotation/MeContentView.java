package com.jesen.dagger.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 作用到类上
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MeContentView {

    int value() default -1;

}
