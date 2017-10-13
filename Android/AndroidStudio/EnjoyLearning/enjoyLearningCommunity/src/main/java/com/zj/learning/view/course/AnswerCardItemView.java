package com.zj.learning.view.course;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.model.course.AnswerCardItem;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 作业、考试答题卡ItemView
 * 
 * @author XingRongJing
 * 
 */
public class AnswerCardItemView extends RelativeLayout implements IItemView {

	/** 是否答题报告结果页面 **/
	private boolean mIsAnswerResult = false;
	private TextView mTvCount;

	public AnswerCardItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public AnswerCardItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AnswerCardItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mTvCount = (TextView) this.findViewById(R.id.course_answer_tv_count);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub
		if (null == obj) {
			return;
		}
		ItemSingleAdapter<AnswerCardItemView, AnswerCardItem> adapter = (ItemSingleAdapter<AnswerCardItemView, AnswerCardItem>) obj;
		Object temp = adapter.getParams("isAnswerResult");
		if (null != temp) {
			mIsAnswerResult = (Boolean) temp;
		}
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		AnswerCardItem count = (AnswerCardItem) item;
		mTvCount.setText((count.getIndex() + 1) + "");
		mTvCount.setTag(count);

		if (mIsAnswerResult) {
			mTvCount.setOnClickListener(null);
			if (count.isRight()) {
				mTvCount.setBackgroundResource(R.drawable.course_answer_card_item_bg_no_answer_selector);
				mTvCount.setTextColor(this.getContext().getResources()
						.getColor(R.color.main_color));
			} else {
				mTvCount.setBackgroundResource(R.drawable.course_answer_card_item_bg_answer_result_normal);
				mTvCount.setTextColor(this.getContext().getResources()
						.getColor(android.R.color.white));
			}
		} else {
			if (count.isAnswerAll()) {
				mTvCount.setBackgroundResource(R.drawable.course_answer_card_item_bg_answer_selector);
				mTvCount.setTextColor(this.getContext().getResources()
						.getColor(android.R.color.white));
			} else {
				mTvCount.setBackgroundResource(R.drawable.course_answer_card_item_bg_no_answer_selector);
				mTvCount.setTextColor(this.getContext().getResources()
						.getColor(R.color.main_color));
			}
		}
	}

}
