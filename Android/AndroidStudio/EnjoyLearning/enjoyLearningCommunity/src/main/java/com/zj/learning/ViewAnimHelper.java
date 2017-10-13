package com.zj.learning;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class ViewAnimHelper {

	private Context mCtx;

	public ViewAnimHelper(Context ctx) {
		this.mCtx = ctx.getApplicationContext();
	}

	public void startAnimation(View view, int animId, int visibilty) {
		try {
			Animation anim = AnimationUtils.loadAnimation(mCtx, animId);
			anim.setDuration(800);
			anim.setAnimationListener(new ViewAnimListener(view, visibilty));
			view.startAnimation(anim);
		} catch (Exception e) {
		}
	}
}
