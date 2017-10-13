package com.zj.learning.model.asks;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 问题评论实体
 */
public class Comment extends ItemSingle {

	private int id;
	/** 回答id **/
	private int answerId;
	private String userName;
	private String message;
	private long addTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getAddTime() {
		return addTime;
	}

	public void setAddTime(long addTime) {
		this.addTime = addTime;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this, Comment.class);
	}

	public static Comment toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Comment.class);
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.newCellFromXml(context, R.layout.asks_comment_list_item,
				root);
	}

}
