package com.zj.learning.view.asks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.control.asks.AsksHelper;
import com.zj.learning.model.asks.Answer;
import com.zj.support.util.TimeUtil;
import com.zj.support.widget.ZjImageView;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 回答列表ItemView
 * 
 */
public class AnswerItemView extends RelativeLayout implements IItemView,
		OnClickListener {

	private ZjImageView mIvAvator, mIvThumb;
	private TextView mTvUsername, mTvDesc, mTvTime, mTvCommentCount;
	private AsksHelper mAsksHelper;

	public AnswerItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AnswerItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AnswerItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mIvAvator = (ZjImageView) this.findViewById(R.id.user_show_iv_avator);
		mIvThumb = (ZjImageView) this.findViewById(R.id.asks_content_iv_thumb);
		mTvUsername = (TextView) this.findViewById(R.id.user_show_tv_name);
		mTvDesc = (TextView) this.findViewById(R.id.asks_content_tv_desc);
		mTvTime = (TextView) this.findViewById(R.id.user_show_tv_time);
		mTvCommentCount = (TextView) this
				.findViewById(R.id.asks_content_tv_comment_count);
		mTvCommentCount.setVisibility(View.VISIBLE);
		mTvDesc.setMaxLines(100);
		mIvAvator.setErrorImageResId(R.drawable.avator_default);
		mIvAvator.setDefaultImageResId(R.drawable.avator_default);
		mIvThumb.setErrorImageResId(R.drawable.asks_thumb_default);
		mIvThumb.setDefaultImageResId(R.drawable.asks_thumb_default);
	}

	@Override
	public void prepareItemProperty(Object obj) {// listview
		// TODO Auto-generated method stub
		@SuppressWarnings("unchecked")
		ItemSingleAdapter<AnswerItemView, Answer> adapter = (ItemSingleAdapter<AnswerItemView, Answer>) obj;
		if (null == mAsksHelper) {
			mAsksHelper = (AsksHelper) adapter.getParams("asksHelper");
		}
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		Answer answer = (Answer) item;
		mTvDesc.setText(answer.getAnswerContent());
		mTvTime.setText(TimeUtil.getTimeTips(this.getContext(),
				answer.getAddTime()));
		// mAsksHelper.handleAvator(mIvAvator, answer.getUserId());
		mTvUsername.setText(answer.getUserName());
		mAsksHelper.handleThumb(answer.getThumb(), mIvThumb, this);
		mTvCommentCount.setText(mAsksHelper.getCommentCountTextSpan(
				this.getContext(), answer.getCommentCount()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.asks_content_iv_thumb:
			mAsksHelper.onClickThumb(v);
			break;
		}
	}

}
