package com.zj.learning.control.course.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.ActionBar.LayoutParams;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.zj.learning.R;

/**
 * 多选题Fragment
 * 
 * @author XingRongJing
 * 
 */
public class MulitpleChoiceExamFragment extends BaseExamFragment implements
		OnClickListener {

	private LinearLayout mLlContainer;
	/** 选中标记集 **/
	private Map<Integer, Boolean> mCheckTags;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCheckTags = new HashMap<Integer, Boolean>();
	}

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
		this.resetCheckState();
	}

	private boolean hasChecked() {
		if (null == mCheckTags || mCheckTags.isEmpty()) {
			return false;
		}
		Set<Integer> keys = mCheckTags.keySet();
		Iterator<Integer> it = keys.iterator();
		while (it.hasNext()) {
			boolean check = mCheckTags.get(it.next());
			if (check) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		TextView tv = (TextView) v
				.findViewById(R.id.course_question_single_choice_item_tv_left);
		int tag = Integer.valueOf(tv.getTag().toString());
		if (mCheckTags.containsKey(tag)) {
			if (mCheckTags.get(tag)) {
				mCheckTags.put(tag, false);
			} else {
				mCheckTags.put(tag, true);
			}
		} else {
			mCheckTags.put(tag, true);
		}
		this.resetCheckState();
		this.notifyAnswerChanged(this.buildAnswers());
//		this.setAnswerCardStateChanaged(hasChecked());
	}

	/**
	 * 重置选中与否状态
	 */
	private void resetCheckState() {
		if (null == mCheckTags || mCheckTags.isEmpty()) {
			return;
		}
		Set<Integer> keys = mCheckTags.keySet();
		Iterator<Integer> it = keys.iterator();
		while (it.hasNext()) {
			int tag = it.next();
			boolean checked = mCheckTags.get(tag);
			TextView tv = (TextView) mLlContainer.findViewWithTag(tag);
			if (checked) {
				this.onCheckedChanged(tv,
						R.drawable.course_exam_option_selected, this
								.getResources().getColor(android.R.color.white));
			} else {
				this.onCheckedChanged(tv, R.drawable.course_exam_option_normal,
						this.getResources().getColor(R.color.main_color));
			}
		}
	}

	private void onCheckedChanged(TextView tv, int resBg, int color) {
		if (null != tv) {
			tv.setBackgroundResource(resBg);
			tv.setTextColor(color);
		}
	}

	@Override
	protected List<String> buildAnswers() {
		// TODO Auto-generated method stub
		if (null == mCheckTags || mCheckTags.isEmpty()) {
			return null;
		}
		Set<Integer> keys = mCheckTags.keySet();
		Iterator<Integer> it = keys.iterator();
		String[] temps = this.getResources().getStringArray(
				R.array.exam_options_arr);
		List<String> answers = new ArrayList<String>();
		int length = temps.length;
		while (it.hasNext()) {
			int index = it.next();
			boolean check = mCheckTags.get(index);
			if(check&&index<length){
				String answer = temps[index];
				answers.add(answer);
			}
		}
		return answers;
	}
}
