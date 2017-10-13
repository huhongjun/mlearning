package com.zj.learning.control.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.control.common.LoginActivity;
import com.zj.learning.control.common.base.BaseFragment;
import com.zj.support.widget.ZjImageView;

/**
 * 我
 * 
 * @author XingRongJing
 * 
 */
public class UserIndexFragment extends BaseFragment implements OnClickListener {

	private static final int REQ_CODE_LOGIN = 1;
	private static final int REQ_CODE_USER_COURSE = 2;
	private RelativeLayout mRlUnlogin, mRllogin, mRlCourseFocusMe, mRlAsksMe,
			mRlForumMe, mRlCetificateMe;
	private TextView mTvSetting, mTvCetificate, mTvUsername, mTvLevel,
			mTvNextLevel;
	private ZjImageView mIvAvator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.user_index, null);
		mRlUnlogin = (RelativeLayout) view
				.findViewById(R.id.user_index_rl_user_info_before);
		mRllogin = (RelativeLayout) view
				.findViewById(R.id.user_index_rl_user_info);
		mRlCourseFocusMe = (RelativeLayout) view
				.findViewById(R.id.user_index_rl_course_focus_me);
		mRlAsksMe = (RelativeLayout) view
				.findViewById(R.id.user_index_rl_asks_me);
		mRlForumMe = (RelativeLayout) view
				.findViewById(R.id.user_index_rl_forum_me);
		mRlCetificateMe = (RelativeLayout) view
				.findViewById(R.id.user_index_rl_cetificate_me);
		mIvAvator = (ZjImageView) view.findViewById(R.id.user_index_iv_avator);
		mTvSetting = (TextView) view.findViewById(R.id.user_index_tv_setting);
		mTvCetificate = (TextView) view
				.findViewById(R.id.user_index_tv_cetificate);
		mTvUsername = (TextView) view.findViewById(R.id.user_index_tv_username);
		mTvLevel = (TextView) view.findViewById(R.id.user_index_tv_level);
		mTvNextLevel = (TextView) view
				.findViewById(R.id.user_index_tv_next_level);

		mRlUnlogin.setOnClickListener(this);
		mRllogin.setOnClickListener(this);
		mRlCourseFocusMe.setOnClickListener(this);
		mRlAsksMe.setOnClickListener(this);
		mRlForumMe.setOnClickListener(this);
		mRlCetificateMe.setOnClickListener(this);
		mTvSetting.setOnClickListener(this);
		mTvCetificate.setOnClickListener(this);
		if(this.isLogin()){
			this.showLoginUserInfo();
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.user_index_tv_setting:
			this.onClickSetting(v);
			break;
		case R.id.user_index_tv_cetificate:
			this.onClickCetificate(v);
			break;
		case R.id.user_index_rl_user_info_before:
			this.onClickUserInfoUnlogin(v);
			break;
		case R.id.user_index_rl_user_info:
			this.onClickUserInfo(v);
			break;
		case R.id.user_index_rl_course_focus_me:
			this.onClickCourseFocusMe(v);
			break;
		case R.id.user_index_rl_asks_me:
			this.onClickAsksMe(v);
			break;
		case R.id.user_index_rl_forum_me:
			this.onClickForumMe(v);
			break;
		case R.id.user_index_rl_cetificate_me:
			this.onClickCetificateMe(v);
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (Activity.RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case REQ_CODE_LOGIN:
			this.showLoginUserInfo();
			break;
		case REQ_CODE_USER_COURSE:// 我的课程/我关注的课程
			this.showLoginUserInfo();
			this.startUserCourse();
			break;
		}
	}

	private void onClickSetting(View view) {// 设置

	}

	private void onClickCetificate(View view) {// 证书专区

	}

	private void onClickUserInfoUnlogin(View view) {// 登录
		this.startLogin(REQ_CODE_LOGIN);
	}

	private void onClickUserInfo(View view) {// 用户信息修改

	}

	private void onClickCourseFocusMe(View view) {// 我的课程/我关注的课程
		if (this.isLogin()) {
			this.startUserCourse();
		} else {
			this.startLogin(REQ_CODE_USER_COURSE);
		}
	}

	private void onClickAsksMe(View view) {// 我的提问/回答

	}

	private void onClickForumMe(View view) {// 我的帖子/参与帖子

	}

	private void onClickCetificateMe(View view) {// 我的证书

	}

	private void startLogin(int requestCode) {
		this.startActivityForResult(new Intent(this.getActivity(),
				LoginActivity.class), requestCode);
	}

	private void startUserCourse() {
		this.startActivity(new Intent(this.getActivity(),
				UserCourseIndexActivity.class));
	}

	/**
	 * 登录成功后，显示用户信息
	 */
	private void showLoginUserInfo() {
		mRlUnlogin.setVisibility(View.GONE);
		mRllogin.setVisibility(View.VISIBLE);
		mTvNextLevel.setVisibility(View.VISIBLE);
	}

}
