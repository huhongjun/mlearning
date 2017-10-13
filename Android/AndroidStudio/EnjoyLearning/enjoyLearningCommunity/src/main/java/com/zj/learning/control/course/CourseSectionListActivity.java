package com.zj.learning.control.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.CoreApp;
import com.zj.learning.R;
import com.zj.learning.ResourceHouseKeeper;
import com.zj.learning.ResourcePathHelper;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.control.course.adapter.CourseSectionListAdapter;
import com.zj.learning.control.course.video.VideoOfflineActivity;
import com.zj.learning.control.course.video.VideoOnlineActivity;
import com.zj.learning.model.course.Course;
import com.zj.learning.model.course.CourseChapter;
import com.zj.learning.model.course.CourseSection;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * 课程章节列表
 * 
 * @author XingRongJing
 * 
 */
public class CourseSectionListActivity extends BaseActivity {

	private TextView mTvTitle;
	private ExpandableListView mListView;
	private LinearLayout mLlLoading;
	private RelativeLayout mRlEmpty;
	private CourseSectionListAdapter mAdapter;
	private List<CourseChapter> mChapters;
	private Course mCourse;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_section_list);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mListView = (ExpandableListView) this
				.findViewById(R.id.course_section_list_listview);
		mLlLoading = (LinearLayout) this
				.findViewById(R.id.course_section_list_ll_loading);
		mRlEmpty = (RelativeLayout) this
				.findViewById(R.id.course_section_list_rl_empty);
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
		mTvTitle.setText(mCourse.getResName());

		mChapters = new ArrayList<CourseChapter>();

		// Step 1：获取数据
		this.fetchDatas();
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
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		// Step 2.1：获取数据完成
		if (null == arg0) {
			return;
		}
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			List<CourseChapter> chapters = parser.getArray(CourseChapter.class);
			this.handleDataSuccess(chapters);
			this.hideLoading();
		} else {
			this.showToast(this.getString(R.string.request_fail));
			this.showRetry();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onResponseCache(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponseCache(arg0);
		if (null == arg0) {
			return;
		}
		List<Object> temps = (List<Object>) arg0.result;
		this.handleDataSuccess(temps);
		this.notifyDataChanged();
		this.hideLoading();
		if (this.isDataEmpty()) {
			this.showDataEmpty();
		}
	}

	private void fetchDatas() {
		if (mCourse.isOnline()) {
			this.showLoading();
			// Step 1：获取远程数据
			this.fetchDatasFromServer();
		} else {
			// Step 1：自动获取数据
			this.fetchDatasFromXml();
		}
	}

	/**
	 * 获取数据（本地）
	 */
	private void fetchDatasFromXml() {
		String path = ResourcePathHelper
				.getCoursePath(mCourse.getResFileName());
		// 本地已有，则直接解析即可
		if (ResourcePathHelper.isFileExist(path)) {
			this.showLoading();
			this.getResourceHouseKeeper().fetchChapters(path, this,
					new RequestTag(mReqTag));
		} else {// 没有则提示资源不存在
			this.showToast(this.getString(R.string.course_offline_res_not_find));
			this.showDataEmpty();
		}
	}

	/**
	 * 获取数据（在线）
	 */
	private void fetchDatasFromServer() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("fileName", mCourse.getResFileName());
		this.getHttpApi().request(GlobalConfig.URL_COURSE_SECTIONS, params,
				this, new RequestTag(this.mReqTag));
	}

	private boolean isDataEmpty() {
		return (null == mChapters ? true : mChapters.isEmpty());
	}

	@SuppressWarnings("unchecked")
	private void handleDataSuccess(List<?> chapters) {
		if (null != mChapters && null != chapters && !chapters.isEmpty()) {
			mChapters.addAll((List<CourseChapter>) chapters);
			this.notifyDataChanged();
		} else {
			this.showDataEmpty();
		}
	}

	private void notifyDataChanged() {
		if (null == mAdapter) {
			mAdapter = new CourseSectionListAdapter(this, mChapters);
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	private ResourceHouseKeeper getResourceHouseKeeper() {
		return ((CoreApp) this.getApplication()).getResourceHouseKeeper();
	}

	private void showDataEmpty() {
		if (null != mRlEmpty) {
			mRlEmpty.setVisibility(View.VISIBLE);
		}
		if (null != mListView) {
			mListView.setVisibility(View.GONE);
		}
	}

	public void onClickReload(View view) {// 没网或失败时，重新加载
		// Step 2：手动获取数据
		this.hideRetry();
		this.fetchDatas();
	}

	public void onClickRight(View view) {
		CourseSection section = (CourseSection) view.getTag();
		Intent intent = null;
		if (mCourse.isOnline()) {
			intent = new Intent(this, VideoOnlineActivity.class);
		} else {
			intent = new Intent(this, VideoOfflineActivity.class);
		}
		intent.putExtra("courseSection", section.toJson());
		intent.putExtra("course", mCourse.toJson());
		this.startActivity(intent);
	}

}
