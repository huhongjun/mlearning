package com.zj.learning.control.course.exam;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.course.ResourceDetailActivity;
import com.zj.learning.model.course.Question;

/**
 * 考题/作业Fragment试题基类（暂定一题一个Fragment）
 * 
 * @author XingRongJing
 * 
 */
public abstract class BaseExamFragment extends Fragment {

	protected TextView mTvCount, mTvQuestion, mTvTitle;
	protected Question mQuestion;
	// private List<String> mAnswers;
	private String mTitle;
	private int mIndex = -1;

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		if (null != args) {
			String temp = args.getString("examItem");
			mIndex = args.getInt("index");
			mTitle = args.getString("examTitle");
			mQuestion = Question.toObj(temp);
		}
		if (null == mQuestion) {
			throw new IllegalArgumentException("Question is null");
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		if (null != mQuestion) {
			this.setQueestion(mQuestion.getTitle());
		}
		this.setTitle(mTitle);

	}

	// @Override
	// public void onSaveInstanceState(Bundle outState) {
	// // TODO Auto-generated method stub
	// super.onSaveInstanceState(outState);
	// System.out.println(" onSaveInstanceState（）" + this.toString());
	// }
	//
	// @Override
	// public void onViewCreated(View view, Bundle savedInstanceState) {
	// // TODO Auto-generated method stub
	// super.onViewCreated(view, savedInstanceState);
	// // System.out.println(" onViewCreated（）" + this.toString());
	// // System.out.println(" onViewCreated() "
	// // + (null == savedInstanceState ? " null" : savedInstanceState
	// // .toString()));
	// if (null != mQuestion) {
	// // savedInstanceState.putString("examItem", mQuestion.toJson());
	// }
	// EditText et = (EditText) mFragmentView
	// .findViewById(R.id.course_exam_short_answer_et);
	// if (null != et) {
	// et.setText(test);
	// }
	// }
	//
	// @Override
	// public void onDestroyView() {
	// // TODO Auto-generated method stub
	// super.onDestroyView();
	// int type = mQuestion.getType();
	// switch (type) {
	// case ExamItem.TYPE_SINGLE_CHOICE:
	// RadioGroup group = (RadioGroup) mFragmentView
	// .findViewById(R.id.course_exam_single_choice_rg_choice);
	// int id = group.getCheckedRadioButtonId();
	// System.out.println(" id = "+id);
	// break;
	// case ExamItem.TYPE_SHORT_ANSWER:
	// EditText et = (EditText) mFragmentView
	// .findViewById(R.id.course_exam_short_answer_et);
	// if (null != et) {
	// test = et.getText().toString();
	// }
	// break;
	// }
	// // System.out.println(" onDestroyView（）" + this.toString());
	// }

	protected View buildContentView(LayoutInflater inflater, int resId) {
		View view = inflater.inflate(resId, null);
		mTvQuestion = (TextView) view
				.findViewById(R.id.course_exam_tv_question);
		mTvTitle = (TextView) view.findViewById(R.id.course_exam_tv_title);
		mTvCount = (TextView) view.findViewById(R.id.course_exam_tv_count);
		return view;
	}

	private void setQueestion(String question) {
		if (null != mTvQuestion) {
			mTvQuestion.setText(question);
		}
	}

	private void setTitle(String title) {
		if (null != mTvTitle) {
			mTvTitle.setText(title);
		}
	}

	public void setExamCount(String count) {
		if (null != mTvCount) {
			mTvCount.setText(count);
		}
	}

	/**
	 * 构建答案集-子类实现
	 * 
	 * @return
	 */
	protected abstract List<String> buildAnswers();

	protected void scrollNextPage() {
		((ResourceDetailActivity) this.getActivity()).scrollNextPage(mIndex);

	}

	/**
	 * 设置此题答案
	 * 
	 * @param answers
	 */
	protected void notifyAnswerChanged(List<String> answers) {
		((ResourceDetailActivity) this.getActivity()).setAnswers(mIndex,
				answers);

	}

}
