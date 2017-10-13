package com.zj.learning.control.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListFragment;
import com.zj.learning.model.course.Course;
import com.zj.learning.view.course.CourseItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 在线课程界面
 * 
 * @author XingRongJing
 * 
 */
public class CourseOnlineListFragment extends BaseRefreshListFragment {

	private static final int REQ_CODE = 1;
	private List<Course> mCourses;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mCourses = new ArrayList<Course>();
		mAdapter = new ItemSingleAdapter<CourseItemView, Course>(
				this.getActivity(), mCourses);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.course_list, null);
		mLlLoading = (LinearLayout) view.findViewById(R.id.course_list_loading);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.course_list_listview);
		mListView.setAdapter(mAdapter);
		this.prepareByView();
		// 如果还未加载过
		if (!this.isLoaded()) {
			// Step 1:加载缓存
			this.fetchCacheDatas();
			this.setIsLoaded(true);
		} else if (this.isDataEmpty()) {
			if (this.isNetworkEnable() && !this.isReqFails()) {
				this.setEmptyView();
			} else {
				this.setNoNetworkView();
			}
		}
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Course course = (Course) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this.getActivity(),
				CourseDetailActivity.class);
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
			float avgScore = data.getFloatExtra("avgScore", 0);
			if (0 != avgScore) {
				course.setAvgScore(avgScore);
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
		// Step 1：先看缓存
		this.showLoading();
		RequestTag tag = new RequestTag(mReqTag);
		this.getCacheManager().getCacheByUrl(this, tag,
				GlobalConfig.URL_COURSE_ALL);

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
		RequestTag tag = new RequestTag(mReqTag);
		if (needCache) {
			tag.cacheUrl = GlobalConfig.URL_COURSE_ALL;
		}
		// Step 2.1：获取数据
		Map<String, String> params = new HashMap<String, String>();
		params.put("max", this.getMax() + "");
		if (!needCache) {
			params.put("courseId", this.getTimestamp() + "");
		}
		this.getHttpApi().request(GlobalConfig.URL_COURSE_ALL, params, this,
				tag);

	}

	@Override
	protected boolean isDataEmpty() {
		// TODO Auto-generated method stub
		return (null == mCourses ? false : mCourses.isEmpty());
	}

	@Override
	protected void clearDatas() {
		// TODO Auto-generated method stub
		if (null != mCourses) {
			mCourses.clear();
		}
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
			// Step 2.2.1 设置此次登陆不再自动刷新本页
			this.setRefreshAutoKey(GlobalConfig.URL_COURSE_ALL);
			RequestTag tag = arg0.param;
			// Step 2.2.2 缓存需要缓存的数据
			if (null != tag && !TextUtils.isEmpty(tag.cacheUrl)) {
				this.saveCache(tag.cacheUrl, arg0.resp);
			}
			// Step 2.2.3 处理UI和数据
			List<Course> courses = parser.getArray(Course.class);
			this.handleFetchDataSuccess(courses);
		} else {
			this.handleFetchDataFail();
		}

	}

	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseCache(out);
		// Step 1.1：加载缓存完成（不论成功与否、有无缓存）
		if (null == out) {
			return;
		}
		List<Course> courses = null;
		String resp = out.resp;
		if (!TextUtils.isEmpty(resp)) {// 缓存不为空
			RequestDataParser parser = new RequestDataParser(resp);
			courses = parser.getArray(Course.class);
		}
		this.handleFetchCacheComplete(courses);
	}

}
