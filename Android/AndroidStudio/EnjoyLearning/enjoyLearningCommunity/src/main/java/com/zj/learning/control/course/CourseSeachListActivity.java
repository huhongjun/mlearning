package com.zj.learning.control.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListActivity;
import com.zj.learning.model.course.Course;
import com.zj.learning.view.course.CourseItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;
import com.zj.support.widget.refresh.PullToRefreshBase.Mode;

/**
 * 课程搜索界面
 * 
 * @author XingRongJing
 * 
 */
public class CourseSeachListActivity extends BaseRefreshListActivity implements
		TextWatcher {

	private static final int REQ_CODE = 1;
	private List<Course> mCourses;
	private EditText mEtSearch;
	private String mKeywords;
	private RelativeLayout mRlEmpty;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_search_list);
		mListView = (PullToRefreshListView) this
				.findViewById(R.id.course_search_list_listview);
		mRlEmpty = (RelativeLayout) this
				.findViewById(R.id.course_search_list_empty_rl_no_data);
		mEtSearch = (EditText) this.findViewById(R.id.titlebar_search_et);
		mEtSearch.addTextChangedListener(this);
		mEtSearch.setHint(this.getString(R.string.course_search_hint));

	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		this.showEmpty();
		mCourses = new ArrayList<Course>();
		mAdapter = new ItemSingleAdapter<CourseItemView, Course>(this, mCourses);
		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Course course = (Course) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this, CourseDetailActivity.class);
		course.setOnline(true);
		intent.putExtra("course", course.toJson());
		this.startActivityForResult(intent, REQ_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK != resultCode || REQ_CODE != requestCode
				|| null == data) {
			return;
		}
		int courseId = data.getIntExtra("courseId", -1);
		boolean isFocus = data.getBooleanExtra("isFocus", false);
		boolean isLearn = data.getBooleanExtra("isLearn", false);
		Course course = this.getCourseById(courseId);
		if (null != course) {
			if (isFocus) {
				int focusNum = course.getFocusNum();
				course.setFocusNum(++focusNum);
			}
			if (isLearn) {
				int learnNum = course.getLearnedNum();
				course.setLearnedNum(++learnNum);
			}
			this.notifyDataChanged();
		}
	}


	private Course getCourseById(int courseId) {
		if (this.isDataEmpty()) {
			return null;
		}
		int size = mCourses.size();
		for (int i = 0; i < size; i++) {
			Course temp = mCourses.get(i);
			if (courseId == temp.getCourseId()) {
				return temp;
			}
		}
		return null;
	}

	@Override
	protected void fetchCacheDatas() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleSuccessDatas(List<?> results) {
		// TODO Auto-generated method stub
		if (null != results && !results.isEmpty()) {
			List<Course> temps = (List<Course>) results;
			// 设置加载更多时时间戳(此处实际是courId，比较特殊)
			this.setTimestamp(temps.get(temps.size() - 1).getCourseId());
			mCourses.addAll(temps);
		}
		this.notifyDataChanged();
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(mKeywords)) {
			this.showToast(this.getString(R.string.course_search_no_empty));
			return;
		}
		RequestTag tag = new RequestTag(mReqTag);
		// Step 2.1：获取数据
		Map<String, String> params = new HashMap<String, String>();
		params.put("max", this.getMax() + "");
		if (!needCache) {
			params.put("courseId", this.getTimestamp() + "");
		}
		params.put("resName", mKeywords);
		this.getHttpApi().request(GlobalConfig.URL_COURSE_ALL, params, this,
				tag);

	}

	@Override
	protected boolean isDataEmpty() {
		// TODO Auto-generated method stub
		return (null == mCourses ? true : mCourses.isEmpty());
	}

	@Override
	protected void clearDatas() {
		// TODO Auto-generated method stub
		if (null != mCourses) {
			mCourses.clear();
		}
	}

	public void onClicksearchBtn(View view) {// 点击搜索
		if (TextUtils.isEmpty(mKeywords)) {
			this.showToast(this.getString(R.string.course_search_no_empty));
			return;
		}
		this.hideEmpty();
		this.setMode(Mode.BOTH);
		this.onFirstManualRefresh();
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (TextUtils.isEmpty(s)) {
			this.clearDatas();
			this.notifyDataChanged();
			this.showEmpty();
			mKeywords = null;
			this.setMode(Mode.DISABLED);
		} else {
			mKeywords = s.toString();
			// this.onFirstManualRefresh();
			// this.setMode(Mode.BOTH);
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		// Step 2.2：加载远程数据完成
		if (null == arg0) {
			return;
		}
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			// Step 2.2.3 处理UI和数据
			List<Course> courses = parser.getArray(Course.class);
			this.handleFetchDataSuccess(courses);
			if (this.isDataEmpty()) {
				this.showEmpty();
			}
		} else {
			this.handleFetchDataFail();
		}

	}

	private void hideEmpty() {
		mRlEmpty.setVisibility(View.GONE);
		mListView.setVisibility(View.VISIBLE);
	}

	private void showEmpty() {
		mRlEmpty.setVisibility(View.VISIBLE);
		mListView.setVisibility(View.GONE);
	}
}
