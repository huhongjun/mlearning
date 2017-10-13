package com.zj.learning.model.course;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 资源实体基类，可分为试题、作业等资源
 * 
 * @author XingRongJing
 * @see CourseResource
 * 
 */
public class Resource extends ItemSingle {

	/** 资源类型-课程 **/
	public static final int TYPE_COURSE = 0;
	/** 资源类型-试题 **/
	public static final int TYPE_EXAM = 1;
	/** 资源类型-作业 **/
	public static final int TYPE_HOMEWORK = 2;

	private int id;
	/** 课程id，如无则表示资源独立于课程存在 **/
	private int courseId;
	private int resType = TYPE_COURSE;
	private String resName;
	/** 资源文件名-具体的资源在此文件中 **/
	private String resFileName;
	/** 资源封面图-相对路径 **/
	private String resImage;
	private String resDesc;
	/** 建议时长 **/
	private String time;

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

	public int getResType() {
		return resType;
	}

	public void setResType(int resType) {
		this.resType = resType;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public String getResFileName() {
		return resFileName;
	}

	public void setResFileName(String resFileName) {
		this.resFileName = resFileName;
	}

	public String getResDesc() {
		return resDesc;
	}

	public void setResDesc(String resDesc) {
		this.resDesc = resDesc;
	}

	public String getResImage() {
		return resImage;
	}

	public void setResImage(String resImage) {
		this.resImage = resImage;
	}

	public boolean isCourseResource(int resType) {
		return TYPE_COURSE == resType;
	}

	public boolean isExamResource(int resType) {
		return TYPE_EXAM == resType;
	}

	public boolean isHomeworkResource(int resType) {
		return TYPE_HOMEWORK == resType;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Resource toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Resource.class);
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.newCellFromXml(context, R.layout.course_common_list_item,
				root);
	}

}
