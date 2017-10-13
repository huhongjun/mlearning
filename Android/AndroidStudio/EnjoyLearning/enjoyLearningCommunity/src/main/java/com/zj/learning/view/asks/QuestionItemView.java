package com.zj.learning.view.asks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.asks.AsksAddActivity;
import com.zj.learning.control.asks.AsksHelper;
import com.zj.learning.control.asks.AsksListFragment;
import com.zj.learning.model.asks.Question;
import com.zj.support.util.TimeUtil;
import com.zj.support.widget.ZjImageView;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 问题列表ItemView
 * 
 * @author XingRongJing
 * 
 */
public class QuestionItemView extends RelativeLayout implements IItemView,
		OnClickListener {

	private ZjImageView mIvAvator, mIvThumb;
	private TextView mTvUsername, mTvDesc, mTvTime, mTvReply, mTvReplyCount,
			mTvReplyCountMe;
	private LinearLayout mLlBottom;
	private Fragment mFragment;
	private AsksHelper mAsksHelper;

	public QuestionItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public QuestionItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public QuestionItemView(Context context) {
		super(context);
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mIvAvator = (ZjImageView) this.findViewById(R.id.user_show_iv_avator);
		mIvThumb = (ZjImageView) this.findViewById(R.id.asks_content_iv_thumb);
		mTvUsername = (TextView) this.findViewById(R.id.user_show_tv_name);
		mTvDesc = (TextView) this.findViewById(R.id.asks_content_tv_desc);
		mTvTime = (TextView) this.findViewById(R.id.user_show_tv_time);
		mTvReply = (TextView) this.findViewById(R.id.asks_list_item_tv_reply);
		mTvReplyCount = (TextView) this
				.findViewById(R.id.asks_list_item_tv_reply_counts);
		mLlBottom = (LinearLayout) this
				.findViewById(R.id.asks_list_item_rl_bottom);
		mIvAvator.setErrorImageResId(R.drawable.avator_default);
		mIvAvator.setDefaultImageResId(R.drawable.avator_default);
		mIvThumb.setErrorImageResId(R.drawable.asks_thumb_default);
		mIvThumb.setDefaultImageResId(R.drawable.asks_thumb_default);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub
		if (null == obj) {
			return;
		}
		@SuppressWarnings("unchecked")
		ItemSingleAdapter<QuestionItemView, Question> adapter = (ItemSingleAdapter<QuestionItemView, Question>) obj;
		if (null == mFragment) {
			mFragment = (Fragment) adapter.getParams("fragment");
		}
		if (null == mAsksHelper) {
			mAsksHelper = (AsksHelper) adapter.getParams("asksHelper");
		}
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		Question question = (Question) item;
		int operator = question.getOperator();
		if (Question.OPERATOR_ME == operator) {
			mLlBottom.setVisibility(View.GONE);
			mTvReplyCountMe.setVisibility(View.VISIBLE);
			// mTvReplyCountMe.setText(mAsksHelper.getAnswerCountTextSpan(
			// this.getContext(), question.getAnswersNum()));
		} else {
			mLlBottom.setVisibility(View.VISIBLE);
			// mTvReplyCountMe.setVisibility(View.GONE);
			mTvReply.setOnClickListener(this);
			mTvReply.setTag(question);
			mTvReplyCount.setText("已回答(" + question.getAnswerCount() + ")");
		}
		mTvTime.setText(TimeUtil.getTimeTips(this.getContext(),
				question.getAddTime()));
		mTvUsername.setText(question.getUserName());
		mTvDesc.setText(question.getQuestionContent());
		String imageUrl = GlobalConfig.URL_PIC_ASKS_THUMB
				+ GlobalConfig.SEPERATOR + question.getQuestionId();
		mIvThumb.setImageUrl(imageUrl);	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.asks_content_iv_thumb:// 查看大图
			mAsksHelper.onClickThumb(v);
			break;
		case R.id.asks_list_item_tv_reply:// 立即回复
			Question question = (Question) v.getTag();
			Intent intent2 = new Intent(this.getContext(),
					AsksAddActivity.class);
			intent2.putExtra("question", question.toJson());
			mFragment.startActivity(intent2);
			break;
		}
	}

}
