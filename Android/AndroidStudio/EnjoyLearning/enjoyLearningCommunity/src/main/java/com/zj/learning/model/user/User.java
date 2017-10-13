package com.zj.learning.model.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userId;
	private String loginName;
	private String pswd;
	private String email;
	private String qq;
	/** 积分 **/
	private int totalScore = 0;
	/** 用户群-如NIIS、脊骨神经、游客 **/
	private int group;
	/** 存放用户头像的图片名称 **/
	private String standby0;
	/** 注册时间 **/
	private long registerDate;
	/** 我的课程id集 **/
	private List<Integer> myCourseIds;
	/** 我关注的课程id集 **/
	private List<Integer> myFocusIds;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPswd() {
		return pswd;
	}

	public void setPswd(String pswd) {
		this.pswd = pswd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public int getTotalScore() {
		return totalScore;
	}

	public void setTotalScore(int totalScore) {
		this.totalScore = totalScore;
	}

	public int getGroup() {
		return group;
	}

	public void setGroup(int group) {
		this.group = group;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getStandby0() {
		return standby0;
	}

	public void setStandby0(String standby0) {
		this.standby0 = standby0;
	}

	public long getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(long registerDate) {
		this.registerDate = registerDate;
	}

	public List<Integer> getMyCourseIds() {
		return myCourseIds;
	}

	public void setMyCourseIds(List<Integer> myCourseIds) {
		this.myCourseIds = myCourseIds;
	}

	public List<Integer> getMyFocusIds() {
		return myFocusIds;
	}

	public void setMyFocusIds(List<Integer> myFocusIds) {
		this.myFocusIds = myFocusIds;
	}

	public void addMyCourse(int courseId) {
		if (this.isMyCourse(courseId)) {
			return;
		}
		if (null == myCourseIds) {
			myCourseIds = new ArrayList<Integer>();
			myCourseIds.add(courseId);
			this.setMyCourseIds(myCourseIds);
		} else {
			myCourseIds.add(courseId);
		}
	}

	public void addMyFocusCourse(int courseId) {
		if (this.isMyFocusCourse(courseId)) {
			return;
		}
		if (null == myFocusIds) {
			myFocusIds = new ArrayList<Integer>();
			myFocusIds.add(courseId);
			this.setMyFocusIds(myFocusIds);
		} else {
			myFocusIds.add(courseId);
		}
	}

	public boolean isMyCourse(int courseId) {
		if (null != myCourseIds) {
			return myCourseIds.contains(courseId);
		}
		return false;
	}

	public boolean isMyFocusCourse(int courseId) {
		if (null != myFocusIds) {
			return myFocusIds.contains(courseId);
		}
		return false;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static User toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, User.class);
	}
}
