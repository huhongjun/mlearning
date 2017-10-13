package com.zj.app;

import java.util.List;

import android.os.Handler;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.zj.support.widget.refresh.PullToRefreshBase.Mode;
import com.zj.support.widget.refresh.PullToRefreshBase.OnPullEventListener;
import com.zj.support.widget.refresh.PullToRefreshBase.OnRefreshListener;
import com.zj.support.widget.refresh.PullToRefreshBase.OnRefreshListener2;
import com.zj.support.widget.refresh.PullToRefreshBase.State;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 下拉刷新、上拉加载更多的ListView的助手（遵循合成/聚合设计模式）
 * 
 * @author XingRongJing
 * 
 */
public class RefreshListHelper {

	private PullToRefreshListView mListView;
	/** 刷新页码，默认从1起始 **/
	private int mPage = 1;
	/** 分页最大条数，默认每页15条 **/
	private int mMaxLoad = 15;
	/** 是否第一次手动加载数据 **/
	private boolean mIsFirstManual = false;
	/** 是否还可加载更多 **/
	private boolean mHasMore = true;
	private Handler mHandler;
	/** 预先加载显示View **/
	private View mLoadingView;
	/** 列表为空时显示的View **/
	private View mEmptyView;
	/** 没网且没缓存时显示的View **/
	private View mNoNetworkView;
	/** 是否无网点击重新加载数据 **/
	private boolean mRetryLoad = false;
	/** 自动刷新key，设置之后可防止一次登录多次自动刷新 **/
	private String mRefreshAotuKey;
	private ImageLoadingHelper mImageLoadingHelper;
	private long mTimestamp = 0l;

	/**
	 * @param listview
	 *            可下拉、上拉加载数据的View
	 * @param loadingView
	 *            加载缓存时提前显示的View，与listview互斥
	 * @param emptyView
	 *            没数据时显示的View
	 * @param noNetworkView
	 *            没网且没缓存时显示的View
	 */
	public RefreshListHelper(PullToRefreshListView listview, View loadingView,
			View emptyView, View noNetworkView) {
		if (null == listview) {
			throw new IllegalArgumentException("Listview is null.");
		}
		this.mListView = listview;
		this.mLoadingView = loadingView;
		this.mEmptyView = emptyView;
		this.mNoNetworkView = noNetworkView;
		this.prepare();
	}

	private void prepare() {
		if (null != mListView) {
			mHandler = AppHelper.getAppHelper(
					mListView.getContext().getApplicationContext())
					.getHandler();
		}
	}

	/**
	 * 设置ListView可刷新类型 (Mode.DISABLED-不能刷新; Mode.BOTH-上拉、下拉均可;
	 * Mode.PULL_FROM_START-仅仅下拉; Mode.PULL_FROM_END-仅仅上拉;)
	 * 
	 * @param mode
	 */
	public void setMode(Mode mode) {
		if (null != mListView) {
			mListView.setMode(mode);
		}
	}

	/**
	 * 下拉、上拉刷新完毕时调用
	 */
	public void notifyLoadingComplete() {
		if (null != mListView) {
			mListView.onRefreshComplete();
			if (this.isFirstManual()) {
				// this.setScrollingWhileRefreshingEnabled(true);
				this.setIsFirstManual(false);
			}
		}
	}

	/**
	 * 设置加载数据中，是否还可下拉、上拉
	 * 
	 * @param enable
	 */
	public void setScrollingWhileRefreshingEnabled(boolean enable) {
		if (null != mListView) {
			mListView.setScrollingWhileRefreshingEnabled(enable);
		}
	}

	/**
	 * 设置缓存加载中动画图片的ImageView
	 * 
	 * @param mIvLoading
	 */
	public void setLoadingImageView(ImageView mIvLoading) {
		if (null == mImageLoadingHelper && null != mIvLoading) {
			mImageLoadingHelper = new ImageLoadingHelper(mIvLoading);
		}
	}

	/**
	 * 第一次手动加载数据时调用(需在setAdapter之后)
	 */
	public void onFirstManualRefresh() {
		if (null == mListView) {
			return;
		}
		this.setIsFirstManual(true);
		mListView.setRefreshing();
	}

	/**
	 * 重置ListView的header或footer（当没网或没有更多时调用）
	 */
	public void resetHeaderOrFooter() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				RefreshListHelper.this.notifyLoadingComplete();
			}
		}, 200);
	}

	/**
	 * 是否下拉刷新
	 * 
	 * @return
	 */
	public boolean isPullDownRefresh() {
		if (null == mListView) {
			return false;
		}
		return mListView.isHeaderShown();
	}

	/**
	 * 是否上拉加载更多
	 * 
	 * @return
	 */
	public boolean isPullUpRefresh() {
		if (null == mListView) {
			return false;
		}
		return mListView.isFooterShown();
	}

	/**
	 * 设置下拉、上拉时的字样
	 * 
	 * @param state
	 * @param refreshTips
	 * @param refreshUpdateTips
	 * @param loadTips
	 */
	public void setPullEventLabel(State state, String refreshTips,
			String refreshUpdateTips, String loadTips) {
		if (null == mListView) {
			return;
		}
		if (state.equals(State.PULL_TO_REFRESH)) {
			if (this.isPullDownRefresh()) {
				String label = DateUtils.formatDateTime(mListView.getContext()
						.getApplicationContext(), System.currentTimeMillis(),
						DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);
				mListView.getLoadingLayoutProxy().setRefreshingLabel(
						refreshTips);
				// Update the LastUpdatedLabel
				mListView.getLoadingLayoutProxy().setLastUpdatedLabel(
						refreshUpdateTips + "" + label);
			} else {
				mListView.getLoadingLayoutProxy().setRefreshingLabel(loadTips);
			}
		}
	}

	public void setLoadingView(View loadingView) {
		this.mLoadingView = loadingView;
	}

	/**
	 * 当没有数据时，调用此方法
	 */
	public void setEmptyView() {
		if (null != mListView && null != mEmptyView) {
			mListView.setEmptyView(mEmptyView);
		}
	}

	/**
	 * 当没网且没缓存时调用
	 */
	public void setNoNetworkView() {
		if (null != mListView && null != mNoNetworkView) {
			this.setMode(Mode.DISABLED);
			mListView.setEmptyView(mNoNetworkView);
		}
	}

	public void addHeaderView(View header) {
		if (null == mListView || null == header) {
			return;
		}
		ListView lv = mListView.getRefreshableView();
		lv.addHeaderView(header);
	}

	public void addFooterView(View footer) {
		if (null == mListView || null == footer) {
			return;
		}
		ListView lv = mListView.getRefreshableView();
		lv.addFooterView(footer);
	}

	public int getHeaderCount() {
		if (null == mListView) {
			return 0;
		}
		return mListView.getRefreshableView().getHeaderViewsCount();
	}

	public int getFooterCount() {
		if (null == mListView) {
			return 0;
		}
		return mListView.getRefreshableView().getFooterViewsCount();
	}

	public void showLoading() {
		if (null != mLoadingView) {
			mLoadingView.setVisibility(View.VISIBLE);
		}
		if (null != mImageLoadingHelper) {
			mImageLoadingHelper.startAnimation();
		}
		if (null != mListView) {
			mListView.setVisibility(View.GONE);
		}
	}

	public void hideLoading() {
		if (null != mLoadingView) {
			mLoadingView.setVisibility(View.GONE);
		}
		if (null != mImageLoadingHelper) {
			mImageLoadingHelper.stopAnimation();
		}
		if (null != mListView) {
			mListView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 根据返回数据确定是否需要添加EmptyView
	 * 
	 * @param results
	 * @return
	 */
	public boolean isNeedAddEmptyView(List<Object> results) {
		if (null == results) {
			return true;
		}
		// 如果服务器没数据，且是第一次加载
		if (results.size() <= 0 && this.isFirstManual()) {
			return true;
		}
		return false;
	}

	/**
	 * 请求列表数据成功时调用，预先处理一些事件
	 * 
	 * @param results
	 */
	public void onPrepareSuccess(List<?> results) {
		this.pagePlus();
		if (null == results) {
			//第一次加载，且是不添加刷新以外的headerview
			if (this.isFirstManual() && this.getHeaderCount() <= 1) {
				this.setEmptyView();
			}
			this.setHasMore(false);
			return;
		}
		int length = results.size();
		// 根据服务器返回值，处理是否还有更多
		if (results.isEmpty() || length < mMaxLoad) {
			this.setHasMore(false);
		}
		// 如果服务器没数据，且是第一次加载，且是不添加刷新以外的headerview
		if (results.isEmpty() && this.isFirstManual()
				&& this.getHeaderCount() <= 1) {
			this.setMode(Mode.PULL_FROM_START);
			this.setEmptyView();
		}
	}

	private void pagePlus() {
		mPage++;
	}

	public void pageReset() {
		mPage = 1;
		this.setHasMore(true);
	}

	public void setPage(int page) {
		this.mPage = page;
	}

	public long getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(long mTimestamp) {
		this.mTimestamp = mTimestamp;
	}

	public void resetTimestamp() {
		this.mTimestamp = 0l;
		this.setHasMore(true);
	}

	public int getPage() {
		return this.mPage;
	}

	public void setMaxLoad(int max) {
		this.mMaxLoad = max;
	}

	public int getMaxLoad() {
		return this.mMaxLoad;
	}

	public void setOnRefreshListener(OnRefreshListener2<ListView> listener) {
		if (null != mListView) {
			mListView.setOnRefreshListener(listener);
		}
	}

	public void setOnRefreshListener(OnRefreshListener<ListView> listener) {
		if (null != mListView) {
			mListView.setOnRefreshListener(listener);
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		if (null != mListView) {
			mListView.setOnItemClickListener(listener);
		}
	}

	public void setOnPullEventListener(OnPullEventListener<ListView> listener) {
		if (null != mListView) {
			mListView.setOnPullEventListener(listener);
		}
	}

	public boolean isFirstManual() {
		return mIsFirstManual;
	}

	public void setIsFirstManual(boolean isFirstManual) {
		this.mIsFirstManual = isFirstManual;
	}

	public boolean isHasMore() {
		return mHasMore;
	}

	public void setHasMore(boolean hasMore) {
		this.mHasMore = hasMore;
	}

	public boolean isRetryLoad() {
		return mRetryLoad;
	}

	public void setRetryLoad(boolean retryLoad) {
		this.mRetryLoad = retryLoad;
	}

	public String getRefreshAotuKey() {
		return mRefreshAotuKey;
	}

	public void setRefreshAotuKey(String mRefreshAotuKey) {
		this.mRefreshAotuKey = mRefreshAotuKey;
	}

}
