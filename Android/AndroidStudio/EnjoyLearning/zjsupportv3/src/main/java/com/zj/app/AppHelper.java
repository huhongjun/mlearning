package com.zj.app;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.zj.support.cache.image.ImageCacheHelper;
import com.zj.support.http.ZjVolley;
import com.zj.support.http.api.HttpApi;
import com.zj.support.http.upload.UploadApi;

public class AppHelper {

	public static boolean mIsLog = true;
	private static final int CORE_THRED_POOL_SIZE = 5;
	private static AppHelper mAppHelper;
	private Context mCtx;
	private HttpApi mHttpApi;
	private UploadApi mUploadApi;
	private ImageCacheHelper mImageCacheHelper;
	private ExecutorService mExecutors;
	private Handler mHandler;

	private AppHelper(Context ctx) {
		if (null == ctx) {
			throw new IllegalArgumentException("Context can not be null");
		}
		this.mCtx = ctx.getApplicationContext();
	}

	public static final AppHelper getAppHelper(Context ctx) {
		if (null == mAppHelper) {
			mAppHelper = new AppHelper(ctx);
		}
		return mAppHelper;
	}

	public ImageCacheHelper getImageCacheHelper() {
		if (null == mImageCacheHelper) {
			mImageCacheHelper = new ImageCacheHelper(ZjVolley.newRequestQueue(
					mCtx, ZjVolley.HttpCacheType.IMAGE_CACHE_TYPE));
		}
		return mImageCacheHelper;
	}

	public HttpApi getHttpApi() {
		if (null == mHttpApi) {
			mHttpApi = new HttpApi(ZjVolley.newRequestQueue(mCtx,
					ZjVolley.HttpCacheType.HTTP_CACHE_TYPE));
		}
		return mHttpApi;
	}

	public ExecutorService getExecutors() {
		if (null == mExecutors) {
			mExecutors = Executors.newFixedThreadPool(CORE_THRED_POOL_SIZE);
		}
		return mExecutors;
	}

	public Handler getHandler() {
		if (null == mHandler) {
			mHandler = new Handler();
		}
		return mHandler;
	}

	public UploadApi getUploadApi() {
		if (null == mUploadApi) {
			mUploadApi = new UploadApi();
		}
		return mUploadApi;
	}

	public boolean isNetworkEnable() {
		ConnectivityManager connectivity = (ConnectivityManager) this.mCtx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public boolean isWifiConnected() {
		ConnectivityManager connectivity = (ConnectivityManager) this.mCtx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] infos = connectivity.getAllNetworkInfo();
			if (infos != null) {
				for (int i = 0; i < infos.length; i++) {
					NetworkInfo info = infos[i];
					// 当网络的状态为连接时
					if (info.getState() == NetworkInfo.State.CONNECTED
							&& ConnectivityManager.TYPE_WIFI == info.getType()) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
