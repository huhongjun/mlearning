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
import com.zj.learning.model.asks.Comment;
import com.zj.support.util.TimeUtil;
import com.zj.support.widget.ZjImageView;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 回答评论列表ItemView
 * 
 */
public class CommentItemView extends RelativeLayout implements IItemView {

	private ZjImageView mIvAvator;
	private TextView mTvUsername, mTvDesc, mTvTime;

	public CommentItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CommentItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CommentItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mIvAvator = (ZjImageView) this.findViewById(R.id.user_show_iv_avator);
		mTvUsername = (TextView) this.findViewById(R.id.user_show_tv_name);
		mTvDesc = (TextView) this.findViewById(R.id.asks_comment_content);
		mTvTime = (TextView) this.findViewById(R.id.user_show_tv_time);
		mTvDesc.setMaxLines(100);
		mIvAvator.setErrorImageResId(R.drawable.avator_default);
		mIvAvator.setDefaultImageResId(R.drawable.avator_default);
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		Comment comment = (Comment) item;
		mTvDesc.setText(comment.getMessage());
		mTvTime.setText(TimeUtil.getTimeTips(this.getContext(),
				comment.getAddTime()));
		// mAsksHelper.handleAvator(mIvAvator, answer.getUserId());
		mTvUsername.setText(comment.getUserName());
		// mAsksHelper.handleThumb(comment.getThumb(), mIvThumb, this);
		// mTvCommentCount.setText(mAsksHelper.getCommentCountTextSpan(
		// this.getContext(), comment.getCommentCount()));
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub
		
	}

}
