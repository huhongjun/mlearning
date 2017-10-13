package com.zhijin.mlearning.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Lazy {
	boolean isLazy();
}
