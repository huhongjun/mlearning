package com.zj.learning.control.user;

import android.os.Bundle;

import com.zj.learning.R;
import com.zj.learning.control.common.base.BaseTabActivity;
import com.zj.learning.model.course.Course;

/**
 * 我的课程/我关注的课程主页
 * 
 * @author XingRongJing
 * 
 */
public class UserCourseIndexActivity extends BaseTabActivity {

	private static final int PAGE_COURSE_FOCUS_ME = 1;

	@Override
	protected void prepareViewsChanged() {
		// TODO Auto-generated method stub
		this.setTitleRBtnText(this.getString(R.string.user_index_course_me),
				this.getString(R.string.user_index_course_focus_me));
//		this.setRightVisible(this.getString(R.string.certificate_rule));
	}

	@Override
	protected void prepareFragments() {
		// TODO Auto-generated method stub
		this.adddFragment(this
				.buildCourseUserFragment(Course.OPERATOR_COURSE_ME));
		this.adddFragment(this
				.buildCourseUserFragment(Course.OPERATOR_COURSE_FOCUS_ME));
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		if (PAGE_COURSE_FOCUS_ME == arg0) {
			this.setRightCheck();
			CourseUserListFragment myCourse = (CourseUserListFragment) this
					.getFragmentByIndex(arg0);
			if (null != myCourse) {
				myCourse.onSelectedFoucsCourse();
			}
		} else {
			this.setLeftCheck();
		}
	}

	private CourseUserListFragment buildCourseUserFragment(int operator) {
		CourseUserListFragment fragment = new CourseUserListFragment();
		Bundle args = new Bundle();
		args.putInt("operator", operator);
		fragment.setArguments(args);
		return fragment;
	}

}
