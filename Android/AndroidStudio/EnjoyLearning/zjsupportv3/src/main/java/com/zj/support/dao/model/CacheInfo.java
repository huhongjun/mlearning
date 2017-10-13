package com.zj.support.dao.model;

public class CacheInfo {

	private Integer id;
	private String url;
	private String jsonString;
	private String updateTime;

	public CacheInfo() {
	}

	public CacheInfo(String url, String jsonStr, String updateTime) {
		this.url = url;
		this.jsonString = jsonStr;
		this.updateTime = updateTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return "CacheInfo [id=" + id + ", url=" + url + ", jsonString="
				+ jsonString + ", updateTime=" + updateTime + "]";
	}

}
