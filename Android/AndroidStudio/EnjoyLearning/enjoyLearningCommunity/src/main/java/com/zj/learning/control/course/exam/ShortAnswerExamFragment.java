package com.zj.learning.control.course.exam;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zj.learning.R;

/**
 * 简答题Fragment-暂时不考虑
 * 
 * @author XingRongJing
 * 
 */
public class ShortAnswerExamFragment extends BaseExamFragment implements
		TextWatcher, OnFocusChangeListener {

	private LinearLayout mLlContainer;
	/** 用户输入的内容-做个标记 **/
	private String mInput;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mFragmentView = this.buildContentView(inflater,
				R.layout.course_question_common);
		mLlContainer = (LinearLayout) mFragmentView
				.findViewById(R.id.course_exam_common_ll_container);
		List<String> answers = mQuestion.getAnswers();
		if (null != answers) {
			int size = answers.size();
			for (int i = 0; i < size; i++) {
				LinearLayout llEt = (LinearLayout) inflater.inflate(
						R.layout.course_question_short_answer_item, null);
				EditText et = (EditText) llEt
						.findViewById(R.id.course_exam_short_answer_item_et_content);
				et.addTextChangedListener(this);
				et.setOnFocusChangeListener(this);
				et.setTag("ShortAnswer" + i);
				// 恢复用户输入内容
				if (!TextUtils.isEmpty(mInput)) {
					et.setText(mInput);
					et.setSelection(mInput.length());
				}
				mLlContainer.addView(llEt);
			}
		}
		return mFragmentView;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		// 保存用户输入值
		mInput = this.getInput();
	}

	private String getInput() {
		if (null == mLlContainer) {
			return null;
		}
		EditText et = (EditText) mLlContainer
				.findViewById(R.id.course_exam_short_answer_item_et_content);
		if (null == et) {
			return null;
		}
		return et.getText().toString().trim();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		mInput = s.toString();
		this.notifyAnswerChanged(this.buildAnswers());
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		// TODO Auto-generated method stub
		ImageView iv = (ImageView) mLlContainer
				.findViewById(R.id.course_question_short_answer_iv_divider);
		if (null == iv) {
			return;
		}
		if (hasFocus) {
			iv.setBackgroundColor(this.getResources().getColor(
					R.color.main_color));
		} else {
			iv.setBackgroundColor(this.getResources().getColor(
					R.color.divider_color));
		}
	}

	@Override
	protected List<String> buildAnswers() {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(mInput)) {
			return null;
		}
		List<String> answers = new ArrayList<String>();
		answers.add(mInput);
		return answers;
	}
}
