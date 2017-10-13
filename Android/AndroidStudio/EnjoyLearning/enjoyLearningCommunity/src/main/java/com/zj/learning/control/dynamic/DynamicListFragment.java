package com.zj.learning.control.dynamic;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.zj.learning.R;
import com.zj.learning.control.common.base.BaseRefreshListFragment;
import com.zj.learning.model.dynamic.Dynamic;
import com.zj.learning.view.dynamic.DynamicItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 我的动态/全部动态界面
 * 
 * @author XingRongJing
 * 
 */
public class DynamicListFragment extends BaseRefreshListFragment {

	private List<Dynamic> mDynamics;
	private int mOperatorType = -1;
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (DynamicListFragment.this.isMyDynamic()) {
				DynamicListFragment.this.testMyDynamic();
			} else {
				DynamicListFragment.this.testAllDynamic();
			}
			DynamicListFragment.this.onResponse(null);
		};
	};

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		mOperatorType = args.getInt("operatorType");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mDynamics = new ArrayList<Dynamic>();
		mAdapter = new ItemSingleAdapter<DynamicItemView, Dynamic>(
				this.getActivity(), mDynamics);
	}

	private void testMyDynamic() {
		Dynamic d = new Dynamic();
		d.setType(Dynamic.TYPE_USER);
		d.setEventType(Dynamic.EVENT_TYPE_USER_ADD);
		d.setUsername("我");
		d.setEventTitle("加入享学吧");

		Dynamic d1 = new Dynamic();
		d1.setType(Dynamic.TYPE_COURSE);
		d1.setEventType(Dynamic.EVENT_TYPE_COURSE_LEARN);
		d1.setUsername("我");
		d1.setEventTitle("如何学好Android开发之精品课程");

		Dynamic d2 = new Dynamic();
		d2.setType(Dynamic.TYPE_COURSE);
		d2.setEventType(Dynamic.EVENT_TYPE_COURSE_FOCUS);
		d2.setUsername("我");
		d2.setEventTitle("如何学习IOS开发");

		Dynamic d3 = new Dynamic();
		d3.setType(Dynamic.TYPE_ASKS);
		d3.setEventType(Dynamic.EVENT_TYPE_ASKS_ADD);
		d3.setUsername("我");
		d3.setEventTitle("怎么提供学习力？如何在职场走的更好、更远");

		Dynamic d4 = new Dynamic();
		d4.setType(Dynamic.TYPE_ASKS);
		d4.setEventType(Dynamic.EVENT_TYPE_ASKS_ANSWER);
		d4.setUsername("我");
		d4.setEventTitle("怎么提供学习力？如何在职场走的更好、更远");

		mDynamics.add(d);
		mDynamics.add(d1);
		mDynamics.add(d2);
		mDynamics.add(d3);
		mDynamics.add(d4);

	}

	private void testAllDynamic() {

		Dynamic d01 = new Dynamic();
		d01.setType(Dynamic.TYPE_FORUM);
		d01.setEventType(Dynamic.EVENT_TYPE_FORUM_ADD);
		d01.setUsername("小不懂");
		d01.setEventTitle("分享我的15个学习方法");

		Dynamic d0 = new Dynamic();
		d0.setType(Dynamic.TYPE_USER);
		d0.setEventType(Dynamic.EVENT_TYPE_USER_ADD);
		d0.setUsername("前程无忧笑天下");
		d0.setEventTitle("加入享学吧");

		Dynamic d = new Dynamic();
		d.setType(Dynamic.TYPE_USER);
		d.setEventType(Dynamic.EVENT_TYPE_USER_ADD);
		d.setUsername("张三");
		d.setEventTitle("加入享学吧");

		Dynamic d1 = new Dynamic();
		d1.setType(Dynamic.TYPE_COURSE);
		d1.setEventType(Dynamic.EVENT_TYPE_COURSE_LEARN);
		d1.setUsername("李四");
		d1.setEventTitle("如何学好Android开发之精品课程");

		Dynamic d2 = new Dynamic();
		d2.setType(Dynamic.TYPE_COURSE);
		d2.setEventType(Dynamic.EVENT_TYPE_COURSE_FOCUS);
		d2.setUsername("匿名小知知");
		d2.setEventTitle("如何学习IOS开发");

		Dynamic d3 = new Dynamic();
		d3.setType(Dynamic.TYPE_ASKS);
		d3.setEventType(Dynamic.EVENT_TYPE_ASKS_ADD);
		d3.setUsername("昨日像那东流水");
		d3.setEventTitle("怎么提供学习力？如何在职场走的更好、更远");

		Dynamic d4 = new Dynamic();
		d4.setType(Dynamic.TYPE_ASKS);
		d4.setEventType(Dynamic.EVENT_TYPE_ASKS_ANSWER);
		d4.setUsername("我");
		d4.setEventTitle("怎么提供学习力？如何在职场走的更好、更远");

		mDynamics.add(d01);
		mDynamics.add(d0);
		mDynamics.add(d);
		mDynamics.add(d1);
		mDynamics.add(d2);
		mDynamics.add(d3);
		mDynamics.add(d4);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.dynamic_list, null);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.dynamic_list_listview);
		mLlLoading = (LinearLayout) view
				.findViewById(R.id.dynamic_list_loading);
		mListView.setAdapter(mAdapter);

		this.prepareByView();
		System.out.println(" DynamicListFragment  isLoaded = "
				+ this.isLoaded() + " operator = " + this.mOperatorType);
		// 如果是我的动态且未加载（全部动态数据加载见@DynamicIndexFragment）
		if (this.isMyDynamic() && !this.isLoaded()) {
			this.fetchCacheDatas();
		}
		mListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// Dynamic Dynamic = (Dynamic) parent.getAdapter().getItem(position);
		// Intent intent = new Intent(this.getActivity(),
		// DynamicDetailActivity.class);
		// intent.putExtra("Dynamic", Dynamic.toJson());
		// this.startActivity(intent);
	}

	@Override
	protected void fetchCacheDatas() {
		// TODO Auto-generated method stub
		// Step 1：先看缓存
		this.showLoading();
		if (this.isMyDynamic()) {
			this.getCacheManager().getCacheByUrl(this,
					new RequestTag(this.hashCode()), "MyDynamicCacheUrl");
		} else {
			this.getCacheManager().getCacheByUrl(this,
					new RequestTag(this.hashCode()), "AllDynamicCacheUrl");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleSuccessDatas(List<?> results) {
		// TODO Auto-generated method stub
		if (null != results) {
			List<Dynamic> temps = (List<Dynamic>) results;
			mDynamics.addAll(temps);
		}
		this.notifyDataChanged();
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		// TODO Auto-generated method stub
		// RequestTag tag = new RequestTag(mReqTag);
		mHandler.sendEmptyMessageDelayed(1, 4000);
	}

	@Override
	protected boolean isDataEmpty() {
		// TODO Auto-generated method stub
		return (null == mDynamics ? false : mDynamics.isEmpty());
	}

	@Override
	protected void clearDatas() {
		// TODO Auto-generated method stub
		if (null != mDynamics) {
			mDynamics.clear();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		this.setIsLoaded(true);
		// Step 3.1：加载远程数据完成
		// Object result = arg0.result;
		// if (null == result) {
		// return;
		// }
		// List<Dynamic> Dynamics = (List<Dynamic>) result;
		this.handleFetchDataSuccess(mDynamics);
	}

	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseCache(out);
		// Step 2：获取缓存成功，加载网络数据
		if (out.isSuccess) {// 有缓存
			RequestDataParser parser = new RequestDataParser(out.resp);
			if (parser.isSuccess()) {// 操作成功
				List<Dynamic> resList = parser.getArray(Dynamic.class);
				this.handleFetchCacheComplete(resList);
			}
		}
	}

	public boolean isMyDynamic() {
		return (Dynamic.OPERATOR_DYNAMIC_ME == mOperatorType);
	}

}
