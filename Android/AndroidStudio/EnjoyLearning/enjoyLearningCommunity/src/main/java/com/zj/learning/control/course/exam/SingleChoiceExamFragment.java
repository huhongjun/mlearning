package com.zj.learning.control.course.exam;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;

/**
 * 单选题Fragment
 * 
 * @author XingRongJing
 * 
 */
public class SingleChoiceExamFragment extends BaseExamFragment implements
		OnClickListener {

	private LinearLayout mLlContainer;
	/** 标记选中项Tag **/
	private int mCheckedIndex = GlobalConfig.INVALID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mFragmentView = this.buildContentView(inflater,
				R.layout.course_question_common);
		mLlContainer = (LinearLayout) mFragmentView
				.findViewById(R.id.course_exam_common_ll_container);
		List<String> options = mQuestion.getOptions();
		if (null != options) {
			int size = options.size();
			String[] temps = this.getResources().getStringArray(
					R.array.exam_options_arr);
			for (int i = 0; i < size; i++) {
				String option = options.get(i);
				LinearLayout ll = (LinearLayout) inflater.inflate(
						R.layout.course_question_single_choice_item, null);
				ll.setOnClickListener(this);
				TextView tv = (TextView) ll
						.findViewById(R.id.course_question_single_choice_item_tv_left);
				if (i < temps.length) {
					tv.setText(temps[i]);
				}
				tv.setTag(i);
				TextView tvContent = (TextView) ll
						.findViewById(R.id.course_question_single_choice_item_tv_content);
				tvContent.setText(option);
				mLlContainer.addView(ll, LayoutParams.MATCH_PARENT,
						LayoutParams.WRAP_CONTENT);
			}
		}
		return mFragmentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		// 恢复标记值
		if (null != mLlContainer && this.isChecked()) {
			TextView tv = (TextView) mLlContainer
					.findViewWithTag(mCheckedIndex);
			this.onCheckedChanged(tv, R.drawable.course_exam_option_selected,
					this.getResources().getColor(android.R.color.white));

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) v
				.findViewById(R.id.course_question_single_choice_item_tv_left);
		int tag = Integer.valueOf(tv.getTag().toString());
		if (this.isChecked()) {// 清除上个选中状态
			TextView lastTv = (TextView) mLlContainer
					.findViewWithTag(mCheckedIndex);
			this.onCheckedChanged(lastTv, R.drawable.course_exam_option_normal,
					this.getResources().getColor(R.color.main_color));
		}
		mCheckedIndex = tag;
		// 标记上选中
		this.onCheckedChanged(tv, R.drawable.course_exam_option_selected, this
				.getResources().getColor(android.R.color.white));
		// 标记已回答
//		this.setAnswerCardStateChanaged(true);
		this.notifyAnswerChanged(this.buildAnswers());
		
		this.scrollNextPage();
	}

	private void onCheckedChanged(TextView tv, int resBg, int color) {
		if (null != tv) {
			tv.setBackgroundResource(resBg);
			tv.setTextColor(color);
		}
	}

	private boolean isChecked() {
		return (GlobalConfig.INVALID != mCheckedIndex);
	}
	
	@Override
	protected List<String> buildAnswers() {
		// TODO Auto-generated method stub
		if (this.isChecked()) {
			List<String> answers = new ArrayList<String>();
			String[] temps = this.getResources().getStringArray(
					R.array.exam_options_arr);
			int length = temps.length;
			for (int i = 0; i < length; i++) {
				if (mCheckedIndex == i) {
					answers.add(temps[i]);
				}
			}
			return answers;
		} else {
			return null;
		}
	}

	 

}
