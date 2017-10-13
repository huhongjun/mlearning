package com.zj.support.widget.swipeback;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class SwipeBackLinearLayout extends LinearLayout {

	private SwipeBackHelper mSwipeHelper;

	public SwipeBackLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mSwipeHelper = new SwipeBackHelper(context, attrs);

	}

	public SwipeBackLinearLayout(Context context) {
		this(context, null);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return mSwipeHelper.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return mSwipeHelper.onTouchEvent(event);
	}

	public void setSwipeBackListener(ISwipeBackListener swipeBackListener) {
		mSwipeHelper.setSwipeBackListener(swipeBackListener);
	}

	public void setEnable(boolean enable) {
		mSwipeHelper.setEnable(enable);
	}

	public boolean isEnable() {
		return mSwipeHelper.isEnable();
	}

}
