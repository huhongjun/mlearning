package com.zj.learning.model.asks;

import java.util.HashMap;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 问题实体
 * 
 * @author XingRongJing
 * 
 */
public class Question extends ItemSingle {

	public static final int OPERATOR_ALL = 1;
	public static final int OPERATOR_COMMON = OPERATOR_ALL + 1;
	public static final int OPERATOR_ME = OPERATOR_COMMON + 1;
	public static final int OPERATOR_FIND = OPERATOR_ME + 1;

	private int questionId;
	private String questionContent;
	private String userName;
	private String fileName;
	/** 回答数量 **/
	private int answerCount;
	private long addTime;
	private boolean anonymous;

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getQuestionContent() {
		return questionContent;
	}

	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this, Question.class);
	}

	public static String toJson(HashMap<String, Object> jsonMap) {
		Gson gson = new Gson();
		String mapToJson = gson.toJson(jsonMap);
		return mapToJson;
	}

	public static Question toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Question.class);
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		return this.newCellFromXml(context, R.layout.asks_list_item, root);
	}

	@Override
	public String toString() {
		return "Question [questionId=" + questionId + ", questionContent="
				+ questionContent + ", userName=" + userName + ", fileName="
				+ fileName + ", answerCount=" + answerCount + ", addTime="
				+ addTime + ", anonymous=" + anonymous + "]";
	}

}
