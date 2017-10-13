package com.zj.learning.control.course;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zj.app.ImageLoadingHelper;
import com.zj.learning.CourseReviewHelper;
import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.LoginActivity;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.dao.CoreSharedPreference;
import com.zj.learning.model.course.Course;
import com.zj.learning.model.course.CourseReview;
import com.zj.learning.model.course.Resource;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.CommonUtil;
import com.zj.support.widget.ZjImageView;

/**
 * 课程详情
 * 
 * @author XingRongJing
 * 
 */
public class CourseDetailActivity extends BaseActivity {

	private static final int CODE_REQ_REVIEW_ADD = 1;
	private static final int CODE_REQ_REVIEW_MORE = 2;
	private static final int CODE_REQ_LOGIN_REVIEW_ADD = 3;
	private static final int CODE_REQ_LOGIN_JOIN_CLASS = 4;
	private static final int CODE_REQ_LOGIN_FOCUS = 5;
	private ScrollView mSvContent;
	private RelativeLayout mRlTitlebar, mRlBottom;
	private LinearLayout mLlLoading;
	private RelativeLayout mRlReviewItemView;
	private ZjImageView mIvCover;
	private TextView mTvName, mTvScoreTotal, mTvScoreKnowledge, mTvScoreFunny,
			mTvScoreDesign, mTvHomework, mTvExam, mTvDesc, mTvLearnCount,
			mTvFocusCount, mTvReviewTips, mTvReviewEmptyTips, mTvAsksTips,
			mTvBottomGoClass;
	private RatingBar mRatingBarScore;
	private ImageView mIvDownload, mIvReviewLoading, mIvJoinClass;
	private Button mBtnReviewMore;
	private Course mCourse;
	private CourseReviewHelper mReviewHelper;
	/** 评论获取加载助手 **/
	private ImageLoadingHelper mIvReviewLoadingHelper;
	private ImageLoadingHelper mIvJoinClassHelper;
	private boolean mIsFocusSuccess = false, mIsLearned = false,
			mIsReviewAdd = false;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_detail);
		mIvDownload = (ImageView) this
				.findViewById(R.id.course_detail_iv_download);
		mBtnReviewMore = (Button) this
				.findViewById(R.id.course_detail_btn_review_more);
		mSvContent = (ScrollView) this
				.findViewById(R.id.course_detail_sv_content);
		mIvCover = (ZjImageView) this.findViewById(R.id.course_detail_iv_cover);
		// mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mLlLoading = (LinearLayout) this
				.findViewById(R.id.course_detail_ll_loading);
		mIvReviewLoading = (ImageView) this
				.findViewById(R.id.course_iv_review_loading);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mRlTitlebar = (RelativeLayout) this
				.findViewById(R.id.course_detail_rl_titlebar);
		mTvName = (TextView) this.findViewById(R.id.course_detail_tv_name);
		mTvScoreTotal = (TextView) this
				.findViewById(R.id.course_detail_tv_score_total);
		mTvScoreKnowledge = (TextView) this
				.findViewById(R.id.course_detail_tv_review_knowleage);
		mTvScoreFunny = (TextView) this
				.findViewById(R.id.course_detail_tv_review_funny);
		mTvScoreDesign = (TextView) this
				.findViewById(R.id.course_detail_tv_review_design);
		mTvDesc = (TextView) this.findViewById(R.id.course_detail_tv_desc);
		mTvHomework = (TextView) this
				.findViewById(R.id.course_detail_tv_homework);
		mTvExam = (TextView) this.findViewById(R.id.course_detail_tv_exam);
		mTvLearnCount = (TextView) this
				.findViewById(R.id.course_detail_tv_learn);
		mTvFocusCount = (TextView) this
				.findViewById(R.id.course_detail_tv_focus);
		mTvReviewTips = (TextView) this
				.findViewById(R.id.course_detail_tv_review_tips);
		mTvReviewEmptyTips = (TextView) this
				.findViewById(R.id.course_detail_tv_review_empty);
		mTvAsksTips = (TextView) this
				.findViewById(R.id.course_detail_tv_asks_tips);

		mRatingBarScore = (RatingBar) this
				.findViewById(R.id.course_detail_ratingbar);

		mRlReviewItemView = (RelativeLayout) this
				.findViewById(R.id.course_detail_rl_review_item);
		mRlBottom = (RelativeLayout) this
				.findViewById(R.id.course_detail_rl_bottom);

		mTvBottomGoClass = (TextView) this
				.findViewById(R.id.course_detail_tv_go);
		mIvJoinClass = (ImageView) this
				.findViewById(R.id.course_detail_iv_join_class);
		this.buildLoading(mLlLoading);

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
		this.bindValue();

		mIvReviewLoadingHelper = new ImageLoadingHelper(mIvReviewLoading);
		mIvJoinClassHelper = new ImageLoadingHelper(mIvJoinClass);
		mReviewHelper = new CourseReviewHelper(mRlReviewItemView);

		if (this.isNetworkEnable()) {
			// 获取一条评论
			this.fetchReview();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (null != outState && null != mCourse) {// 保存course数据
			outState.putString("course", mCourse.toJson());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case CODE_REQ_REVIEW_ADD:// 添加评论
			this.onActivityResultReviewAdd(data);
			break;
		case CODE_REQ_REVIEW_MORE:// 更多评论有新增时
			this.onActivityResultReviewAdd(data);
			break;
		case CODE_REQ_LOGIN_REVIEW_ADD:// 登录成功-点评
			if (this.getUser().isMyCourse(mCourse.getCourseId())) {
				this.startReviewAdd();
			} else {
				this.showToast(this.getString(R.string.course_not_my_course));
			}
			break;
		case CODE_REQ_LOGIN_JOIN_CLASS:// 登录成功-参加课程
			// 如果已登录且是我的课程
			if (this.isLogin()
					&& this.getUser().isMyCourse(mCourse.getCourseId())) {
				mTvBottomGoClass.setText(this
						.getString(R.string.course_go_class));
			} else {
				mTvBottomGoClass.setText(this
						.getString(R.string.course_go_class_before));
			}
			break;
		case CODE_REQ_LOGIN_FOCUS:// 登录成功-关注
			break;
		}
	}

	@Override
	protected void setResultBack() {
		if (!mIsFocusSuccess && !mIsLearned && !mIsReviewAdd) {
			return;
		}
		Intent data = new Intent();
		data.putExtra("courseId", mCourse.getCourseId());
		data.putExtra("isFocus", mIsFocusSuccess);
		data.putExtra("isLearn", mIsLearned);
		if (mIsReviewAdd) {
			data.putExtra("avgScore", mCourse.getAvgScore());
		}
		this.setResult(RESULT_OK, data);

	}

	private void bindValue() {
		String imageUrl = GlobalConfig.URL_PIC_COURSE_THUMB
				+ GlobalConfig.SEPERATOR + mCourse.getCourseId();
		mIvCover.setImageUrl(imageUrl);
		String courseName = mCourse.getResName();
		mTvName.setText(courseName);
		// mTvTitle.setText(courseName);
		mTvDesc.setText(mCourse.getDesc());
		float scoreTotal = mCourse.getAvgScore();
		mTvScoreTotal.setText(CommonUtil.floatToString(scoreTotal, 1) + "（"
				+ mCourse.getCommentUserNum() + "个点评）");
		mRatingBarScore.setRating(scoreTotal / 2);
		mTvScoreKnowledge.setText(this
				.getString(R.string.course_review_knowledge_detail)
				+ "："
				+ CommonUtil.floatToString(mCourse.getAvg0(), 1));
		mTvScoreFunny.setText(this
				.getString(R.string.course_review_funny_detail)
				+ "："
				+ CommonUtil.floatToString(mCourse.getAvg1(), 1));
		mTvScoreDesign.setText(this
				.getString(R.string.course_review_design_detail)
				+ "："
				+ CommonUtil.floatToString(mCourse.getAvg2(), 1));
		mTvLearnCount.setText(this.getString(R.string.course_learn) + "（"
				+ mCourse.getLearnedNum() + "）");
		mTvFocusCount.setText(this.getString(R.string.course_focus) + "（"
				+ mCourse.getFocusNum() + "）");
		mTvReviewTips.setText(this.getString(R.string.course_review) + "（"
				+ mCourse.getCommentUserNum() + "个点评）");
		mTvAsksTips.setText(this.getString(R.string.asks) + "（"
				+ mCourse.getAsksCount() + "个问题）");
		mTvHomework.setText(mCourse.getHomeworkCount() + "套作业题");
		mTvExam.setText(mCourse.getExamCount() + "套考试题");

		// 如果已登录且是我的课程
		if (this.isLogin() && this.getUser().isMyCourse(mCourse.getCourseId())) {
			mTvBottomGoClass.setText(this.getString(R.string.course_go_class));
		} else {
			mTvBottomGoClass.setText(this
					.getString(R.string.course_go_class_before));
		}
	}

	private void onActivityResultReviewAdd(Intent data) {
		mIsReviewAdd = true;
		if (null != data) {
			String temp = data.getStringExtra("CourseReview");
			String course = data.getStringExtra("course");
			CourseReview review = CourseReview.toObj(temp);
			mCourse = Course.toObj(course);
			mRlReviewItemView.setVisibility(View.VISIBLE);
			mBtnReviewMore.setVisibility(View.VISIBLE);
			mTvReviewEmptyTips.setVisibility(View.GONE);
			this.bindValue();
			mReviewHelper.bindItemValue(review);
		}
	}

	private Intent buildIntent(Class<?> cls) {
		Intent intent = new Intent(this, cls);
		intent.putExtra("course", mCourse.toJson());
		return intent;
	}

	private void showLoadingIv(ImageView iv, ImageLoadingHelper helper) {
		iv.setVisibility(View.VISIBLE);
		helper.startAnimation();
	}

	private void hideLoadingIv(ImageView iv, ImageLoadingHelper helper) {
		iv.setVisibility(View.GONE);
		helper.stopAnimation();
	}

	private void onFocusSuccess() {
		int count = mCourse.getFocusNum();
		int temp = count + 1;
		mCourse.setFocusNum(temp);
		mTvFocusCount.setText(this.getString(R.string.course_focus) + "（"
				+ temp + "）");
	}

	private void onFocusFails() {
		mIsFocusSuccess = false;
		int count = mCourse.getFocusNum();
		int temp = count - 1;
		mCourse.setFocusNum(temp);
		mTvFocusCount.setText(this.getString(R.string.course_focus) + "（"
				+ temp + "）");
	}

	private void setViewState(View view, boolean enable) {
		view.setEnabled(enable);
		view.setClickable(enable);
	}

	private void onJoinClassComplete(boolean success) {// 加入课程完成
		this.setViewState(mRlBottom, true);
		this.hideLoadingIv(mIvJoinClass, mIvJoinClassHelper);
		mTvBottomGoClass.setVisibility(View.VISIBLE);
		if (success) {
			int count = mCourse.getLearnedNum();
			int temp = count + 1;
			mCourse.setLearnedNum(temp);
			mTvLearnCount.setText(this.getString(R.string.course_learn) + "（"
					+ mCourse.getLearnedNum() + "）");
			mIsLearned = true;
			mTvBottomGoClass.setText(this.getString(R.string.course_go_class));
			this.showToast(this.getString(R.string.course_add_success));
			// 更新内存中myCourseIds
			this.getUser().addMyCourse(mCourse.getCourseId());
			// 更新本地myCourseIds
			// CoreSharedPreference.saveMember(this, this.getUser());
			CoreSharedPreference.saveObject(this,
					GlobalConfig.SharedPreferenceDao.FILENAME_USER,
					GlobalConfig.SharedPreferenceDao.KEY_USER, this.getUser());
		} else {
			mIsLearned = false;
			this.showToast(this.getString(R.string.course_add_fail));
		}
	}

	private void onJoinClassStart() {// 加入课程开始
		mTvBottomGoClass.setVisibility(View.GONE);
		this.showLoadingIv(mIvJoinClass, mIvJoinClassHelper);
		this.setViewState(mRlBottom, false);
	}

	private void fetchReview() {
		this.showLoadingIv(mIvReviewLoading, mIvReviewLoadingHelper);
		Map<String, String> params = new HashMap<String, String>();
		params.put("max", 1 + "");
		params.put("courseId", mCourse.getCourseId() + "");
		RequestTag tag = new RequestTag(mReqTag,
				GlobalConfig.Operator.COURSE_REVIEW_GET);
		this.getHttpApi().request(GlobalConfig.URL_COURSE_GET_COMMENT, params,
				this, tag);
	}

	private void startLogin(int requestCode) {
		this.startActivityForResult(new Intent(this, LoginActivity.class),
				requestCode);
	}

	private void startReviewAdd() {
		this.startActivityForResult(
				this.buildIntent(CourseReviewAddActivity.class),
				CODE_REQ_REVIEW_ADD);
	}

	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		if (null == arg0 || null == arg0.param) {
			return;
		}
		int operator = arg0.param.operator;
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		switch (operator) {
		case GlobalConfig.Operator.COURSE_REVIEW_GET:// 获取评论
			if (parser.isSuccess()) {
				List<CourseReview> reviews = parser
						.getArray(CourseReview.class);
				this.hideLoadingIv(mIvReviewLoading, mIvReviewLoadingHelper);
				if (null != reviews && !reviews.isEmpty()) {
					mTvReviewEmptyTips.setVisibility(View.GONE);
					mRlReviewItemView.setVisibility(View.VISIBLE);
					mBtnReviewMore.setVisibility(View.VISIBLE);
					mReviewHelper.bindItemValue(reviews.get(0));
				} else {
					mTvReviewEmptyTips.setVisibility(View.VISIBLE);
				}
			} else {
				// mLoadinHelperReview.showRetry();
			}
			break;
		case GlobalConfig.Operator.COURSE_FOCUS:// 关注成功
			this.getUser().addMyFocusCourse(mCourse.getCourseId());
			mIsFocusSuccess = true;
			this.showToast(this.getString(R.string.course_focus_success));
			break;
		case GlobalConfig.Operator.COURSE_ADD:// 加入课程成功
			this.onJoinClassComplete(true);
			break;
		}
	}

	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		if (null == out || null == out.param) {
			return;
		}
		int operator = out.param.operator;
		switch (operator) {
		case GlobalConfig.Operator.COURSE_DETAIL:// 获取详情失败
			super.onResponseError(out);
			break;
		case GlobalConfig.Operator.COURSE_REVIEW_GET:// 获取评论
			break;
		case GlobalConfig.Operator.COURSE_FOCUS:// 关注失败
			this.showToast(this.getString(R.string.course_focus_fail));
			this.onFocusFails();
			break;
		case GlobalConfig.Operator.COURSE_ADD:// 加入课程失败
			this.onJoinClassComplete(false);
			break;
		}
	}

	public void onClickFocus(View view) {// 关注课程
		if (!this.isNetworkEnable()) {
			this.showToast(this.getString(R.string.no_network));
			return;
		}
		if (this.isLogin()) {
			RequestTag tag = new RequestTag(this.mReqTag,
					GlobalConfig.Operator.COURSE_FOCUS);
			Map<String, String> params = new HashMap<String, String>();
			params.put("userId", this.getUser().getUserId() + "");
			params.put("courseId", mCourse.getCourseId() + "");
			this.getHttpApi().request(GlobalConfig.URL_COURSE_FOCUS, params,
					this, tag);
			this.onFocusSuccess();
		} else {
			this.startLogin(CODE_REQ_LOGIN_FOCUS);
		}
	}

	public void onClickDownload(View view) {// 离线缓存

	}

	public void onClickDescMore(View view) {// 更多简介

	}

	public void onClickHomework(View view) {// 作业题
		int count = mCourse.getHomeworkCount();
		if (count <= 0) {
			this.showToast(this.getString(R.string.course_homework_none));
			return;
		}
		Intent intent = this.buildIntent(ResourceListActivity.class);
		intent.putExtra("resType", Resource.TYPE_HOMEWORK);
		this.startActivity(intent);
	}

	public void onClickExam(View view) {// 考试题
		int count = mCourse.getExamCount();
		if (count <= 0) {
			this.showToast(this.getString(R.string.course_exam_none));
			return;
		}
		Intent intent = this.buildIntent(ResourceListActivity.class);
		intent.putExtra("resType", Resource.TYPE_EXAM);
		this.startActivity(intent);
	}

	public void onClickReviewAdd(View view) {// 评论
		if (this.isLogin()) {
			if (this.getUser().isMyCourse(mCourse.getCourseId())) {
				this.startReviewAdd();
			} else {
				this.showToast(this.getString(R.string.course_not_my_course));
			}
		} else {
			this.startLogin(CODE_REQ_LOGIN_REVIEW_ADD);
		}
	}

	public void onClickReviewMore(View view) {// 更多评价
		this.startActivityForResult(
				this.buildIntent(CourseReviewListActivity.class),
				CODE_REQ_REVIEW_MORE);
	}

	public void onClickAsksAdd(View view) {// 提问

	}

	public void onClickAsksMore(View view) {// 更多问题

	}

	public void onClickGoClass(View view) {// 去上课/加入课程
		if (mCourse.isOnline() && !this.isNetworkEnable()) {
			this.showToast(this.getString(R.string.no_network));
			return;
		}
		if (this.isLogin()) {
			int courseId = mCourse.getCourseId();
			if (this.getUser().isMyCourse(courseId)) {// 已登录且是我的课程
				this.startActivity(this
						.buildIntent(CourseSectionListActivity.class));
			} else {// 加入课程
				this.onJoinClassStart();
				RequestTag tag = new RequestTag(mReqTag,
						GlobalConfig.Operator.COURSE_ADD);
				Map<String, String> params = new HashMap<String, String>();
				params.put("courseId", courseId + "");
				params.put("userId", this.getUser().getUserId() + "");
				this.getHttpApi().request(GlobalConfig.URL_COURSE_ADD, params,
						this, tag);
			}
		} else {
			this.startLogin(CODE_REQ_LOGIN_JOIN_CLASS);
		}
	}

}
