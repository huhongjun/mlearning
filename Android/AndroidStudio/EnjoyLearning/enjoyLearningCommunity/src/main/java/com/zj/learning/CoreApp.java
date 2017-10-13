package com.zj.learning;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.zj.app.AppHelper;
import com.zj.app.RefreshControlHelper;
import com.zj.learning.content.CoreService;
import com.zj.learning.model.forum.Tag;
import com.zj.learning.model.user.User;
import com.zj.support.cache.image.ImageCacheHelper;
import com.zj.support.http.api.HttpApi;
import com.zj.support.http.upload.UploadApi;

/**
 * 全局App
 * 
 * @author XingRongJing
 * 
 */
public class CoreApp extends Application {

	private AppHelper mAppHelper;
	private RefreshControlHelper mRefreshControlHelper;
	private ResourceHouseKeeper mResMgr;
	private RecordKeeper mRecordKeeper;
	private User mUser;						// 当前登录用户
	private ArrayList<Tag> tags;

	public void onCreate(){
		super.onCreate();
		Stetho.initializeWithDefaults(this);
	}
	private AppHelper getAppHelper() {
		if (null == mAppHelper) {
			mAppHelper = AppHelper.getAppHelper(this);
		}
		return mAppHelper;
	}

	private RefreshControlHelper getRefreshControlHelper() {
		if (null == mRefreshControlHelper) {
			mRefreshControlHelper = new RefreshControlHelper();
		}
		return mRefreshControlHelper;
	}

	public HttpApi getHttpApi() {
		return this.getAppHelper().getHttpApi();
	}

	public UploadApi getUploadApi() {
		return this.getAppHelper().getUploadApi();
	}

	public ImageCacheHelper getImageCacheHelper() {
		return this.getAppHelper().getImageCacheHelper();
	}

	public ExecutorService getExecutorService() {
		return this.getAppHelper().getExecutors();
	}

	public boolean isNetworkEnable() {
		return this.getAppHelper().isNetworkEnable();
	}

	public void startCoreService() {
		this.startService(new Intent(this, CoreService.class));
	}

	public void stopCoreService() {
		this.stopService(new Intent(this, CoreService.class));
	}

	/**
	 * 以某个键的请求是否自动刷新，确保每个请求在一次登录状态下仅自动刷新一次
	 * 
	 * @param key
	 * @return
	 */
	public boolean isRefreshAuto(String key) {
		if (TextUtils.isEmpty(key)) {
			return true;
		}
		return this.getRefreshControlHelper().isRefreshAuto(key);
	}

	public ResourceHouseKeeper getResourceHouseKeeper() {
		if (null == mResMgr) {
			this.mResMgr = new ResourceHouseKeeper();
		}
		return this.mResMgr;
	}

	public RecordKeeper getRecordKeeper() {
		if (null == mRecordKeeper) {
			mRecordKeeper = new RecordKeeper();
		}
		return mRecordKeeper;
	}

	/**
	 * Toast是Android中用来显示显示信息的一种机制,和Dialog不一样的是没有焦点的,而且显示一定的时间就会自动消失。
	 * @param msg
	 */
	public void showToast(String msg) {
		// Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		View toastRoot = LayoutInflater.from(this.getApplicationContext()).inflate(R.layout.common_toast, null);
		TextView message = (TextView) toastRoot.findViewById(R.id.common_toast_tv_tips);
		message.setText(msg);
		Toast toastStart = new Toast(this);
		toastStart.setGravity(Gravity.CENTER, 0, 0);
		toastStart.setDuration(Toast.LENGTH_SHORT);
		toastStart.setView(toastRoot);
		toastStart.show();
	}

	public boolean isLogin() {
		return (null != this.getUser());
	}

	public User getUser() {
		return this.mUser;
	}

	public void setUser(User user) {
		this.mUser = user;
	}

	public ArrayList<Tag> getTags() {
		return this.tags;
	}

	public void setTags(ArrayList<Tag> tags) {
		this.tags = tags;
	}
}
