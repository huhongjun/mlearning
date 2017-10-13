package com.zj.app;

import java.util.HashMap;
import java.util.Map;

import android.text.TextUtils;

/**
 * 请求控制类，主要是控制多次自动刷新，提高效率（遵循合成/聚合设计模式、遵循接口分离原则）
 * 
 * @author XingRongJing
 * 
 */
public class RefreshControlHelper {

	/** key-可是类名或请求符，能唯一代表即可 value：是否自动刷新 **/
	private Map<String, Boolean> mReqControlMaps = new HashMap<String, Boolean>();

	public void addReqControl(String key, boolean value) {
		if (TextUtils.isEmpty(key) || null == mReqControlMaps) {
			return;
		}
		mReqControlMaps.put(key, value);
	}

	public void removeReqControl(String key) {
		if (TextUtils.isEmpty(key) || null == mReqControlMaps) {
			return;
		}
		mReqControlMaps.remove(key);
	}

	/**
	 * 是否自动刷新，如果不存在此key，则自动放入，并默认为不再自动刷新
	 * 
	 * @param key
	 * @return
	 */
	public boolean isRefreshAuto(String key) {
		if (null == mReqControlMaps || TextUtils.isEmpty(key)) {
			return true;
		}
		if (mReqControlMaps.containsKey(key)) {
			return mReqControlMaps.get(key);
		}
		this.addReqControl(key, false);
		return true;
	}

	public void clear() {
		if (null != mReqControlMaps) {
			mReqControlMaps.clear();
		}
	}

	public void destroy() {
		this.clear();
		mReqControlMaps = null;
	}

}
