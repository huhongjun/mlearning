package com.zj.learning.control.course;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.zj.learning.CoreApp;
import com.zj.learning.R;
import com.zj.learning.ResourceHouseKeeper;
import com.zj.learning.ResourcePathHelper;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListFragment;
import com.zj.learning.model.course.Course;
import com.zj.learning.view.course.CourseItemView;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 本地课程界面
 * 
 * @author XingRongJing
 * 
 */
public class CourseOfflineListFragment extends BaseRefreshListFragment {

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
		course.setOnline(false);
		intent.putExtra("course", course.toJson());
		this.startActivityForResult(intent,1002);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		System.out.println(" offline = "+requestCode);
	}

	@Override
	protected void fetchCacheDatas() {
		// TODO Auto-generated method stub
		// Step 1：先看缓存
		String path = ResourcePathHelper.getResourcesPath();
		// Step 1.1.1 本地已有资源文件，则解析
		if (ResourcePathHelper.isFileExist(path)) {
			this.showLoading();
			this.fetchResourceFromXml(path);
		} else {// Step1.1.2 本地没有资源文件，暂提醒即可
			this.showToast(this.getString(R.string.course_offline_no_exist));
			this.setEmptyView();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleSuccessDatas(List<?> results) {
		// TODO Auto-generated method stub
		if (null != results) {
			List<Course> temps = (List<Course>) results;
			mCourses.addAll(temps);
		}
		this.notifyDataChanged();
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		// TODO Auto-generated method stub
		// Step 2:获取数据
		Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);

				int offset = getMax();
				int start = (getPage() - 1) * offset;
				List<Course> temps = getResourceHouseKeeper().getCourses(start,
						offset);
				handleFetchDataSuccess(temps);
			}
		};
		mHandler.sendEmptyMessageDelayed(0, 200);

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

	@SuppressWarnings("unchecked")
	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		// Step 2.1：获取数据完成
		if (null == arg0) {
			return;
		}
		List<Course> courses = (List<Course>) arg0.result;
		this.handleFetchDataSuccess(courses);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseCache(out);
		// Step 1.2：获取缓存成功，加载数据
		if (null == out.param) {
			return;
		}
		int operator = out.param.operator;
		switch (operator) {
		case GlobalConfig.Operator.RESOURCE_FETCH:
			List<Object> results = (List<Object>) out.result;
			this.getResourceHouseKeeper().pushCourses(results);
			this.handleFetchCacheComplete(null);
			break;
		}
	}

	@Override
	protected boolean isNetworkEnable() {
		// TODO Auto-generated method stub
		return true;
	}

	/**
	 * 选中此页时，数据初始化之源
	 */
	public void onSelected() {
		// 如果是还未加载
		if (!this.isLoaded()) {
			this.prepareByView();
			this.fetchCacheDatas();
			this.setIsLoaded(true);
		} else if (this.isDataEmpty()) {
			if (this.isNetworkEnable() && !this.isReqFails()) {
				this.setEmptyView();
			} else {
				this.setNoNetworkView();
			}
		} else {
			this.prepareByView();
		}
	}

	private void fetchResourceFromXml(String path) {
		RequestTag tag = new RequestTag(mReqTag,
				GlobalConfig.Operator.RESOURCE_FETCH);
		this.getResourceHouseKeeper().fetchCourse(path, this, tag);
	}

	private ResourceHouseKeeper getResourceHouseKeeper() {
		return ((CoreApp) this.getActivity().getApplication())
				.getResourceHouseKeeper();
	}

}
