package com.zj.learning.control.common.base;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.zj.learning.CoreApp;
import com.zj.learning.LoadingHelper;
import com.zj.learning.R;
import com.zj.learning.model.forum.Tag;
import com.zj.learning.model.user.User;
import com.zj.support.cache.HttpCacheManager;
import com.zj.support.http.api.HttpApi;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RespOut;
import com.zj.support.http.upload.UploadApi;

public class BaseActivity extends Activity implements ICallback<RespOut> {

	/** Volley请求标识，用于发送、取消请求 **/
	protected int mReqTag;
	private LoadingHelper mLoadingHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.prepareViews();
		this.prepareDatas(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		this.cancelRequests();
		super.onDestroy();
	}

	/**
	 * 网络请求成功的响应方法,子类必须实现
	 * @param arg0
	 */
	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub

	}

	/**
	 * 网络请求出错的响应方法,子类必须实现
	 * @param out
	 */
	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		this.showToast(this.getString(R.string.request_fail));
		this.showRetry();
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		this.setResultBack();
		super.onBackPressed();
	}

	protected void prepareViews() {

	}

	protected void prepareDatas(Bundle savedInstanceState) {
		mReqTag = this.hashCode();
	}

	protected void buildLoading(LinearLayout mLlLoading) {
		mLoadingHelper = new LoadingHelper(this, mLlLoading);
	}

	protected void showLoading() {
		if (null != mLoadingHelper) {
			mLoadingHelper.showLoading();
		}
	}

	protected void hideLoading() {
		if (null != mLoadingHelper) {
			mLoadingHelper.hideLoading();
		}
	}

	protected void showRetry() {
		if (null != mLoadingHelper) {
			mLoadingHelper.showRetry();
		}
	}

	protected void hideRetry() {
		if (null != mLoadingHelper) {
			mLoadingHelper.hideRetry();
		}
	}

	protected CoreApp getCoreApp() {
		return ((CoreApp) this.getApplication());
	}

	protected boolean isNetworkEnable() {
		return this.getCoreApp().isNetworkEnable();
	}

	protected void showToast(String msg) {
		this.getCoreApp().showToast(msg);
	}

	protected UploadApi getUploadApi() {
		return this.getCoreApp().getUploadApi();
	}

	protected HttpApi getHttpApi() {
		return this.getCoreApp().getHttpApi();
	}

	protected HttpCacheManager getCacheManager() {
		return HttpCacheManager.getHttpCacheManager(this
				.getApplicationContext());
	}

	private void cancelRequests() {
		this.getHttpApi().cancelAll(mReqTag);
	}

	protected boolean isLogin() {
		return this.getCoreApp().isLogin();
	}

	protected User getUser() {
		return this.getCoreApp().getUser();
	}

	protected void setUser(User user) {
		this.getCoreApp().setUser(user);
	}

	protected ArrayList<Tag> getTags() {
		return this.getCoreApp().getTags();
	}

	protected void setTags(ArrayList<Tag> tags) {
		this.getCoreApp().setTags(tags);

	}
	
	/**
	 * 设置返回值，交给子类实现
	 */
	protected void setResultBack(){
		
	}

	public void onBtnBackClick(View view) {// 点击返回
		this.setResultBack();
		this.finish();
	}

	public void onSwipeFinish() {// 滑动返回
		this.setResultBack();
		this.finish();
	}

}
