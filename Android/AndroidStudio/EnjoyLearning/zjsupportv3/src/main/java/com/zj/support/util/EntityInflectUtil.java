package com.zj.support.util;

import java.lang.reflect.Field;

import android.util.Log;

public abstract class EntityInflectUtil {

	private static final String TAG = EntityInflectUtil.class.getName();

	/**
	 * 获取某个对象某个域的值
	 * 
	 * @param owner
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldVlaue(Object owner, String fieldName) {
		Object obj = null;
		Field field = null;
		try {
			field = owner.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			obj = field.get(owner);
		} catch (Exception e) {
//			Log.e(TAG, "shan-->" + e.toString() + " fieldName = " + fieldName);
			return null;
		}
		return obj;
	}

	/**
	 * 将指定的值插入指定对象的指定域中
	 * 
	 * @param value
	 * @param owner
	 * @param fieldName
	 * @return
	 */
	public static boolean setFieldVlaue(Object value, Object owner,
			String fieldName) {
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
