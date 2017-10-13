package com.zj.support.widget.swipeback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.zj.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class SwipeBackHelper {

	/**
	 * 滑动的最小距离
	 */
	private int mTouchSlop;
	/**
	 * 按下点的X坐标
	 */
	private int mDownX;
	/**
	 * 按下点的Y坐标
	 */
	private int mDownY;
	private Context mCtx;
	/** 是否是滑动返回 **/
	private boolean mEnable = true;
	private ISwipeBackListener mSwipeBackListener;

	SwipeBackHelper(Context ctx, AttributeSet attrs) {
		if (null == ctx) {
			throw new IllegalArgumentException("Context can not null");
		}
		mTouchSlop = ViewConfiguration.get(ctx).getScaledTouchSlop();
		mCtx = ctx;
		TypedArray ta = mCtx.obtainStyledAttributes(attrs,
				R.styleable.SwipeBack);
		boolean enable = ta.getBoolean(R.styleable.SwipeBack_enableSwipe, true);
		final String handlerName = ta
				.getString(R.styleable.SwipeBack_onSwipeFinish);
		if (enable && !TextUtils.isEmpty(handlerName)) {
			this.bindSwipeListener(handlerName);
		}
		ta.recycle();
	}

	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!mEnable) {
			return false;
		}
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = (int) ev.getRawX();
			mDownY = (int) ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			int moveX = (int) ev.getRawX();
			int moveY = (int) ev.getRawY();
			// 满足是横向滑动则不传递touch事件给子类
			if (Math.abs(moveX - mDownX) > mTouchSlop
					&& Math.abs(moveY - mDownY) < mTouchSlop) {
				return true;
			}
			break;
		}
		return false;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!mEnable) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			int upX = (int) event.getRawX();
			int upY = (int) event.getRawY();
			int deltaX = upX - mDownX;
			int deltaY = upY - mDownY;
			if (Math.abs(deltaY) == 0) {
				return false;
			}
			// 如果是从右向左滑或横竖比小于1或滑动距离小于mTouchSlop则交给子类
			if (deltaX <= 0 || (Math.abs(deltaX) / Math.abs(deltaY) <= 1)
					|| deltaX <= mTouchSlop) {
				return false;
			}
			if (null != mSwipeBackListener) {
				mSwipeBackListener.onSwipeBackFinish();
			}
			break;
		}
		return true;

	}

	public void setSwipeBackListener(ISwipeBackListener swipeBackListener) {
		this.mSwipeBackListener = swipeBackListener;
	}

	public void setEnable(boolean enable) {
		this.mEnable = enable;
	}

	public boolean isEnable() {
		return this.mEnable;
	}

	private void bindSwipeListener(final String handlerName) {
		this.setSwipeBackListener(new ISwipeBackListener() {

			private Method mHandler;

			@Override
			public void onSwipeBackFinish() {
				// TODO Auto-generated method stub
				if (null == mHandler) {
					try {
						mHandler = mCtx.getClass().getMethod(handlerName, null);
					} catch (NoSuchMethodException e) {
						throw new IllegalStateException(
								"Could not find a method "
										+ handlerName
										+ "(SwipeBackLinearLayout) in the activity "
										+ mCtx.getClass()
										+ " for onSwipeListener handler"
										+ " on view "
										+ SwipeBackHelper.this.getClass(), e);
					}
				}

				try {
					mHandler.invoke(mCtx, null);
				} catch (IllegalArgumentException e) {
					throw new IllegalStateException("Could not execute "
							+ "method of the activity", e);
				} catch (IllegalAccessException e) {
					throw new IllegalStateException("Could not execute non"
							+ "public method of a activity", e);
				} catch (InvocationTargetException e) {
					throw new IllegalStateException("Could not execute "
							+ "method of the activity", e);
				}
			}
		});

	}

}
