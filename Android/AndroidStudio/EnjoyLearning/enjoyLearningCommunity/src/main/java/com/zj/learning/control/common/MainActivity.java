package com.zj.learning.control.common;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.widget.RadioButton;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.asks.AsksIndexFragment;
import com.zj.learning.control.common.base.BaseFragmentActivity;
import com.zj.learning.control.course.CourseIndexFragment;
import com.zj.learning.control.dynamic.DynamicIndexFragment;
import com.zj.learning.control.forum.ForumIndexFragment;
import com.zj.learning.control.user.UserIndexFragment;
import com.zj.learning.dao.CoreSharedPreference;
import com.zj.learning.model.user.User;

/**
 * 项目主页，Android项目配置为默认加载页
 * 
 * @author XingRongJing
 * 
 */
public class MainActivity extends BaseFragmentActivity implements
		OnTabChangeListener {

	private TabWidget mTabWidget;
	private FragmentTabHost mTabHost;
	private int mTabIndex = -1;
	private boolean mDoubleExit = false;

	@Override
	protected void prepareViews() {
		// mInflater = this.getLayoutInflater();
		setContentView(R.layout.main);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, this.getSupportFragmentManager(), R.id.content);
		mTabWidget = mTabHost.getTabWidget();

		// 接收处理Tab页切换事件
		mTabHost.setOnTabChangedListener(this);
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		super.prepareDatas(savedInstanceState);
//		User user = CoreSharedPreference.getUser(this);
		User user = CoreSharedPreference.getObject(this, GlobalConfig.SharedPreferenceDao.FILENAME_USER, GlobalConfig.SharedPreferenceDao.KEY_USER);
		this.setUser(user);

		Intent intent = getIntent();
		mTabIndex = intent.getIntExtra("tabId", -1);	// 获得当前Tab页序号,默认-1表示尚未设置
		this.addTabItem(R.drawable.main_bottom_course_selector,
				CourseIndexFragment.class);// 课程
		this.addTabItem(R.drawable.main_bottom_asks_selector,
				AsksIndexFragment.class);	// 问答
		this.addTabItem(R.drawable.main_bottom_forum_selector,
				ForumIndexFragment.class);	// 论坛
		this.addTabItem(R.drawable.main_bottom_dynamic_selector,
				DynamicIndexFragment.class);	// 动态
		this.addTabItem(R.drawable.main_bottom_me_selector,
				UserIndexFragment.class);		// 我的

		RadioButton rBtn = null;	// 下方一排Tab页按钮
		if (mTabIndex == -1) {
			// 尚未设置,默认为第一个课程
			rBtn = (RadioButton) mTabWidget.getChildAt(0);
			rBtn.setChecked(true);
		} else {
			// 根据传来的参数设定Tab页位置
			int size = mTabWidget.getChildCount();
			if (mTabIndex < size && mTabIndex >= 0) {
				rBtn = (RadioButton) mTabWidget.getChildAt(mTabIndex);
				mTabHost.setCurrentTab(mTabIndex);
				rBtn.setChecked(true);
			}
		}
	}

	private void addTabItem(int resId, Class<?> cls) {
		RadioButton rbtn = (RadioButton) this.getLayoutInflater().inflate(
				R.layout.main_bottom_item, null);
		rbtn.setBackgroundResource(resId);
		TabSpec tabSpec = mTabHost.newTabSpec(cls.getName()).setIndicator(rbtn);
		mTabHost.addTab(tabSpec, cls, null);
	}

	@Override
	public void onTabChanged(String tabId) {
		// TODO Auto-generated method stub
		for (int i = 0; i < mTabWidget.getChildCount(); i++) {
			RadioButton rBtn = (RadioButton) mTabWidget.getChildAt(i);
			if (mTabHost.getCurrentTab() == i) {
				rBtn.setChecked(true);
			} else {
				rBtn.setChecked(false);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (mDoubleExit) {
			super.onBackPressed();
			android.os.Process.killProcess(android.os.Process.myPid());
			return;
		}
		mDoubleExit = true;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				mDoubleExit = false;
			}
		}, 2000);
		this.showToast(this.getString(R.string.exit));
	}

}
