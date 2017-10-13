package com.zj.learning.view.dynamic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.StringSplitter;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.control.dynamic.DynamicHelper;
import com.zj.learning.model.dynamic.Dynamic;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 动态ItemView
 * 
 * @author XingRongJing
 * 
 */
public class DynamicItemView extends LinearLayout implements IItemView {

	private ImageView mIvIcon;
	private TextView mTvContent, mTvTime;
	private Drawable mDIconUser;
	private Resources mRes;
	private FrameLayout mFlTest;

	public DynamicItemView(Context context) {
		this(context, null);
	}

	public DynamicItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (null == mDIconUser) {
			mDIconUser = this.getResources().getDrawable(
					R.drawable.dynamic_icon_user);
		}
		mRes = this.getResources();
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mIvIcon = (ImageView) this.findViewById(R.id.common_list_item_iv_icon);
		mTvContent = (TextView) this
				.findViewById(R.id.dynamic_list_item_tv_text);
		mTvTime = (TextView) this.findViewById(R.id.dynamic_list_item_tv_time);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		Dynamic dynamic = (Dynamic) item;
		int type = dynamic.getType();
		mIvIcon.setImageResource(DynamicHelper.getDynamicHelper()
				.getIconByType(type));
		CharSequence content = DynamicHelper.getDynamicHelper()
				.getContentByEventType(mRes, dynamic.getUsername(), type,
						dynamic.getEventType(), dynamic.getEventTitle());
		mTvContent.setText(content);
	}

}
