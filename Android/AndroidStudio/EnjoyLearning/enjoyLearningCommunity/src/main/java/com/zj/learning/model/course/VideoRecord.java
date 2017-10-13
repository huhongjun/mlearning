package com.zj.learning.model.course;

import java.io.Serializable;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 视频播放记录
 * 
 * @author XingRongJing
 * 
 */
public class VideoRecord extends ItemSingle implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
	private int courseId;
	private int sectionId;
	/** 播放url **/
	private String videoUrl;
	private String sectionName;
	private String courseName;
	/** 当前时间记录 **/
	private long currPos;
	/** 总共时间记录 **/
	private long totalPos;
	/** 观看时间 **/
	private long stuDate;

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

	public int getSectionId() {
		return sectionId;
	}

	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public long getCurrPos() {
		return currPos;
	}

	public void setCurrPos(long currPos) {
		this.currPos = currPos;
	}

	public long getTotalPos() {
		return totalPos;
	}

	public void setTotalPos(long totalPos) {
		this.totalPos = totalPos;
	}

	public long getStuDate() {
		return stuDate;
	}

	public void setStuDate(long stuDate) {
		this.stuDate = stuDate;
	}

	public boolean isVideoFinish() {
		return (totalPos - currPos) / 1000 < 3;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static VideoRecord toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, VideoRecord.class);
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.newCellFromXml(context, R.layout.course_record_list_item,
				root);
	}

}
