package com.zj.learning.control.common.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabFragmentAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;

	public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.mFragments = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return (null == mFragments) ? null : mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return (null == mFragments) ? 0 : mFragments.size();
	}

}
