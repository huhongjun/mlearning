package com.zj.learning.control.common.base;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.zj.learning.CoreApp;
import com.zj.learning.model.forum.Tag;
import com.zj.learning.model.user.User;
import com.zj.support.http.api.HttpApi;

public class BaseFragment extends Fragment {

	/** 请求标识，用于发送、取消请求 **/
	protected int mReqTag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mReqTag = this.hashCode();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.cancelRequests();
		super.onDestroy();
	}

	private CoreApp getCoreApp() {
		return ((CoreApp) this.getActivity().getApplication());
	}

	protected boolean isNetworkEnable() {
		return this.getCoreApp().isNetworkEnable();
	}

	protected void showToast(String msg) {
		this.getCoreApp().showToast(msg);
	}

	protected HttpApi getHttpApi() {
		return this.getCoreApp().getHttpApi();
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
}
