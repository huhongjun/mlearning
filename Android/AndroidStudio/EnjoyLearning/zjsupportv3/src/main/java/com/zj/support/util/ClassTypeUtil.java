package com.zj.support.util;

public abstract class ClassTypeUtil {

	public static boolean isBaseType(Object type) {
		return type.toString().equals("class java.util.Date")
				|| type.toString().equals("class java.lang.String")
				|| type.toString().equals("class java.lang.Integer")
				|| type.toString().equals("int")
				|| type.toString().equals("long")
				|| type.toString().equals("class java.lang.Long")
				|| type.toString().equals("boolean")
				|| type.toString().equals("class java.math.BigDecimal")
				|| type.toString().equals("interface java.util.Map");
	}
}
