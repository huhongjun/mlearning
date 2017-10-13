package com.zj.support.widget;

import android.R.interpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * 监听滚动状态和Y轴的ScrollView
 * 
 * @author XingRongJing
 * 
 */
public class CustomScrollView extends ScrollView {

	private static final int MILLISECOND = 5;
	private OnScrollListener mOnScrollListener;
	private boolean mIsBottom = false;

	/**
	 * 主要是用在用户手指离开ScrollView时，其还在继续滑动，我们用来保存Y的距离，然后做比较
	 */
	private int mLastScrollY;

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CustomScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 用于用户手指离开MyScrollView的时候获取MyScrollView滚动的Y距离，然后回调给onScroll方法中
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			int scrollY = CustomScrollView.this.getScrollY();

			if (null != mOnScrollListener) {
				mOnScrollListener.onScroll(scrollY, (mLastScrollY == scrollY),
						mIsBottom);
			}
			// 此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
			if (mLastScrollY != scrollY) {
				mLastScrollY = scrollY;
				handler.sendMessageDelayed(handler.obtainMessage(), MILLISECOND);
			}

		};

	};

	/**
	 * 重写onTouchEvent， 当用户的手在MyScrollView上面的时候，
	 * 直接将MyScrollView滑动的Y方向距离回调给onScroll方法中，当用户抬起手的时候，
	 * MyScrollView可能还在滑动，所以当用户抬起手我们隔5毫秒给handler发送消息，在handler处理
	 * MyScrollView滑动的距离
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (null != mOnScrollListener) {
			mOnScrollListener.onScroll(mLastScrollY = this.getScrollY(), false,
					mIsBottom);
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			handler.sendMessageDelayed(handler.obtainMessage(), MILLISECOND);
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		mIsBottom = (t + getHeight() >= computeVerticalScrollRange());
	}

	public void setOnScrollListener(OnScrollListener listener) {
		this.mOnScrollListener = listener;
	}

	public interface OnScrollListener {
		/**
		 * 回调方法， 返回CustomScrollView滑动的Y轴距离
		 * 
		 * @param scrollY
		 * @param 是否滚动停止
		 * @param 是否已滚动到底部
		 * 
		 */
		public void onScroll(int scrollY, boolean scroll, boolean bottom);

	}

}
