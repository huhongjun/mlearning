package com.zj.learning.control.common.base;

import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.zj.app.RefreshListHelper;
import com.zj.learning.CoreApp;
import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.support.cache.HttpCacheManager;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.refresh.PullToRefreshBase;
import com.zj.support.widget.refresh.PullToRefreshBase.Mode;
import com.zj.support.widget.refresh.PullToRefreshBase.OnPullEventListener;
import com.zj.support.widget.refresh.PullToRefreshBase.OnRefreshListener2;
import com.zj.support.widget.refresh.PullToRefreshBase.State;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 下拉刷新/上拉加载的ListView的Fragment的基类
 * 
 * @author XingRongJing
 * 
 */
public abstract class BaseRefreshListFragment extends BaseFragment implements
		OnItemClickListener, OnRefreshListener2<ListView>,
		OnPullEventListener<ListView>, ICallback<RespOut>, OnClickListener {

	private RefreshListHelper mRefreshHelper;
	protected PullToRefreshListView mListView;
	protected LinearLayout mLlLoading;
	protected BaseAdapter mAdapter;
	/** 是否已经加载过数据 **/
	private boolean mIsLoaded = false;
	private boolean mIsReqListFails = false;
	private ImageView mIvLoading;

	/**
	 * 必须在onCreateView初始化view之后调用
	 */
	protected void prepareByView() {
		LayoutInflater inflater = this.getActivity().getLayoutInflater();
		View emptyView = inflater.inflate(R.layout.list_empty, null);
		View mRlNoNetwork = (RelativeLayout) inflater.inflate(
				R.layout.no_network, null);
		mRlNoNetwork.setOnClickListener(this);
		RefreshListHelper temp = null;
		if (null != mRefreshHelper) {
			temp = mRefreshHelper;
		}
		mRefreshHelper = new RefreshListHelper(mListView, mLlLoading,
				emptyView, mRlNoNetwork);
		if (null != temp) {
			mRefreshHelper.setPage(temp.getPage());
			mRefreshHelper.setHasMore(temp.isHasMore());
			mRefreshHelper.setIsFirstManual(temp.isFirstManual());
			mRefreshHelper.setRetryLoad(temp.isRetryLoad());
			mRefreshHelper.setTimestamp(temp.getTimestamp());
			mRefreshHelper.setMaxLoad(temp.getMaxLoad());
			temp = null;
		}
		mRefreshHelper.setOnItemClickListener(this);
		mRefreshHelper.setOnRefreshListener(this);
		mRefreshHelper.setOnPullEventListener(this);
		mRefreshHelper.setMaxLoad(GlobalConfig.MAX_LOAD);

		if (null != mLlLoading) {
			mIvLoading = (ImageView) mLlLoading
					.findViewById(R.id.loading_iv_loading);
		}
		mRefreshHelper.setLoadingImageView(mIvLoading);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		this.getHttpApi().cancelAll(mReqTag);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.error_rl:
			this.onClickErrorLoading(v);
			break;
		}
	}

	protected void setIsReqFails(boolean isFails) {
		this.mIsReqListFails = isFails;
	}

	protected boolean isReqFails() {
		return this.mIsReqListFails;
	}

	protected void setNoNetworkView() {
		if (null == mRefreshHelper) {
			return;
		}
		mRefreshHelper.setNoNetworkView();
	}

	protected void setEmptyView() {
		if (null == mRefreshHelper) {
			return;
		}
		mRefreshHelper.setEmptyView();
	}

	protected void onFirstManualRefresh() {
		if (null == mRefreshHelper) {
			return;
		}
		mRefreshHelper.onFirstManualRefresh();
		;
	}

	protected long getTimestamp() {
		return null == mRefreshHelper ? 0 : mRefreshHelper.getTimestamp();
	}

	protected void setTimestamp(long mTimestamp) {
		mRefreshHelper.setTimestamp(mTimestamp);
	}

	protected void reset() {
		this.setIsLoaded(false);
		mRefreshHelper.resetTimestamp();
	}

	public void onClickErrorLoading(View view) {// 没网时，点击重新加载
		if (this.isNetworkEnable()) {
			mRefreshHelper.showLoading();
			mRefreshHelper.setRetryLoad(true);
			this.fetchDatas(true);
		} else {
			this.showToast(this.getString(R.string.no_network));
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!this.isNetworkEnable()) {// 沒有网络
			this.showToast(this.getString(R.string.no_network));
			mRefreshHelper.resetHeaderOrFooter();
			return;
		}
		PullToRefreshBase.State state = refreshView.getState();
		// 手动刷新
		if (state.equals(PullToRefreshBase.State.MANUAL_REFRESHING)) {
			this.fetchDatas(true);
		} else if (state.equals(PullToRefreshBase.State.REFRESHING)) {// 下拉刷新
			mRefreshHelper.pageReset();
			this.reset();
			this.fetchDatas(true);
		}
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!this.isNetworkEnable()) {// 没有网络
			this.showToast(this.getString(R.string.no_network));
			mRefreshHelper.resetHeaderOrFooter();
			return;
		}
		if (!mRefreshHelper.isHasMore()) {// 没有更多数据了
			this.showToast(this.getString(R.string.no_more_datas));
			mRefreshHelper.resetHeaderOrFooter();
			return;
		}
		this.fetchDatas(false);
	}

	@Override
	public void onPullEvent(PullToRefreshBase<ListView> refreshView,
			State state, Mode direction) {
		// TODO Auto-generated method stub
		mRefreshHelper.setPullEventLabel(state,
				this.getString(R.string.list_refresh_tip),
				this.getString(R.string.list_refresh_updated),
				this.getString(R.string.list_loading_tip));
	}

	@Override
	public void onResponse(RespOut out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		this.handleFetchDataFail();
	}

	protected HttpCacheManager getCacheManager() {
		return HttpCacheManager.getHttpCacheManager(this.getActivity()
				.getApplicationContext());
	}

	protected void notifyDataChanged() {
		if (null == mAdapter) {
			return;
		}
		mAdapter.notifyDataSetChanged();
	}

	protected void setRefreshAutoKey(String key) {
		if (null != mRefreshHelper) {
			mRefreshHelper.setRefreshAotuKey(key);
		}
	}

	protected String getRefreshAutoKey() {
		return mRefreshHelper.getRefreshAotuKey();
	}

	protected int getPage() {
		return mRefreshHelper.getPage();
	}

	protected int getMax() {
		return mRefreshHelper.getMaxLoad();
	}

	protected void setMax() {
		mRefreshHelper.setMaxLoad(GlobalConfig.MAX_LOAD);
	}

	protected void setMode(Mode mode) {
		mRefreshHelper.setMode(mode);
	}

	public void setIsLoaded(boolean isLoaded) {
		this.mIsLoaded = isLoaded;
	}

	/**
	 * 是否已经加载--限制仅加载一次
	 * 
	 * @return
	 */
	public boolean isLoaded() {
		return mIsLoaded;
	}

	protected void showLoading() {
		mRefreshHelper.showLoading();
	}

	protected void hideLoading() {
		mRefreshHelper.hideLoading();
	}

	/**
	 * 保存缓存（手动调用）--需要时调用
	 * 
	 * @param cacheUrl
	 * @param result
	 */
	protected void saveCache(String cacheUrl, String result) {
		if (TextUtils.isEmpty(cacheUrl) || TextUtils.isEmpty(result)) {
			return;
		}
		this.getCacheManager().saveOrUpdateCache(null, null, cacheUrl, result);
	}

	/**
	 * 获取缓存完成之后调用
	 */
	protected void handleFetchCacheComplete(List<?> results) {
		this.handleSuccessDatas(results);
		this.hideLoading();
		if (this.isNetworkEnable()) {// 有网
			// 是否需要自动刷新
			if (((CoreApp) this.getActivity().getApplication())
					.isRefreshAuto(this.getRefreshAutoKey())) {
				this.onFirstManualRefresh();
			} else if (this.isDataEmpty()) {// 无需自动刷新且无数据时
				this.setEmptyView();
			}
		} else if (this.isDataEmpty()) {// 无网且无数据时
			this.setNoNetworkView();
		}
	}

	/**
	 * 获取数据成功之后调用
	 * 
	 * @param results
	 */
	protected void handleFetchDataSuccess(List<?> results) {
		this.setIsReqFails(false);
		mRefreshHelper.onPrepareSuccess(results);
		// 如果是没网时点击加载
		if (mRefreshHelper.isRetryLoad()) {
			mRefreshHelper.setRetryLoad(false);
			mRefreshHelper.hideLoading();
			mRefreshHelper.setMode(Mode.BOTH);
			this.handleSuccessDatas(results);
			return;
		} else {// 下拉、上拉
				// 下拉刷新或第一次刷新，重置数据避免重复
			if (mRefreshHelper.isPullDownRefresh()
					|| mRefreshHelper.isFirstManual()) {
				this.clearDatas();
			}
			mRefreshHelper.notifyLoadingComplete();
			this.handleSuccessDatas(results);
		}
	}

	/**
	 * 获取数据失败时调用
	 */
	protected void handleFetchDataFail() {
		this.showToast(this.getString(R.string.request_fail));
		this.setIsReqFails(true);
		// Step 3.2：加载数据失败
		if (mRefreshHelper.isRetryLoad()) {// 如果是没网时点击加载
			mRefreshHelper.hideLoading();
			return;
		}
		// 加载失败且目前无数据
		if (this.isDataEmpty()) {
			mRefreshHelper.setNoNetworkView();
		}
		mRefreshHelper.notifyLoadingComplete();
	}

	/**
	 * 获取缓存-一切的操作的源头
	 */
	protected abstract void fetchCacheDatas();

	/**
	 * 请求数据成功（网络或缓存）处理
	 * 
	 * @param result
	 */
	protected abstract void handleSuccessDatas(List<?> result);

	/**
	 * 获取网络数据
	 * 
	 * @param needCache
	 *            是否需要缓存
	 */
	protected abstract void fetchDatas(boolean needCache);

	protected abstract boolean isDataEmpty();

	protected abstract void clearDatas();

}
