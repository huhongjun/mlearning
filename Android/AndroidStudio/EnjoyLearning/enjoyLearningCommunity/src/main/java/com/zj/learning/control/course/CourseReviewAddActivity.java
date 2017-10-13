package com.zj.learning.control.course;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.model.course.Course;
import com.zj.learning.model.course.CourseReview;
import com.zj.learning.model.user.User;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.TimeUtil;

/**
 * 添加课程评论
 * 
 * @author XingRongJing
 * 
 */
public class CourseReviewAddActivity extends BaseActivity {

	private TextView mTvTitle, mTvReviewCommit;
	private EditText mEtContent;
	private RatingBar mRBarKnowledge, mRBarFunny, mRBarDesign;
	private RadioGroup mRGroupStatus;
	private LinearLayout mLlProgress;
	private CourseReview mReview;
	private Course mCourse;

	// private boolean mSuccess = false;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_review_add);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvReviewCommit = (TextView) this
				.findViewById(R.id.titlebar_back_tv_right);
		mEtContent = (EditText) this
				.findViewById(R.id.course_review_add_et_content);
		mRGroupStatus = (RadioGroup) this
				.findViewById(R.id.course_review_add_rgroup_status);
		mRBarKnowledge = (RatingBar) this
				.findViewById(R.id.course_review_add_rbar_knowledge);
		mRBarFunny = (RatingBar) this
				.findViewById(R.id.course_review_add_rbar_funny);
		mRBarDesign = (RatingBar) this
				.findViewById(R.id.course_review_add_rbar_design);
		mLlProgress = (LinearLayout) this
				.findViewById(R.id.course_review_add_ll_loading);
		this.buildLoading(mLlProgress);

		mTvReviewCommit.setVisibility(View.VISIBLE);
		mTvReviewCommit.setText(this.getString(R.string.asks_commit));

	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		String jsonCourse = "";
		if (null == savedInstanceState) {
			jsonCourse = this.getIntent().getStringExtra("course");
		} else {// 恢复course数据
			jsonCourse = savedInstanceState.getString("course");
		}
		mCourse = Course.toObj(jsonCourse);
		mTvTitle.setText(mCourse.getResName());

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (null != mCourse) {// 保存course数据
			outState.putString("course", mCourse.toJson());
		}
	}

	public void onClickRightBtn(View view) {// 提交
		String content = mEtContent.getText().toString().trim();
		int knowledge = mRBarKnowledge.getProgress() * 2;
		int funny = mRBarFunny.getProgress() * 2;
		int design = mRBarDesign.getProgress() * 2;
		int checkId = mRGroupStatus.getCheckedRadioButtonId();
		String tips = this.validate(content, knowledge, funny, design, checkId);
		if (!TextUtils.isEmpty(tips)) {
			this.showToast(tips);
			return;
		}
		if (null == mReview) {
			mReview = new CourseReview();
			mReview.setCourseId(mCourse.getCourseId());
		}
		mReview.setContent(content);
		mReview.setFlag0(knowledge);
		mReview.setFlag1(funny);
		mReview.setFlag2(design);
		mReview.setStuState(this.getStatusByCheckId(checkId));
		User user = this.getUser();
		if (null != user) {
			mReview.setUserId(user.getUserId());
			mReview.setUserName(user.getLoginName());
		}

		Map<String, String> params = new HashMap<String, String>();
		params.put("content", this.buildContent(mReview));
		this.getHttpApi().request(GlobalConfig.URL_COURSE_ADD_COMMENT, params,
				this, new RequestTag(mReqTag));
		this.showLoading();
	}

	private String validate(String content, int knowledge, int funny,
			int design, int checkId) {
		if (knowledge <= 0) {
			return this.getString(R.string.course_review_score_knowledge_limit);
		}
		if (funny <= 0) {
			return this.getString(R.string.course_review_score_funny_limit);
		}
		if (design <= 0) {
			return this.getString(R.string.course_review_score_design_limit);
		}
		if (checkId == -1) {
			return this.getString(R.string.course_review_status_limit);
		}
		if (TextUtils.isEmpty(content)) {
			return this.getString(R.string.no_empty);
		}
		int length = content.length();
		if (length < GlobalConfig.Limit.MIN_LENGTH_COURSE_REVIEW
				|| length > GlobalConfig.Limit.MAX_LENGTH_COURSE_REVIEW) {
			return this.getString(R.string.course_review_et_length_limit);
		}
		return null;
	}

	private String buildContent(CourseReview review) {
		JSONObject json = new JSONObject();
		try {
			json.put("courseId", review.getCourseId());
			json.put("flag0", review.getFlag0());
			json.put("flag1", review.getFlag1());
			json.put("flag2", review.getFlag2());
			json.put("stuState", review.getStuState());
			json.put("userId", review.getUserId());
			json.put("content", review.getContent());
			json.put("userName", review.getUserName());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
	}

	private int getStatusByCheckId(int checkId) {
		switch (checkId) {
		case R.id.course_review_add_rBtn_status_ing:
			return CourseReview.STATUS_LEARN_ING;
		case R.id.course_review_add_rBtn_status_complete:
			return CourseReview.STATUS_LEARN_COMPLETE;
		case R.id.course_review_add_rBtn_status_giveup:
			return CourseReview.STATUS_LEARN_GIVEUP;
		}
		return -1;
	}

	@Override
	protected void setResultBack() {
		if (null != mReview) {
			Intent intent = new Intent();
			intent.putExtra("CourseReview", mReview.toJson());
			this.resetCourseReviewsData(mCourse, mReview);
			intent.putExtra("course", mCourse.toJson());
			this.setResult(RESULT_OK, intent);
		}
	}

	private void resetCourseReviewsData(Course course, CourseReview review) {
		int oldCount = course.getCommentUserNum();
		int newCount = oldCount + 1;
		float avgKnowledge = ((course.getAvg0() * oldCount) + review.getFlag0())
				/ newCount;
		float avgFunny = ((course.getAvg1() * oldCount) + review.getFlag1())
				/ newCount;
		float avgDesign = ((course.getAvg2() * oldCount) + review.getFlag2())
				/ newCount;
		float avgScoreReview = (review.getFlag0() + review.getFlag1() + review
				.getFlag2()) / 3;
		float avgScore = ((course.getAvgScore() * oldCount) + avgScoreReview)
				/ newCount;
		course.setAvg0(avgKnowledge);
		course.setAvg1(avgFunny);
		course.setAvg2(avgDesign);
		course.setAvgScore(avgScore);
		course.setCommentUserNum(newCount);
	}

	private void onFails() {
		this.hideLoading();
		this.showToast(this.getString(R.string.request_fail));
		mReview = null;
	}

	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		this.hideLoading();
		if (null == arg0) {
			return;
		}
		// 添加成功时
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			if (null != mReview) {
				int id = parser.getId();
				mReview.setCommentTime(TimeUtil.getCurrentTimeBySeconds());
				mReview.setCcId(id);
			}
			this.setResultBack();
			this.finish();
		} else {
			this.onFails();
		}
	}

	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseError(out);
		this.onFails();
	}

}
