package com.zhijin.mlearning.support.util;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import android.util.Log;
import android.widget.Toast;

import com.zhijin.mlearning.app.CoreApp;

public class MyToast
{

	static String text;

	public static void showText(String text)
	{
		Toast.makeText(CoreApp.getApp().getApplicationContext(), text, Toast.LENGTH_SHORT).show();
	}

	// 显示错误信息方法
	// --显示用户输入时验证错误的信息
	// --显示提交服务端后返回的错误信息
	public static void showToast(Object obj)
	{
		String errors = "";
		Class cls = obj.getClass();
		try {
			Method method = cls.getDeclaredMethod("getErrors");
			Map map = (Map) method.invoke(obj);// error属性存放的是所有的错误信息
			if (!map.isEmpty()) {
				Iterator it = map.entrySet().iterator();
				// 遍历错误信息
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					System.out.println("errorValue=============" + value);
					errors += value.toString();
				}
				// 将错误信息显示
				errors = (String) errors.subSequence(0, errors.length());
				Toast.makeText(CoreApp.getApp().getApplicationContext(), errors, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e("info", "error:" + e.toString());
		}
	}

	// 显示错误信息方法
	// --显示用户输入时验证错误的信息
	// --显示提交服务端后返回的错误信息
	public static void showToast(Map map)
	{
		String errors = "";
		try {
			if (!map.isEmpty()) {
				Iterator it = map.entrySet().iterator();
				// 遍历错误信息
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					errors += value.toString();
				}
				// 将错误信息显示
				errors = (String) errors.subSequence(0, errors.length() - 1);
				Toast.makeText(CoreApp.getApp().getApplicationContext(), errors, Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Log.e("info", "error:" + e.getMessage());
		}
	}

}
