package com.zj.learning;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

public class ViewAnimListener implements AnimationListener {

	private View mView;
	private int mVisibility = -1;

	public ViewAnimListener(View view, int visibility) {
		if (null == view) {
			throw new IllegalArgumentException("View cannot be null");
		}
		this.mView = view;
		mVisibility = visibility;
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		mView.setVisibility(mVisibility);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}
}
