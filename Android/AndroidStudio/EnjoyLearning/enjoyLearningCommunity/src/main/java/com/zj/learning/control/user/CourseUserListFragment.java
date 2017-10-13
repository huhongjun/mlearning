package com.zj.learning.control.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.zj.learning.control.course.CourseDetailActivity;
import com.zj.learning.model.course.Course;
import com.zj.learning.view.course.CourseItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 我的课程/我关注的课程界面
 * 
 * @author XingRongJing
 * 
 */
public class CourseUserListFragment extends BaseRefreshListFragment {

	private List<Course> mCourses;
	private int mOperator = Course.OPERATOR_COURSE_ME;

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		mOperator = args.getInt("operator");
	}

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
		if (Course.OPERATOR_COURSE_ME == mOperator) {
			this.onRequest();
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
		this.startActivity(intent);
	}

	@Override
	protected void fetchCacheDatas() {
		// TODO Auto-generated method stub
		// Step 1：先看缓存
		this.showLoading();
		RequestTag tag = new RequestTag(mReqTag);
		if (this.isMyCourse()) {
			this.getCacheManager().getCacheByUrl(this, tag,
					GlobalConfig.URL_COURSE_ME);
		} else {
			this.getCacheManager().getCacheByUrl(this, tag,
					GlobalConfig.URL_COURSE_FOCUS_ME);
		}

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
		Map<String, String> params = new HashMap<String, String>();
		String url = null;
		params.put("max", this.getMax() + "");
		params.put("userId", this.getUser().getUserId() + "");
		if (!needCache) {
			params.put("courseId", this.getTimestamp() + "");
		}
		// Step 2.1：获取数据
		if (this.isMyCourse()) {
			if (needCache) {
				tag.cacheUrl = GlobalConfig.URL_COURSE_ME;
			}
			url = GlobalConfig.URL_COURSE_ME;
		} else {
			if (needCache) {
				tag.cacheUrl = GlobalConfig.URL_COURSE_FOCUS_ME;
			}
			url = GlobalConfig.URL_COURSE_FOCUS_ME;
		}
		this.getHttpApi().request(url, params, this, tag);

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
			if (this.isMyCourse()) {
				this.setRefreshAutoKey(GlobalConfig.URL_COURSE_ME);
			} else {
				this.setRefreshAutoKey(GlobalConfig.URL_COURSE_FOCUS_ME);
			}
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

	/**
	 * 如果是选中我关注的课程时，则加载数据
	 */
	public void onSelectedFoucsCourse() {
		this.onRequest();
	}

	private boolean isMyCourse() {
		return Course.OPERATOR_COURSE_ME == mOperator;
	}

	private void onRequest() {
		// 如果还未加载过
		if (!this.isLoaded()) {
			this.fetchCacheDatas();
			this.setIsLoaded(true);
		} else if (this.isDataEmpty()) {
			if (this.isNetworkEnable() && !this.isReqFails()) {
				this.setEmptyView();
			} else {
				this.setNoNetworkView();
			}
		}
	}
}
