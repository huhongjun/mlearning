package com.zj.learning.model.course;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 课程实体
 * 
 * @author XingRongJing
 * @see CourseResource
 */
public class Course extends ItemSingle {

	private static final String ID_SEPERATOR = ",";
	public static final int OPERATOR_COURSE_ME = 1;
	public static final int OPERATOR_COURSE_FOCUS_ME = 2;
	private int courseId;
	/** 缩略图地址 **/
	private String resImage;
	private String resName;
	/** 章节信息文件名 **/
	private String resFileName;
	private String desc;
	/** 价格 **/
	private float price;
	/** 考试id集，逗号分隔 **/
	private String questionId;
	/** 作业id集，逗号分隔 **/
	private String taskId;
	private List<Integer> examIds;
	private List<Integer> homeworkIds;
	// private CourseResource resource;
	/** 总评分、知识量评分、趣味性评分、课程设计评分 **/
	private float avgScore, avg0, avg1, avg2;
	/** 学习人数、关注人数、评价数量、问题数量 **/
	private int learnedNum, focusNum, commentUserNum, asksCount;
	/** 是否在线课程 **/
	private boolean isOnline = true;

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getResImage() {
		return resImage;
	}

	public void setResImage(String resImage) {
		this.resImage = resImage;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public float getAvgScore() {
		return avgScore;
	}

	public void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	public float getAvg0() {
		return avg0;
	}

	public void setAvg0(float avg0) {
		this.avg0 = avg0;
	}

	public float getAvg1() {
		return avg1;
	}

	public void setAvg1(float avg1) {
		this.avg1 = avg1;
	}

	public float getAvg2() {
		return avg2;
	}

	public void setAvg2(float avg2) {
		this.avg2 = avg2;
	}

	public int getLearnedNum() {
		return learnedNum;
	}

	public void setLearnedNum(int learnedNum) {
		this.learnedNum = learnedNum;
	}

	public int getFocusNum() {
		return focusNum;
	}

	public void setFocusNum(int focusNum) {
		this.focusNum = focusNum;
	}

	public int getCommentUserNum() {
		return commentUserNum;
	}

	public void setCommentUserNum(int commentUserNum) {
		this.commentUserNum = commentUserNum;
	}

	public int getAsksCount() {
		return asksCount;
	}

	public void setAsksCount(int asksCount) {
		this.asksCount = asksCount;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public List<Integer> getExamIds() {
		if (TextUtils.isEmpty(questionId)) {
			return null;
		}
		if (null == this.examIds) {
			this.examIds = this.getIdsByString(questionId);
		}
		return examIds;
	}

	public List<Integer> getHomeworkIds() {
		if (TextUtils.isEmpty(taskId)) {
			return null;
		}
		if (null == this.homeworkIds) {
			this.homeworkIds = this.getIdsByString(taskId);
		}
		return this.homeworkIds;
		
	}

	public int getHomeworkCount() {
		return null == this.getHomeworkIds() ? 0 : homeworkIds.size();
	}

	public int getExamCount() {
		return null == this.getExamIds() ? 0 : examIds.size();
	}

	public boolean isFree() {
		return (0.0f == price);
	}

	private List<Integer> getIdsByString(String idsStr) {
		List<Integer> ids = new ArrayList<Integer>();
		String[] temps = idsStr.split(ID_SEPERATOR);
		if (null != temps) {
			for (String temp : temps) {
				try {
					int id = Integer.valueOf(temp);
					ids.add(id);
				} catch (Exception e) {
				}
			}
		}
		return ids;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Course toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Course.class);
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.newCellFromXml(context, R.layout.course_list_item, root);
	}
}
