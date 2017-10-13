package com.zhijin.mlearning.support.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Validates {
	String type();

	String info();
}
