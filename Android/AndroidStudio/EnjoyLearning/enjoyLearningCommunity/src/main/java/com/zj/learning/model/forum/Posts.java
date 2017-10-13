package com.zj.learning.model.forum;

import android.content.Context;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

public class Posts extends ItemSingle {
	private String subject;// 帖子标题
	private String author;// 帖子作者
	private int reples;// 帖子回复数量
	private int fid;// 帖子标签id
	private String tagName;// 帖子标签名称
	private int tid;// 帖子id
	private String message;// 主贴内容
	private String loveNum;// 点赞数量
	private long dateline;// 发帖时间
	private String nname;// 匿名名字

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getReples() {
		return reples;
	}

	public void setReples(int reples) {
		this.reples = reples;
	}

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getLoveNum() {
		return loveNum;
	}

	public void setLoveNum(String loveNum) {
		this.loveNum = loveNum;
	}

	public long getDateline() {
		return dateline;
	}

	public void setDateline(long dateline) {
		this.dateline = dateline;
	}

	public String getNname() {
		return nname;
	}

	public void setNname(String nname) {
		this.nname = nname;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this, Posts.class);
	}

	public static Posts toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Posts.class);
	}

	@Override
	public String toString() {
		return "Posts [subject=" + subject + ", author=" + author + ", reples="
				+ reples + ", fid=" + fid + ", tagName=" + tagName + ", tid="
				+ tid + ", message=" + message + ", loveNum=" + loveNum
				+ ", dateline=" + dateline + ", nname=" + nname + "]";
	}

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		return this.newCellFromXml(context, R.layout.forum_list_item, root);
	}

}
