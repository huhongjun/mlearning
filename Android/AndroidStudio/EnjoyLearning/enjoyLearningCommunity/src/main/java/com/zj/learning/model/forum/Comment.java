package com.zj.learning.model.forum;

import java.util.Date;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

public class Comment extends ItemSingle {

	private String userImage; // 评论人头像
	private String message; // 评论内容
	private long dateline;// 评论时间
	private String nName;// 匿名
	private int tId;// 主贴id
	private String author;// 跟帖人登陆名

	public String getUserImage() {
		return userImage;
	}

	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getDateline() {
		return dateline;
	}

	public void setDateline(long dateline) {
		this.dateline = dateline;
	}

	public String getnName() {
		return nName;
	}

	public void setnName(String nName) {
		this.nName = nName;
	}

	public int gettId() {
		return tId;
	}

	public void settId(int tId) {
		this.tId = tId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
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
		return this.newCellFromXml(context, R.layout.forum_comment_list_item,
				root);
	}

	@Override
	public String toString() {
		return "Comment [userImage=" + userImage + ", message=" + message
				+ ", dateline=" + dateline + ", nName=" + nName + ", tId="
				+ tId + ", author=" + author + "]";
	}

}
