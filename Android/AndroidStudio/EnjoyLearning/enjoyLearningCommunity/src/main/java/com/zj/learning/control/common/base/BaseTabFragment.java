package com.zj.learning.control.common.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

import com.zj.learning.R;
import com.zj.learning.control.common.adapter.TabFragmentAdapter;

/**
 * 主页两标签切换基本Fragment（课程、问答、论坛等）
 * 
 * @author XingRongJing
 * 
 */
public abstract class BaseTabFragment extends BaseFragment implements
		OnPageChangeListener, OnCheckedChangeListener, OnClickListener {

	private RadioGroup mRGroup;
	private RadioButton mRBtnLeft, mRBtnRight;
	private RelativeLayout mRlRight;
	private ImageView mIvTitleLeft, mIvTitleRight;
	private ViewPager mViewPager;
	private TabFragmentAdapter mFragmentAdapter;
	private List<Fragment> mFragments;
	private int mCurrPage = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mFragments = new ArrayList<Fragment>();
		this.prepareFragments();
		mFragmentAdapter = new TabFragmentAdapter(
				this.getChildFragmentManager(), mFragments);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.tab_fragment_index, null);
		mViewPager = (ViewPager) view
				.findViewById(R.id.tab_fragment_index_viewpager);
		mRGroup = (RadioGroup) view
				.findViewById(R.id.titlebar_rbtn_rg_titlebar);
		mRBtnLeft = (RadioButton) view
				.findViewById(R.id.titlebar_rbtn_radiobtn_left);
		mRBtnRight = (RadioButton) view
				.findViewById(R.id.titlebar_rbtn_radiobtn_right);
		mIvTitleLeft = (ImageView) view
				.findViewById(R.id.titlebar_rbtn_iv_left);
		mIvTitleRight = (ImageView) view
				.findViewById(R.id.titlebar_rbtn_iv_right);
		mRlRight = (RelativeLayout) view
				.findViewById(R.id.titlebar_rbtn_rl_right);
		mViewPager.setAdapter(mFragmentAdapter);

		mIvTitleLeft.setOnClickListener(this);
		mIvTitleRight.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
		mRGroup.setOnCheckedChangeListener(this);
		mViewPager.setOffscreenPageLimit(0);
		this.prepareViewsChanged();
		return view;
	}

	protected abstract void prepareFragments();

	protected void prepareViewsChanged() {

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.titlebar_rbtn_radiobtn_left:
			mViewPager.setCurrentItem(0, true);
			break;
		case R.id.titlebar_rbtn_radiobtn_right:
			mViewPager.setCurrentItem(1, true);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.titlebar_rbtn_iv_left:
			this.onTitleLeftClick(v);
			break;
		case R.id.titlebar_rbtn_iv_right:
			this.onTitleRightClick(v);
			break;
		}
	}

	/**
	 * 设置顶部RadioButton字样
	 * 
	 * @param left
	 * @param right
	 */
	protected void setTitleRBtnText(String left, String right) {
		if (null != mRBtnLeft) {
			mRBtnLeft.setText(left);
		}
		if (null != mRBtnRight) {
			mRBtnRight.setText(right);
		}
	}

	/**
	 * 设置顶部靠右两个图标的资源图
	 * 
	 * @param resIdLeft
	 * @param resIdRight
	 */
	protected void setTitleIcon(int resIdLeft, int resIdRight) {
		if (null != mIvTitleLeft) {
			mIvTitleLeft.setImageResource(resIdLeft);
		}
		if (null != mIvTitleRight) {
			mIvTitleRight.setImageResource(resIdRight);
		}
	}

	protected void hideTitlebarRight() {
		if (null != mRlRight) {
			mRlRight.setVisibility(View.GONE);
		}
	}

	protected void setLeftCheck() {
		if (null != mRBtnLeft) {
			mRBtnLeft.setChecked(true);
		}
	}
	
	protected void setRightCheck() {
		if (null != mRBtnRight) {
			mRBtnRight.setChecked(true);
		}
	}

	/**
	 * 点击顶部相对靠左边的按钮
	 * 
	 * @param view
	 */
	protected void onTitleLeftClick(View view) {

	}

	/**
	 * 点击顶部相对靠右边的按钮
	 * 
	 * @param view
	 */
	protected void onTitleRightClick(View view) {

	}

	public int getCurrPage() {
		return mCurrPage;
	}

	public void setCurrPage(int mCurrPage) {
		this.mCurrPage = mCurrPage;
	}

	protected void adddFragment(Fragment fragment) {
		if (null == mFragments) {
			return;
		}
		mFragments.add(fragment);
	}

	protected Fragment getFragmentByIndex(int index) {
		return null == mFragments ? null : mFragments.get(index);
	}

}
