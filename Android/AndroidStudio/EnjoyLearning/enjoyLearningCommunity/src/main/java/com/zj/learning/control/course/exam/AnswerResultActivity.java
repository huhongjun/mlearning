package com.zj.learning.control.course.exam;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.model.course.AnswerCardItem;
import com.zj.learning.model.course.Resource;
import com.zj.learning.view.course.AnswerCardItemView;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;

/**
 * 考试、作业结果报告页面
 * 
 * @author XingRongJing
 * 
 */
public class AnswerResultActivity extends BaseActivity {

	/** 标题、得分、总分、答对题数、总题数、答题用时 **/
	private TextView mTvTitle, mTvScore, mTvScoreTotal, mTvRightCount,
			mTvTotalCount, mTvTime;
	private GridView mGridView;
	private Resource mResource;
	private BaseAdapter mAdapter;
	private List<AnswerCardItem> mCountList;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_exam_answer_result);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvScore = (TextView) this
				.findViewById(R.id.course_exam_answer_result_tv_score);
		mTvScoreTotal = (TextView) this
				.findViewById(R.id.course_exam_answer_result_tv_score_total);
		mTvRightCount = (TextView) this
				.findViewById(R.id.course_exam_answer_result_tv_answer_count);
		mTvTotalCount = (TextView) this
				.findViewById(R.id.course_exam_answer_result_tv_answer_count_total);
		mTvTime = (TextView) this
				.findViewById(R.id.course_exam_answer_result_tv_time);
		mGridView = (GridView) this
				.findViewById(R.id.course_exam_answer_result_gridview);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		Intent intent = this.getIntent();
		String res = intent.getStringExtra("resource");
		if (!TextUtils.isEmpty(res)) {
			mResource = Resource.toObj(res);
			mTvTitle.setText(mResource.getResName());
		}
		mTvScore.setText(intent.getFloatExtra("score", 0.0f) + "");
		mTvScoreTotal.setText(GlobalConfig.SEPERATOR
				+ intent.getFloatExtra("totalScore", 0.0f) + "分");
		mTvRightCount.setText(intent.getIntExtra("rightCount", 0) + "");
		mTvTotalCount.setText(GlobalConfig.SEPERATOR
				+ intent.getIntExtra("totalCount", 0) + "道");
		long time = intent.getLongExtra("time", 0);
		mTvTime.setText(this.getTimesBySeconds(time) + "");

		mCountList = intent.getParcelableArrayListExtra("answerCounts");
		mAdapter = new ItemSingleAdapter<AnswerCardItemView, AnswerCardItem>(
				this, mCountList);
		((ItemSingleAdapter<AnswerCardItemView, AnswerCardItem>) mAdapter)
				.addParams("isAnswerResult", true);
		mGridView.setAdapter(mAdapter);
	}

	public void onClickAnalyze(View view) {// 题目解析

	}

	/**
	 * 根据秒返回分钟数（多算）
	 * 
	 * @param timeSeconds
	 * @return
	 */
	private long getTimesBySeconds(long timeSeconds) {
		long temp = timeSeconds % 60;
		long minutes = (timeSeconds / 60);
		if (temp != 0) {
			minutes++;
		}
		return minutes;
	}

}
