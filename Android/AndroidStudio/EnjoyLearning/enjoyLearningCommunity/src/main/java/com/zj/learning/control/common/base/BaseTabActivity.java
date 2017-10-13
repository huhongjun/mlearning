package com.zj.learning.control.common.base;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.control.common.adapter.TabFragmentAdapter;

/**
 * 主页两标签切换基本Activity（证书专区、我的提问/回答、我的帖子/参与的帖子等）
 * 
 * @author XingRongJing
 * 
 */
public abstract class BaseTabActivity extends BaseFragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener {

	private RadioGroup mRGroup;
	private RadioButton mRBtnLeft, mRBtnRight;
	private RelativeLayout mRlRight;
	private ImageView mIvBack;
	private TextView mTvRight;
	private ViewPager mViewPager;
	private TabFragmentAdapter mFragmentAdapter;
	private List<Fragment> mFragments;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.tab_fragment_index);
		mViewPager = (ViewPager) this
				.findViewById(R.id.tab_fragment_index_viewpager);
		mRGroup = (RadioGroup) this
				.findViewById(R.id.titlebar_rbtn_rg_titlebar);
		mRBtnLeft = (RadioButton) this
				.findViewById(R.id.titlebar_rbtn_radiobtn_left);
		mIvBack = (ImageView) this
				.findViewById(R.id.titlebar_back_iv_back);
		mRBtnRight = (RadioButton) this
				.findViewById(R.id.titlebar_rbtn_radiobtn_right);
		mTvRight = (TextView) this.findViewById(R.id.titlebar_rbtn_tv_right);
		mRlRight = (RelativeLayout) this
				.findViewById(R.id.titlebar_rbtn_rl_right);

		mIvBack.setVisibility(View.VISIBLE);
		mRlRight.setVisibility(View.GONE);
		

		mViewPager.setOnPageChangeListener(this);
		mRGroup.setOnCheckedChangeListener(this);
		
		this.prepareViewsChanged();
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		mFragments = new ArrayList<Fragment>();
		this.prepareFragments();
		mFragmentAdapter = new TabFragmentAdapter(
				this.getSupportFragmentManager(), mFragments);
		mViewPager.setAdapter(mFragmentAdapter);
	}

	protected abstract void prepareFragments();
	
	protected abstract void prepareViewsChanged();

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
	 * 设置顶部靠右字样
	 * 
	 * @param resIdLeft
	 * @param resIdRight
	 */
	protected void setRightVisible(String text) {
		if (null != mTvRight) {
			mTvRight.setVisibility(View.VISIBLE);
			mTvRight.setText(text);
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
	
	public void onClickBtnBack(View view){//返回
		this.finish();
	}

	/**
	 * 点击顶部靠右边的按钮
	 * 
	 * @param view
	 */
	public void onClickRightBtn(View view) {

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
