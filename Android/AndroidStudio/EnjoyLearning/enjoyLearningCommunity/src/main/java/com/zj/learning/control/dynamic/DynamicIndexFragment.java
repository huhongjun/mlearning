package com.zj.learning.control.dynamic;

import android.os.Bundle;

import com.zj.learning.R;
import com.zj.learning.control.common.base.BaseTabFragment;
import com.zj.learning.model.dynamic.Dynamic;

/**
 * 动态Tab的主页
 * 
 * @author XingRongJing
 * 
 */
public class DynamicIndexFragment extends BaseTabFragment {

	@Override
	protected void prepareFragments() {
		// TODO Auto-generated method stub
		this.adddFragment(this
				.newDynamicListFragment(Dynamic.OPERATOR_DYNAMIC_ME));
		this.adddFragment(this
				.newDynamicListFragment(Dynamic.OPERATOR_DYNAMIC_ALL));
	}

	@Override
	protected void prepareViewsChanged() {
		// TODO Auto-generated method stub
		super.prepareViewsChanged();
		this.setTitleRBtnText(this.getString(R.string.dynamic_me),
				this.getString(R.string.dynamic_all));
		this.hideTitlebarRight();
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		DynamicListFragment fragment = (DynamicListFragment) this
				.getFragmentByIndex(arg0);
		// 如果是全部动态且未加载（我的动态数据加载见@CourseListFragment）
		if (null != fragment && !fragment.isMyDynamic() && !fragment.isLoaded()) {
			fragment.fetchCacheDatas();
		}
	}

	private DynamicListFragment newDynamicListFragment(int operatorType) {
		DynamicListFragment fragment = new DynamicListFragment();
		Bundle meArags = new Bundle();
		meArags.putInt("operatorType", operatorType);
		fragment.setArguments(meArags);
		return fragment;
	}

}
