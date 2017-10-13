package com.zhijin.mlearning.app.view.listview;

/**
 * 真正实现上拉加载的是这个控件,
 * ScrollOverListView只是提供触摸的事件等,
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.constant.GlobalConfig;
import com.zhijin.mlearning.app.view.listview.ScrollOverListView.OnScrollOverListener;

public class UploadView extends LinearLayout implements OnScrollOverListener
{

	public boolean isLoading = false;
	private static final int WHAT_DID_LOAD_DATA = 1; // Handler what 数据加载完毕
	private static final int WHAT_DID_MORE = 5; // Handler what 已经获取完更多
	private static final int WHAT_DID_LOADING = 0;
	// private static SimpleDateFormat dateFormat = new SimpleDateFormat(
	// "MM-dd HH:mm");

	private View mFooterView;
	private TextView mFooterTextView;
	private View mFooterLoadingView;
	private ScrollOverListView mListView;
	private OnUploadListener mOnPullDownListener;
	private boolean mIsFetchMoreing = false; // 是否获取更多中
	private boolean mEnableAutoFetchMore; // 是否允许自动获取更多
	public boolean haveMore = true;

	// 构造
	public UploadView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initFooterViewAndListView(context);
	}

	// 构造
	public UploadView(Context context)
	{
		super(context);
		initFooterViewAndListView(context);
	}

	/**
	 * 事件接口
	 */
	public interface OnUploadListener
	{
		void onMore();
	}

	/**
	 * 通知加载完了数据，要放在Adapter.notifyDataSetChanged后面,
	 * 当你加载完数据的时候，调用这个notifyDidLoad() 才会隐藏头部，并初始化数据等
	 */
	public void notifyDidLoad()
	{
		mUIHandler.sendEmptyMessage(WHAT_DID_LOAD_DATA);// 数据加载完毕
	}

	/**
	 * 通知加载完了数据，要放在Adapter.notifyDataSetChanged后面,
	 * 当你加载完数据的时候，调用这个notifyDidLoad() 才会隐藏头部，并初始化数据等
	 */
	public void notifyDidLoading()
	{
		mUIHandler.sendEmptyMessage(WHAT_DID_LOADING);// 数据加载完毕
	}

	/**
	 * 通知已经获取完更多了，要放在Adapter.notifyDataSetChanged后面
	 * 当你执行完更多任务之后，调用这个notyfyDidMore() 才会隐藏加载圈等操作
	 */
	public void notifyDidMore(boolean isSuccess)
	{
		Message messge = new Message();
		Bundle bundle = new Bundle();
		bundle.putBoolean("success", isSuccess);
		messge.setData(bundle);
		messge.what = WHAT_DID_MORE;
		mUIHandler.sendMessage(messge);// 已经获取完更多
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 */
	public void setOnPullDownListener(OnUploadListener listener)
	{
		mOnPullDownListener = listener;
	}

	/**
	 * 获取内嵌的listview
	 * 
	 * @return ScrollOverListView
	 */
	public ListView getListView()
	{
		return mListView;
	}

	/**
	 * 是否开启自动获取更多，自动获取更多，将会隐藏footer，并在到达底部的时候自动刷新
	 * 
	 * @param index
	 *            倒数第几个触发
	 */
	public void enableAutoFetchMore(boolean enable, int index)
	{
		if (enable) {
			mListView.setBottomPosition(index);
			mFooterLoadingView.setVisibility(View.VISIBLE);
		} else {
			mFooterTextView.setText("更多");
			mFooterLoadingView.setVisibility(View.GONE);
		}
		mEnableAutoFetchMore = enable;
	}

	/*
	 * ============ Private method 具体实现下拉刷新等操作
	 */
	/**
	 * 初始化界面
	 */
	private void initFooterViewAndListView(Context context)
	{
		setOrientation(LinearLayout.VERTICAL);
		/**
		 * 自定义脚部文件
		 */
		mFooterView = LayoutInflater.from(context).inflate(R.layout.uploadview_footer, null);
		mFooterTextView = (TextView) mFooterView.findViewById(R.id.pulldown_footer_text);
		mFooterLoadingView = mFooterView.findViewById(R.id.pulldown_footer_loading);
		mFooterView.setOnClickListener(new OnClickListener() {// 点击事件
					@Override
					public void onClick(View v)
					{
						if (haveMore && !mIsFetchMoreing) {
							mIsFetchMoreing = true;
							mFooterTextView.setText("加载更多中...");
							mFooterLoadingView.setVisibility(View.VISIBLE);
							mOnPullDownListener.onMore();
						}
					}
				});

		/*
		 * ScrollOverListView 同样是考虑到都是使用，所以放在这里。同时因为，需要它的监听事件
		 */
		mListView = new ScrollOverListView(context);
		mListView.setOnScrollOverListener(this);
		mListView.setCacheColorHint(0);
		addView(mListView, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		// 空的listener
		mOnPullDownListener = new OnUploadListener() {
			@Override
			public void onMore()
			{
			}
		};
	}

	private Handler mUIHandler = new Handler() {
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			case WHAT_DID_LOADING:
				showFooterView();
				mFooterTextView.setText("加载数据中...");
				mFooterLoadingView.setVisibility(View.VISIBLE);
				break;
			case WHAT_DID_LOAD_DATA: // 数据加载完毕
				showFooterView();
				break;
			case WHAT_DID_MORE: {// 已经获取完更多
				Bundle bundle = msg.getData();
				boolean success = bundle.getBoolean("success");
				if (success) {
					isLoadindMore();
					if (!haveMore || mIsFetchMoreing == true) {
						mFooterTextView.setText("已经是最后一条");
						mFooterLoadingView.setVisibility(View.GONE);
						haveMore = true;
					} else {
						mFooterTextView.setText("更多");
						mFooterLoadingView.setVisibility(View.GONE);
					}
				} else {
					mFooterTextView.setText("更多");
					mFooterLoadingView.setVisibility(View.GONE);
					haveMore = true;
					mIsFetchMoreing = false;
				}
			}
			}
		}
	};

	/**
	 * 显示脚步脚部文件
	 */
	private void showFooterView()
	{
		if (mListView.getFooterViewsCount() == 0 && isFillScreenItem()) {
			mListView.addFooterView(mFooterView);
			mListView.setAdapter(mListView.getAdapter());
		}
	}

	/**
	 * item条目是否填满整个屏幕
	 */
	private boolean isFillScreenItem()
	{
		final int firstVisiblePosition = mListView.getFirstVisiblePosition();
		final int lastVisiblePosition = mListView.getLastVisiblePosition() - mListView.getFooterViewsCount();
		final int visibleItemCount = lastVisiblePosition - firstVisiblePosition + 1;
		final int totalItemCount = mListView.getCount() - mListView.getFooterViewsCount();
		isLoadindMore();
		if (visibleItemCount <= totalItemCount) {
			System.out.println("isFillScreenItemtrue");
			return true;
		} else {
			System.out.println("isFillScreenItemfalse");
			return false;
		}
	}

	private void isLoadindMore()
	{
		final int totalItemCount = mListView.getCount() - mListView.getFooterViewsCount();
		if (!haveMore || totalItemCount < GlobalConfig.MAX || totalItemCount % GlobalConfig.MAX > 0
				&& totalItemCount % GlobalConfig.MAX < GlobalConfig.MAX) {
			mIsFetchMoreing = true;
		} else {
			mIsFetchMoreing = false;
		}
	}

	// 是否上拉
	@Override
	public boolean onListViewBottomAndPullUp(int delta)
	{
		if (isLoading || !mEnableAutoFetchMore || mIsFetchMoreing) {
			return false;
		}
		// 数量充满屏幕才触发
		if (isFillScreenItem()) {
			isLoading = true;
			mFooterTextView.setText("加载更多中...");
			mFooterLoadingView.setVisibility(View.VISIBLE);
			mOnPullDownListener.onMore();
			return true;
		}
		return false;
	}

	@Override
	public boolean onMotionDown(MotionEvent ev)
	{
		return false;
	}

	@Override
	public boolean onMotionMove(MotionEvent ev, int delta)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMotionUp(MotionEvent ev)
	{
		// TODO Auto-generated method stub
		return false;
	}
}
