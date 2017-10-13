package com.zj.learning.view.user;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.model.user.Certificate;
import com.zj.support.widget.ZjImageView;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 课程列表ItemView
 * 
 * @author XingRongJing
 * 
 */
public class CertificateItemView extends RelativeLayout implements IItemView {

	private ZjImageView mIvThumb;
	private TextView mTvName, mTvApply, mTvCondition;

	public CertificateItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CertificateItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CertificateItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mIvThumb = (ZjImageView) this
				.findViewById(R.id.certificate_list_item_iv_show);
		mTvName = (TextView) this
				.findViewById(R.id.certificate_list_item_tv_name);
		mTvApply = (TextView) this
				.findViewById(R.id.certificate_list_item_tv_apply);
		mTvCondition = (TextView) this
				.findViewById(R.id.certificate_list_item_tv_condition);
		// mIvAvator.setErrorImageResId(R.drawable.asks_avator_default);
		// mIvAvator.setDefaultImageResId(R.drawable.asks_avator_default);
		// mIvThumb.setErrorImageResId(R.drawable.asks_thumb_default);
		// mIvThumb.setDefaultImageResId(R.drawable.asks_thumb_default);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		Certificate certificate = (Certificate) item;
		mTvName.setText(certificate.getTitle());
		mTvCondition.setText(certificate.getCondition());
	}

}
