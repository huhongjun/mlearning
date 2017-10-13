package com.zj.app;

import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * 将一张图片旋转形成加载中的动画助手
 * 
 * @author XingRongJing
 * 
 */
public class ImageLoadingHelper {

	private ImageView mIvLoading;
	static final int ROTATION_ANIMATION_DURATION = 1200;
	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

	private final Animation mRotateAnimation;
	private final Matrix mHeaderImageMatrix;

	public ImageLoadingHelper(ImageView iv) {
		mIvLoading = iv;
		mIvLoading.setScaleType(ScaleType.MATRIX);
		mHeaderImageMatrix = new Matrix();
		mIvLoading.setImageMatrix(mHeaderImageMatrix);

		mRotateAnimation = new RotateAnimation(0, 720,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(ROTATION_ANIMATION_DURATION);
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
		mRotateAnimation.setRepeatMode(Animation.RESTART);
	}

	public void startAnimation() {
		mIvLoading.startAnimation(mRotateAnimation);
	}

	public void stopAnimation() {
		mIvLoading.clearAnimation();
		this.resetImageRotation();
	}

	private void resetImageRotation() {
		if (null != mHeaderImageMatrix) {
			mHeaderImageMatrix.reset();
			mIvLoading.setImageMatrix(mHeaderImageMatrix);
		}
	}
}
