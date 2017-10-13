package com.zj.learning.control.course;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;

import com.zj.learning.R;
import com.zj.learning.control.common.base.BaseTabFragment;

/**
 * 课程主页
 * 
 * @author XingRongJing
 * 
 */
public class CourseIndexFragment extends BaseTabFragment {

	private static final int PAGE_COURSE_OFFLINE = 1;

	@Override
	protected void prepareFragments() {
		this.adddFragment(new CourseOnlineListFragment());
		this.adddFragment(new CourseOfflineListFragment());
	}

	@Override
	protected void prepareViewsChanged() {
		// TODO Auto-generated method stub
		super.prepareViewsChanged();
		this.setTitleRBtnText(this.getString(R.string.course_all),
				this.getString(R.string.course_me));
		// this.setTitleIcon(resIdLeft, resIdRight);
	}

	@Override
	public void onPageSelected(int arg0) {
		this.setCurrPage(arg0);
		if (PAGE_COURSE_OFFLINE == arg0) {
			this.setRightCheck();
			CourseOfflineListFragment myCourse = (CourseOfflineListFragment) this
					.getFragmentByIndex(arg0);
			if (null != myCourse) {
				myCourse.onSelected();
			}
		} else {
			this.setLeftCheck();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = this.getFragmentByIndex(this.getCurrPage());
		if (null != fragment) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onTitleLeftClick(View view) {
		// TODO Auto-generated method stub
		super.onTitleLeftClick(view);
		this.startActivity(new Intent(this.getActivity(),
				CourseRecordListActivity.class));
	}

	@Override
	protected void onTitleRightClick(View view) {// 课程搜索
		// TODO Auto-generated method stub
		super.onTitleRightClick(view);
		this.startActivity(new Intent(this.getActivity(),
				CourseSeachListActivity.class));
	}
}
