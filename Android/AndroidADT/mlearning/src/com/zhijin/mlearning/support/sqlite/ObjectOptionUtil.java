package com.zhijin.mlearning.support.sqlite;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.util.Log;

public class ObjectOptionUtil
{

	public void getProperty(Object entityName) throws Exception
	{
		Class c = entityName.getClass();
		Field field[] = c.getDeclaredFields();
		for (Field f : field) {
			Object v = invokeMethod(entityName, f.getName(), null);
		}
	}

	// 得到对象中get方法的�?
	public Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception
	{
		Class ownerClass = owner.getClass();
		methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		Method method = null;
		try {
			method = ownerClass.getMethod("get" + methodName);
		} catch (SecurityException e) {
		} catch (NoSuchMethodException e) {
			return " can't find 'get" + methodName + "' method";
		}
		return method.invoke(owner);
	}

	public Object invokeMethodSObject(Object owner, String fieldName)
	{
		Object obj = null;
		Class ownerClass = owner.getClass();
		Method method = null;
		Field field = null;
		try {
			method = ownerClass.getMethod("get" + fieldName);
			obj = method.invoke(owner);

		} catch (Exception e) {
			return "-1";
		}
		return obj;
	}

	public Object getFieldVlaue(Object owner, String fieldName)
	{
		Object obj = null;
		Field field = null;
		try {
			field = owner.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			obj = field.get(owner);
		} catch (Exception e) {
			Log.e("error", "Exception:" + e.toString());
			return null;
		}
		return obj;
	}

	// 把数据插入到指定的set方法�?
	public boolean setFieldVlaue(Object value, Object owner, String fieldName)
	{
		boolean flag = true;
		Field field;
		try {
			field = owner.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(owner, value);
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}
}
