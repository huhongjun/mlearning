package com.zj.learning.control.asks;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.zj.learning.R;

/**
 * PopupWindow助手类
 * 
 * @author XingRongJing
 * 
 */
public class PopupWindowHelper implements OnClickListener {

	private PopupWindow mPopWindow;

	public PopupWindowHelper(Context ctx, View view, int animationStyle) {
		this.prepare(ctx, view, animationStyle);
	}

	private void prepare(Context ctx, View v, int animationStyle) {
		v.setOnClickListener(this);
		mPopWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mPopWindow.setBackgroundDrawable(new ColorDrawable(ctx.getResources()
				.getColor(R.color.translucent)));
		mPopWindow.setFocusable(true);
		mPopWindow.setOutsideTouchable(false);
		mPopWindow.setAnimationStyle(animationStyle);
	}

	public void setOnDimissListener(OnDismissListener dismissListener) {
		if (null != mPopWindow) {
			mPopWindow.setOnDismissListener(dismissListener);
		}
	}

	public void showAsDropDown(View anchor) {
		if (null != mPopWindow) {
			mPopWindow.showAsDropDown(anchor, 0, 0);
		}
	}

	public void showAsDropDown(View anchor, int xoff, int yoff) {
		if (null != mPopWindow) {
			mPopWindow.showAsDropDown(anchor, xoff, yoff);
		}
	}

	public void showAtLocation(View parent, int gravity, int x, int y) {
		if (null != mPopWindow) {
			mPopWindow.showAtLocation(parent, gravity, x, y);
		}
	}

	public void dismiss() {
		if (null != mPopWindow) {
			mPopWindow.dismiss();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.dismiss();
	}
}
