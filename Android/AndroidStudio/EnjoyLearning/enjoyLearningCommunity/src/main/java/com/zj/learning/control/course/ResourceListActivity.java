package com.zj.learning.control.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.CoreApp;
import com.zj.learning.R;
import com.zj.learning.ResourceHouseKeeper;
import com.zj.learning.ResourcePathHelper;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.model.course.Course;
import com.zj.learning.model.course.Resource;
import com.zj.learning.view.course.ResourceItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;

/**
 * 考试/作业列表界面
 * 
 * @author XingRongJing
 * 
 */
public class ResourceListActivity extends BaseActivity {

	private TextView mTvTitle;
	private ListView mListView;
	private RelativeLayout mRlEmpty;
	private List<Resource> mResources;
	private BaseAdapter mAdapter;
	private Course mCourse;
	private int mResType = -1;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_question_list);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mListView = (ListView) this
				.findViewById(R.id.course_question_list_listview);
		mRlEmpty = (RelativeLayout) this
				.findViewById(R.id.course_question_list_rl_empty);
		LinearLayout mLlLoading = (LinearLayout) this
				.findViewById(R.id.course_question_list_loading);
		this.buildLoading(mLlLoading);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		String jsonCourse = "";
		if (null == savedInstanceState) {
			Intent intent = this.getIntent();
			jsonCourse = intent.getStringExtra("course");
			mResType = intent.getIntExtra("resType", -1);
		} else {// 恢复course数据
			jsonCourse = savedInstanceState.getString("course");
			mResType = savedInstanceState.getInt("resType");
		}
		mCourse = Course.toObj(jsonCourse);
		mTvTitle.setText(mCourse.getResName());

		mResources = new ArrayList<Resource>();
		mAdapter = new ItemSingleAdapter<ResourceItemView, Resource>(this,
				mResources);
		((ItemSingleAdapter<ResourceItemView, Resource>) mAdapter)
				.setEnableClick(false);
		mListView.setAdapter(mAdapter);

		// Step 1：获取数据
		this.fetchDatas();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (null != mCourse) {// 保存course数据
			outState.putString("course", mCourse.toJson());
			outState.putInt("resType", mResType);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseCache(out);
		// Step 2.1：获取本地成功
		if (null == out) {
			return;
		}
		List<Object> temps = (List<Object>) out.result;
		this.handleDataSuccess(temps);
		this.notifyDataChanged();
		this.hideLoading();
		if (this.isDataEmpty()) {
			this.showDataEmpty();
		}
	}

	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		// Step 2.2：获取远程成功
		if (null == arg0) {
			return;
		}
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			List<Resource> resList = parser.getArray(Resource.class);
			this.handleDataSuccess(resList);
			this.hideLoading();
		} else {
			this.showRetry();
		}
	}

	private void handleDataSuccess(List<?> resources) {
		if (null != mResources && null != resources && !resources.isEmpty()) {
			for (Object obj : resources) {
				Resource res = (Resource) obj;
				res.setResType(mResType);
				mResources.add(res);
			}
			this.notifyDataChanged();
		} else {
			this.showDataEmpty();
		}
	}

	private boolean isDataEmpty() {
		return (null == mResources ? true : mResources.isEmpty());
	}

	private void notifyDataChanged() {
		if (null == mAdapter) {
			return;
		}
		mAdapter.notifyDataSetChanged();
	}

	private void showDataEmpty() {
		if (null != mRlEmpty) {
			mRlEmpty.setVisibility(View.VISIBLE);
		}
		if (null != mListView) {
			mListView.setVisibility(View.GONE);
		}
	}

	private ResourceHouseKeeper getResourceHouseKeeper() {
		return ((CoreApp) this.getApplication()).getResourceHouseKeeper();
	}

	public void onClickReload(View view) {// 没网或失败时，重新加载
		// Step 2：手动获取数据
		this.hideRetry();
		this.fetchDatas();
	}

	public void onClickRight(View view) {// 做作业/去考试
		try {
			Resource resource = (Resource) view.getTag();
			Intent intent = new Intent(this, ResourceDetailActivity.class);
			intent.putExtra("resource", resource.toJson());
			intent.putExtra("course", mCourse.toJson());
			this.startActivity(intent);
		} catch (Exception e) {
		}
	}

	private void fetchDatas() {
		List<Integer> ids = this.getIdsByType(mResType);
		if (null == ids || ids.isEmpty()) {
			this.showDataEmpty();
			return;
		}
		if (mCourse.isOnline()) {
			this.fetchDatasFromServer(mResType, ids);
		} else {
			this.fetchDatasFromXml(mResType, ids);
		}

	}

	private void fetchDatasFromXml(int type, List<Integer> ids) {
		String path = ResourcePathHelper.getResourcesPath();
		if (ResourcePathHelper.isFileExist(path)) {
			this.showLoading();
			this.getResourceHouseKeeper().fetchResource(path, this,
					new RequestTag(mReqTag), type, ids);
		} else {
			this.showToast(this.getString(R.string.course_offline_res_not_find));
			this.showDataEmpty();
		}
	}

	private void fetchDatasFromServer(int mResType, List<Integer> ids) {
		this.showLoading();
		Map<String, String> params = new HashMap<String, String>();
		params.put("resType", mResType + "");
		params.put("ids", this.buildIds(ids));
		this.getHttpApi().request(GlobalConfig.URL_COURSE_EXAM_LIST, params,
				this, new RequestTag(mReqTag));
	}

	private String buildIds(List<Integer> ids) {
		JSONArray jsonArr = new JSONArray();
		if (null != ids) {
			int size = ids.size();
			for (int i = 0; i < size; i++) {
				jsonArr.put(ids.get(i));
			}
		}
		return jsonArr.toString();
	}

	private List<Integer> getIdsByType(int type) {
		List<Integer> tempIds = null;
		if (Resource.TYPE_EXAM == type) {
			tempIds = mCourse.getExamIds();
		} else if (Resource.TYPE_HOMEWORK == type) {
			tempIds = mCourse.getHomeworkIds();
		}
		return tempIds;
	}

}
