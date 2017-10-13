package com.zj.learning.model.course;

import com.google.gson.Gson;

/**
 * 学习记录
 * @author XingRongJing
 *
 */
public class LearnRecord {

	private int id;
	private int courseId;
	/** 资源id **/
	private int resId ;
	/** 学习类型-视频、作业、考试等 **/
	private int learnType = LEARN_TYPE_VIDEO;
	private String courseName;
	private String resName;
	/** 开始学习时间（秒） **/
	private long beginTime;
	/**  结束学习时间（秒） **/
	private long endTime;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCourseId() {
		return courseId;
	}
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public int getLearnType() {
		return learnType;
	}
	public void setLearnType(int learnType) {
		this.learnType = learnType;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public long getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(long beginTime) {
		this.beginTime = beginTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static LearnRecord toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, LearnRecord.class);
	}
	public static final int LEARN_TYPE_VIDEO = 0; 
	public static final int LEARN_TYPE_HOMEWORK = 1; 
	public static final int LEARN_TYPE_EXAM = 2; 
}
