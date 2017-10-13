package com.zj.learning.control.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListFragment;
import com.zj.learning.model.user.Certificate;
import com.zj.learning.view.user.CertificateItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 课程证书界面
 * 
 * @author XingRongJing
 * 
 */
public class CourseCertificateListFragment extends BaseRefreshListFragment {

	private List<Certificate> mCertificates;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCertificates = new ArrayList<Certificate>();
		mAdapter = new ItemSingleAdapter<CertificateItemView, Certificate>(
				this.getActivity(), mCertificates);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.certificate_list, null);
		mLlLoading = (LinearLayout) view
				.findViewById(R.id.certificate_list_loading);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.certificate_list_listview);
		mListView.setAdapter(mAdapter);

		this.prepareByView();
		this.setRefreshAutoKey(GlobalConfig.URL_CERTIFICATE_COURSE);
		// 如果还未加载过
		if (!this.isLoaded()) {
			this.fetchCacheDatas();
			this.setIsLoaded(true);
		} else if (this.isDataEmpty()) {
			if (this.isNetworkEnable() && !this.isReqFails()) {
				this.setEmptyView();
			} else {
				this.setNoNetworkView();
			}
		}
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
	}

	@Override
	protected void fetchCacheDatas() {
		// TODO Auto-generated method stub
		// Step 1：先看缓存
		this.showLoading();
		this.getCacheManager().getCacheByUrl(this, new RequestTag(mReqTag),
				GlobalConfig.URL_CERTIFICATE_COURSE);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleSuccessDatas(List<?> results) {
		// TODO Auto-generated method stub
		if (null != results) {
			List<Certificate> temps = (List<Certificate>) results;
			mCertificates.addAll(temps);
		}
		this.notifyDataChanged();
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		// TODO Auto-generated method stub
		// Step 2.2：获取数据
		RequestTag tag = new RequestTag(mReqTag);
		if (needCache) {
			tag.cacheUrl = GlobalConfig.URL_CERTIFICATE_COURSE;
		}
		Map<String, String> params = new HashMap<String, String>();
		this.getHttpApi().request(GlobalConfig.URL_CERTIFICATE_COURSE, params,
				this, tag);

	}

	@Override
	protected boolean isDataEmpty() {
		// TODO Auto-generated method stub
		return (null == mCertificates ? false : mCertificates.isEmpty());
	}

	@Override
	protected void clearDatas() {
		// TODO Auto-generated method stub
		if (null != mCertificates) {
			mCertificates.clear();
		}
	}

	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		// Step 3.1：获取成功
		if (null == arg0) {
			return;
		}
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			//手动保存需要缓存数据
			if (null != arg0.param && !TextUtils.isEmpty(arg0.param.cacheUrl)) {
				this.saveCache(arg0.param.cacheUrl, arg0.resp);
			}
			List<Certificate> temps = parser.getArray(Certificate.class);
			this.handleFetchDataSuccess(temps);
		} else {
			this.handleFetchDataFail();
		}
	}

	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseCache(out);
		// Step 2.1：获取缓存成功，加载数据
		if (null == out.param) {
			return;
		}
		RequestDataParser parser = new RequestDataParser(out.resp);
		List<Certificate> temps = parser.getArray(Certificate.class);
		this.handleFetchCacheComplete(temps);
	}

}
