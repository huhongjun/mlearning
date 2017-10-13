package com.zj.learning.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Base64;

import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.model.course.VideoRecord;
import com.zj.learning.model.user.User;

public class CoreSharedPreference {

	/**
	 * 保存用户是否查看过用户帮助界面
	 */
	public static void saveUserHelpTag(Context context, boolean islogin) {
		SharedPreferences sp = context.getSharedPreferences(
				GlobalConfig.SharedPreferenceDao.FILENAME_USER,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean(GlobalConfig.SharedPreferenceDao.KEY_USERHELP,
				islogin);
		editor.commit();
	}

	/**
	 * 获取用户是否查看过用户帮助界面
	 */
	public static boolean getUserHelpTag(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				GlobalConfig.SharedPreferenceDao.FILENAME_USER,
				Context.MODE_PRIVATE);
		// 获得数据库中保存的登录名与密码
		boolean isLogin = sp.getBoolean(
				GlobalConfig.SharedPreferenceDao.KEY_USERHELP, false);
		return isLogin;
	}

	/**
	 * 验证通过后将登录用户信息（密码不保存）保存至本地
	 * 
	 */
	// public static void saveMember(Context context, User user) {
	// if (null == user) {
	// return;
	// }
	// SharedPreferences sp = context.getSharedPreferences(
	// GlobalConfig.SharedPreferenceDao.FILENAME_USER,
	// Context.MODE_PRIVATE);
	// Editor editor = sp.edit();
	// // editor.putString(GlobalConfig.SharedPreferenceDao.KEY_LOGIN_NAME,
	// // user.getLoginName());
	// // editor.putString(GlobalConfig.SharedPreferenceDao.KEY_USER_PIC,
	// // user.getStandby0());
	// // editor.putInt(GlobalConfig.SharedPreferenceDao.KEY_USER_INTEGRATION,
	// // user.getIntegration());
	// // editor.putInt(GlobalConfig.SharedPreferenceDao.KEY_USER_GROUP,
	// // user.getGroup());
	// editor.putString(GlobalConfig.SharedPreferenceDao.KEY_USER,
	// user.toJson());
	// editor.commit();
	// }
	//
	// /**
	// * 获得本地数据库中保存的用户信息
	// *
	// * @return 用户信息
	// */
	// public static User getUser(Context context) {
	// SharedPreferences sp = context.getSharedPreferences(
	// GlobalConfig.SharedPreferenceDao.FILENAME_USER,
	// Context.MODE_PRIVATE);
	// // 获得数据库中保存的登录名与密码
	// // String loginName = sp.getString(
	// // GlobalConfig.SharedPreferenceDao.KEY_LOGIN_NAME, "");
	// // // 将数据绑定至Map里并返回
	// // if (!TextUtils.isEmpty(loginName)) {
	// // User user = new User();
	// // user.setLoginName(loginName);
	// // user.setStandby0(sp.getString(
	// // GlobalConfig.SharedPreferenceDao.KEY_USER_PIC, ""));
	// // user.setGroup(sp.getInt(
	// // GlobalConfig.SharedPreferenceDao.KEY_USER_GROUP, 0));
	// // user.setIntegration(sp.getInt(
	// // GlobalConfig.SharedPreferenceDao.KEY_USER_INTEGRATION, 0));
	// // return user;
	// // } else {
	// // return null;
	// // }
	// String userStr = sp.getString(
	// GlobalConfig.SharedPreferenceDao.KEY_USER, "");
	// if (TextUtils.isEmpty(userStr)) {
	// return null;
	// }
	// return User.toObj(userStr);
	// }
//
//	public void saveVideoRecord(Context ctx, VideoRecord record) {
//		if (null == record) {
//			return;
//		}
//		SharedPreferences sp = ctx.getSharedPreferences(
//				GlobalConfig.SharedPreferenceDao.FILENAME_VIDEO_RECORD,
//				Context.MODE_PRIVATE);
//	}

	public static void saveObject(Context ctx, String filename, String key,
			Object obj) {
		if (null == obj) {
			return;
		}
		SharedPreferences sp = ctx.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		ByteArrayOutputStream baos = null;
		try {
			baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
			String info = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT));
			editor.putString(key, info);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static <E> E getObject(Context ctx, String filename, String key) {
		SharedPreferences sp = ctx.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		String base64Str = sp.getString(key, "");
		return base64StringtoObject(base64Str);
	}

	public static <E> List<E> getAllObject(Context ctx, String filename) {
		SharedPreferences sp = ctx.getSharedPreferences(filename,
				Context.MODE_PRIVATE);
		Map<String, ?> maps = sp.getAll();
		if (null == maps) {
			return null;
		}
		List<E> results = new ArrayList<E>();
		Set<String> sets = maps.keySet();
		Iterator<String> it = sets.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String base64Str = sp.getString(key, "");
			if (TextUtils.isEmpty(base64Str)) {
				continue;
			}
			E e = base64StringtoObject(base64Str);
			if (null != e) {
				results.add(e);
			}
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private static <E> E base64StringtoObject(String base64Str) {
		if (TextUtils.isEmpty(base64Str)) {
			return null;
		}
		ByteArrayInputStream bais = null;
		try {
			byte[] base64Bytes = Base64.decode(base64Str.getBytes(),
					Base64.DEFAULT);
			bais = new ByteArrayInputStream(base64Bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			E e = (E) ois.readObject();
			return e;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != bais) {
					bais.close();
				}
			} catch (Exception e) {

			}
		}
		return null;
	}
}
