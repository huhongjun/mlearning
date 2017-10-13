package com.zj.learning.model.forum;

import com.google.gson.Gson;

public class Tag {
	private int fid;// 标签id
	private String name;// 标签名字

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this, Tag.class);
	}

	public static Tag toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Tag.class);
	}

	public Tag(int fid, String name) {
		super();
		this.fid = fid;
		this.name = name;
	}

}
