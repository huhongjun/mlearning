package com.zhijin.mlearning.support.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ClassType
{

	// 判断是否为自定义类型的方法
	public static boolean isBaseDataType(Object obj) throws Exception
	{

		Class clazz = obj.getClass();

		return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class)
				|| clazz.equals(Long.class) || clazz.equals(long.class) || clazz.equals(Double.class)
				|| clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Short.class)
				|| clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class) || clazz.equals(Boolean.class)
				|| clazz.equals(Date.class) || clazz.equals(Map.class) || clazz.equals(List.class) || (obj instanceof List)
				|| (obj instanceof Date) || (obj instanceof Map) || (obj instanceof Set) || clazz.equals(Set.class)
				|| clazz.equals(int.class) || clazz.isPrimitive());
	}

	public static boolean isBaseDataType(Class clazz) throws Exception
	{

		return (clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class)
				|| clazz.equals(Long.class) || clazz.equals(Double.class) || clazz.equals(Float.class)
				|| clazz.equals(Character.class) || clazz.equals(Short.class) || clazz.equals(BigDecimal.class)
				|| clazz.equals(BigInteger.class) || clazz.equals(Boolean.class) || clazz.equals(Date.class)
				|| clazz.equals(Map.class) || clazz.equals(List.class) || clazz.equals(Set.class) || clazz.equals(int.class) || clazz
					.isPrimitive());
	}

	public boolean isNoBaseDataType(String clazz)
	{
		return (clazz.equals("Class") || clazz.equals("int") || clazz.equals("String") || clazz.equals("Integer")
				|| clazz.equals("Byte") || clazz.equals("Long") || clazz.equals("long") || clazz.equals("Double")
				|| clazz.equals("double") || clazz.equals("Float") || clazz.equals("float") || clazz.equals("Character")
				|| clazz.equals("Short") || clazz.equals("short") || clazz.equals("BigDecimal") || clazz.equals("List")
				|| clazz.equals("BigInteger") || clazz.equals("byte") || clazz.equals("Boolean") || clazz.equals(Date.class)
				|| clazz.equals("Map") || clazz.equals("Set"));
	}

	public static boolean isBasyType(Object type)
	{
		return type.toString().equals("class java.util.Date") || type.toString().equals("class java.lang.String")
				|| type.toString().equals("class java.lang.Integer") || type.toString().equals("int")
				|| type.toString().equals("long") || type.toString().equals("boolean")
				|| type.toString().equals("class java.math.BigDecimal") || type.toString().equals("interface java.util.Map");
	}
}
