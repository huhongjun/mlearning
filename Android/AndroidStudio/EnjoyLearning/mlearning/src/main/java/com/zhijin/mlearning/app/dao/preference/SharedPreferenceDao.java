package com.zhijin.mlearning.app.dao.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Shared数据持久化工具
 * 
 */
public class SharedPreferenceDao
{
	private static final String shardPreferenceName = "mlearning";
	
	public static void saveLocalData(Context context, String keyName, String value)
	{
		SharedPreferences sp = context.getSharedPreferences(shardPreferenceName, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(keyName, value);
		editor.commit();
	}

	public static String getLocalData(Context context, String keyName)
	{
		SharedPreferences sp = context.getSharedPreferences(shardPreferenceName, Context.MODE_PRIVATE);
		// 获得数据库中保存的登录名与密码
		String loginName = sp.getString(keyName, "请输入登录邮箱");
		return loginName;
	}

}
