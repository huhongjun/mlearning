package com.zj.learning;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zj.app.ImageLoadingHelper;

/**
 * 加载等待助手
 * 
 * @author XingRongJing
 * 
 */
public class LoadingHelper {

	private View mProgress;
	private View mRetry;
	private ImageView mIvProgress;
	private TextView mTvProgress;
	private ImageLoadingHelper mIvLoadingHelper;

	public LoadingHelper(Context ctx, View progress) {
		this(ctx, progress, null, null);
	}

	public LoadingHelper(Context ctx, View progress, ImageView ivProgress,
			TextView tvProgress) {
		this.mProgress = progress;
		if (null == progress) {
			throw new IllegalArgumentException("Progress view cannot null");
		}
		this.mIvProgress = ivProgress;
		this.mTvProgress = tvProgress;
		this.prepareViews(ctx);
		this.prepareDatas();
	}

	private void prepareViews(Context ctx) {
		if (null == mIvProgress) {
			mIvProgress = (ImageView) mProgress
					.findViewById(R.id.profress_iv_loading);
		}
		if (null == mTvProgress) {
			mTvProgress = (TextView) mProgress.findViewById(R.id.progressMemo);
		}

		if (null == mIvProgress) {
			mIvProgress = (ImageView) mProgress
					.findViewById(R.id.loading_iv_loading);
		}
		if (null == mTvProgress) {
			mTvProgress = (TextView) mProgress
					.findViewById(R.id.loading_tv_text);
		}
		if(null==mRetry){
			mRetry = (View) mProgress.findViewById(R.id.loading_part_ll_no_network);
		}
	}

	private void prepareDatas() {
		if (null != mIvProgress) {
			mIvLoadingHelper = new ImageLoadingHelper(mIvProgress);
		}
	}

	public void setRetryView(View retry) {
		this.mRetry = retry;
	}

	public void setImageView(ImageView iv) {
		this.mIvProgress = iv;
		this.prepareDatas();
	}
	
	public void setImageBitmap(int resId) {
		if (null != mIvProgress) {
			mIvProgress.setImageResource(resId);
		}
	}

	public void setLoadingText(String loadingTips) {
		if (null != mTvProgress) {
			mTvProgress.setText(loadingTips);
		}
	}

	public void showLoading() {
		if (View.VISIBLE == mProgress.getVisibility()) {
			return;
		}
		this.showView(mProgress);
		this.startAnimation();
	}

	public void hideLoading() {
		if (View.GONE == mProgress.getVisibility()) {
			return;
		}
		this.hideView(mProgress);
		this.stopAnimation();
	}

	public void showRetry() {
		this.showView(mRetry);
		this.hideView(mIvProgress);
		this.hideView(mTvProgress);
		this.stopAnimation();
	}

	public void hideRetry() {
		this.showView(mIvProgress);
		this.showView(mTvProgress);
		this.hideView(mRetry);
		this.startAnimation();
	}

	private void showView(View view) {
		if (null == view) {
			return;
		}
		view.setVisibility(View.VISIBLE);
	}

	private void hideView(View view) {
		if (null == view) {
			return;
		}
		view.setVisibility(View.GONE);
	}

	private void startAnimation() {
		if (null != mIvLoadingHelper) {
			mIvLoadingHelper.startAnimation();
		}
	}

	private void stopAnimation() {
		if (null != mIvLoadingHelper) {
			mIvLoadingHelper.stopAnimation();
		}
	}
}
