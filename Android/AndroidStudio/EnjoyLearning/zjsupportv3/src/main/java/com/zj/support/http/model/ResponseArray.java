package com.zj.support.http.model;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * 列表结果集响应实体
 * 
 * @author XingRongJing
 * 
 */
public class ResponseArray extends RequestTag {
	private static final int SUCCESS_CODE = 0;
	private int respCode = -1;
	private int total;
	private JsonArray result;

	public ResponseArray(int hashCode, int operator) {
		super(hashCode, operator);
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public JsonArray getResult() {
		return result;
	}

	public void setResult(JsonArray result) {
		this.result = result;
	}

	public boolean isSuccess() {
		return SUCCESS_CODE == this.respCode;
	}

	public <T> List<T> toResultList(Class<T> cls) {
		if (null == result) {
			return null;
		}
		List<T> results = new ArrayList<T>();
		Gson gson = new Gson();
		int count = result.size();
		for (int i = 0; i < count; i++) {
			JsonElement ele = result.get(i);
			JsonObject json = ele.getAsJsonObject();
			results.add(gson.fromJson(json, cls));
		}
		return results;
	}

}
