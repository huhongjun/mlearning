package com.zj.learning.control.course.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zj.learning.R;

/**
 * 完形填空题Fragment
 * 
 * @author XingRongJing
 * 
 */
public class CloseExamFragment extends BaseExamFragment {

	private LinearLayout mLlContainer;
	/** 用户输入的标记值 **/
	private Map<String, String> mInputTags;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mInputTags = new HashMap<String, String>();
	}

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
				llEt.findViewById(R.id.course_exam_short_answer_item_et_content)
						.setVisibility(View.GONE);
				ImageView iv = (ImageView) llEt
						.findViewById(R.id.course_question_short_answer_iv_divider);
				iv.setTag("ClostIv" + i);
				EditText et = new EditText(this.getActivity());
				et.setBackgroundResource(0);
				et.setPadding(0, 16, 0, 5);
				et.setTag("Close" + i);
				et.setTextColor(this.getResources().getColor(
						R.color.detail_black_color));
				// et.sethintCot(this.getResources().getColor(
				// R.color.detail_black_color));
				// et.setHintTextColor(this.getResources().getColor(
				// R.color.detail_gray_color));
				et.setTextSize(14);
				// et.setHint("您的答案" + (i + 1));
				// et.setHint(R.string.course_exam_et_hint);
				et.addTextChangedListener(new CustomTextWatcher(et));
				et.setOnFocusChangeListener(new CustomFocusListener(iv.getTag()
						.toString()));
				llEt.addView(et, 1);
				String content = mInputTags.get("Close" + i);
				if (!TextUtils.isEmpty(content)) {
					// 恢复保存值
					et.setText(content);
					et.setSelection(content.length());
				}
				mLlContainer.addView(llEt);
			}
		}
		return mFragmentView;
	}

	// @Override
	// public void onDestroyView() {
	// // TODO Auto-generated method stub
	// super.onDestroyView();
	// // 保存用户输入值
	// if (null == mLlContainer) {
	// return;
	// }
	// int count = mLlContainer.getChildCount();
	// for (int i = 0; i < count; i++) {
	// String tag = "Close" + i;
	// EditText et = (EditText) mLlContainer.findViewWithTag(tag);
	// if (null == et) {
	// continue;
	// }
	// mInputTags.put(tag, et.getText().toString().trim());
	// }
	//
	// }

	private boolean isAnswer() {
		if (null == mInputTags) {
			return false;
		}
		Set<String> keys = mInputTags.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = mInputTags.get(key);
			if (TextUtils.isEmpty(value)) {
				return false;
			}
		}
		return true;
	}

	private class CustomTextWatcher implements TextWatcher {

		private EditText mEtInput;

		public CustomTextWatcher(EditText et) {
			this.mEtInput = et;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			// TODO Auto-generated method stub
			if (null != mEtInput) {
				String tag = mEtInput.getTag().toString();
				if (TextUtils.isEmpty(tag) || null == mInputTags) {
					return;
				}
				// 保存用户输入值
				mInputTags.put(tag, s.toString());
				CloseExamFragment.this.notifyAnswerChanged(buildAnswers());
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}
	}

	private class CustomFocusListener implements OnFocusChangeListener {

		private String mTag;

		public CustomFocusListener(String tag) {
			this.mTag = tag;
		}

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// TODO Auto-generated method stub
			ImageView iv = (ImageView) mLlContainer.findViewWithTag(mTag);
			if (null == iv) {
				return;
			}
			if (hasFocus) {
				iv.setBackgroundColor(getResources().getColor(
						R.color.main_color));
			} else {
				iv.setBackgroundColor(getResources().getColor(
						R.color.divider_color));
			}
		}

	}

	@Override
	protected List<String> buildAnswers() {
		// TODO Auto-generated method stub
		if (null == mInputTags) {
			return null;
		}
		Set<String> keys = mInputTags.keySet();
		Iterator<String> it = keys.iterator();
		List<String> answers = new ArrayList<String>();
		while (it.hasNext()) {
			String key = it.next();
			String value = mInputTags.get(key);
			if (!TextUtils.isEmpty(value)) {
				answers.add(value);
			}
		}
		return answers;
	}
}
