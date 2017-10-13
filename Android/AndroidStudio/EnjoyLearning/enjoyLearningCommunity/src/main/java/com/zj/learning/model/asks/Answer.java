package com.zj.learning.model.asks;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 问题回答实体
 */
public class Answer extends ItemSingle {

	public static final int TYPE_ITEM_FIRST = 1;
	private int answerId;
	/** 问题id **/
	private int questionId;
	private String userName;
	private String answerContent;
	private String thumb;
	private long addTime;
	/** 评论数量 **/
	private int commentCount;
	private boolean anonymous;

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAnswerContent() {
		return answerContent;
	}

	public void setAnswerContent(String answerContent) {
		this.answerContent = answerContent;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this, Answer.class);
	}

	public static Answer toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Answer.class);
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		return this.newCellFromXml(context, R.layout.asks_answer_list_item,
				root);
	}

}
