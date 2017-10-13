package com.zj.learning.control.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.LoginActivity;
import com.zj.learning.control.common.base.BaseRefreshListActivity;
import com.zj.learning.model.course.Course;
import com.zj.learning.model.course.CourseReview;
import com.zj.learning.view.course.CourseReviewItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 课程评价列表
 * 
 * @author XingRongJing
 * 
 */
public class CourseReviewListActivity extends BaseRefreshListActivity {

	private static final int CODE_REQ_REVIEW_ADD = 1;
	private static final int CODE_REQ_LOGIN_REVIEW_ADD = 2;
	private TextView mTvTitle;
	private List<CourseReview> mReviews;
	private TextView mTvReviewAdd;
	private Course mCourse;
	private CourseReview mNewReview;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_review_list);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mListView = (PullToRefreshListView) this
				.findViewById(R.id.course_review_list_listview);
		mTvReviewAdd = (TextView) this
				.findViewById(R.id.titlebar_back_tv_right);
		mTvReviewAdd.setVisibility(View.VISIBLE);
		mTvReviewAdd.setText(this.getString(R.string.review_add));
	}

	@SuppressWarnings("unchecked")
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
		mReviews = new ArrayList<CourseReview>();
		mAdapter = new ItemSingleAdapter<CourseReviewItemView, CourseReview>(
				this, mReviews);
		((ItemSingleAdapter<CourseReviewItemView, CourseReview>) mAdapter)
				.setEnableClick(false);
		mListView.setAdapter(mAdapter);
		this.handleFetchCacheComplete(null);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (null != mCourse) {// 保存course数据
			outState.putString("course", mCourse.toJson());
		}
	}

	@Override
	protected void setResultBack() {
		if (null != mNewReview) {
			Intent intent = new Intent();
			intent.putExtra("CourseReview", mNewReview.toJson());
			intent.putExtra("course", mCourse.toJson());
			this.setResult(RESULT_OK, intent);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isDataEmpty() {
		// TODO Auto-generated method stub
		return (null == mReviews ? true : mReviews.isEmpty());
	}

	@Override
	protected void clearDatas() {
		// TODO Auto-generated method stub
		if (null != mReviews) {
			mReviews.clear();
		}
	}

	@Override
	protected void fetchCacheDatas() {
		// TODO Auto-generated method stub
		// this.getCacheManager().getCacheByUrl(this, tag, url)
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		// TODO Auto-generated method stub
		Map<String, String> params = new HashMap<String, String>();
		params.put("max", this.getMax() + "");
		params.put("courseId", mCourse.getCourseId() + "");
		if (!needCache) {
			params.put("timestamp", this.getTimestamp() + "");
		}
		RequestTag tag = new RequestTag(mReqTag,
				GlobalConfig.Operator.COURSE_REVIEW_GET);
		this.getHttpApi().request(GlobalConfig.URL_COURSE_GET_COMMENT, params,
				this, tag);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleSuccessDatas(List<?> results) {
		// TODO Auto-generated method stub
		if (null != results && !results.isEmpty()) {
			List<CourseReview> temps = (List<CourseReview>) results;
			// 设置加载更多时时间戳
			this.setTimestamp(temps.get(temps.size() - 1).getCommentTime());
			mReviews.addAll(temps);
		}
		this.notifyDataChanged();
	}

	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		if (null == arg0) {
			return;
		}
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {// 获取数据成功
			List<CourseReview> temps = parser.getArray(CourseReview.class);
			this.handleFetchDataSuccess(temps);
		} else {
			this.handleFetchDataFail();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		if (CODE_REQ_REVIEW_ADD == requestCode && null != data) {
			String reviewJson = data.getStringExtra("CourseReview");
			String course = data.getStringExtra("course");
			mNewReview = CourseReview.toObj(reviewJson);
			mCourse = Course.toObj(course);
			List<CourseReview> tempList = new ArrayList<CourseReview>();
			tempList.addAll(mReviews);
			mReviews.clear();
			mReviews.add(mNewReview);
			mReviews.addAll(tempList);
			tempList = null;
			this.notifyDataChanged();
		}else if(CODE_REQ_LOGIN_REVIEW_ADD == requestCode){//登录成功-点评
			if (this.getUser().isMyCourse(mCourse.getCourseId())) {
				this.startReviewAdd();
			} else {
				this.showToast(this.getString(R.string.course_not_my_course));
			}
		}
	}

	public void onClickRightBtn(View view) {// 写评论
		if (this.isLogin()) {
			if (this.getUser().isMyCourse(mCourse.getCourseId())) {
				this.startReviewAdd();
			} else {
				this.showToast(this.getString(R.string.course_not_my_course));
			}
		} else {
			this.startActivityForResult(new Intent(this, LoginActivity.class),CODE_REQ_LOGIN_REVIEW_ADD);
		}

	}
	
	private void startReviewAdd(){
		Intent intent = new Intent(this, CourseReviewAddActivity.class);
		intent.putExtra("course", mCourse.toJson());
		this.startActivityForResult(intent, CODE_REQ_REVIEW_ADD);
	}

}
