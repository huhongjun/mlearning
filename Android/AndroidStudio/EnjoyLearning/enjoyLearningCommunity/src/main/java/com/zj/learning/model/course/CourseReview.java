package com.zj.learning.model.course;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 课程评论
 * 
 * @author XingRongJing
 * 
 */
public class CourseReview extends ItemSingle {

	private int ccId;
	private int userId;
	private int courseId;
	private String userName;
	/** 知识量 **/
	private float flag0;
	/** 趣味性 **/
	private float flag1;
	/** 课程设计 **/
	private float flag2;
	/** 评价人状态-默认学习中 **/
	private int stuState = STATUS_LEARN_ING;
	private String content;
	private long commentTime;

	public int getCcId() {
		return ccId;
	}

	public void setCcId(int ccId) {
		this.ccId = ccId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public float getFlag0() {
		return flag0;
	}

	public void setFlag0(float flag0) {
		this.flag0 = flag0;
	}

	public float getFlag1() {
		return flag1;
	}

	public void setFlag1(float flag1) {
		this.flag1 = flag1;
	}

	public float getFlag2() {
		return flag2;
	}

	public void setFlag2(float flag2) {
		this.flag2 = flag2;
	}

	public int getStuState() {
		return stuState;
	}

	public void setStuState(int stuState) {
		this.stuState = stuState;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(long commentTime) {
		this.commentTime = commentTime;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static CourseReview toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, CourseReview.class);
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return newCellFromXml(context, R.layout.course_review_list_item, root);
	}

	/** 状态-正在学 **/
	public static final int STATUS_LEARN_ING = 0;
	/** 状态-已学完 **/
	public static final int STATUS_LEARN_COMPLETE = 1;
	/** 状态-未能学完 **/
	public static final int STATUS_LEARN_GIVEUP = 2;
}
