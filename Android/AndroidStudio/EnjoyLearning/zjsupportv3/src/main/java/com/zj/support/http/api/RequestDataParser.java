package com.zj.support.http.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.google.gson.Gson;

/**
 * 服务器返回json数据解析助手
 * 
 * @author XingRongJing
 * 
 */
public class RequestDataParser {

	private final static String KEY_RESP_RESULT = "result";
	private final static String KEY_RESP_CODE = "respCode";
	private final static String KEY_TOTAL = "total";
	private final static String KEY_RESP_ID = "id";
	private final static String KEY_RESP_THUMB = "thumb";
	private final static String KEY_JSON_ARRAY_LEFT_TAG = "[";
	private final static int INVALID = -1;
	private final static int RESP_CODE_SUCCESS = 0;

	public static class OperatorType {
		public final static int ADD = 0;
		public final static int DELETE = ADD + 1;
		public final static int UPDATE = DELETE + 1;
		public final static int GET_ONE = UPDATE + 1;
		public final static int GET_ARRAY = GET_ONE + 1;
	}

	// /** 操作类型 **/
	// private int mOperatorType = INVALID;
	private int mRespCode = INVALID;
	private String mResult;
	private int mId = INVALID;
	private Gson mGson;
	private JSONObject mJsonResult;

	/**
	 * @param result
	 *            Json String
	 * 
	 */
	public RequestDataParser(String result) {
		if (TextUtils.isEmpty(result)) {
			throw new IllegalArgumentException("Result can not be null");
		}
		mGson = new Gson();
		try {
			mJsonResult = new JSONObject(result);
			mRespCode = mJsonResult.getInt(KEY_RESP_CODE);
			if (mJsonResult.has(KEY_RESP_RESULT)) {// 查询操作成功
				mResult = mJsonResult.getString(KEY_RESP_RESULT);
			} else if (mJsonResult.has(KEY_RESP_ID)) {// 增加、删除、修改操作成功
				mId = mJsonResult.getInt(KEY_RESP_ID);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 操作是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess() {
		return RESP_CODE_SUCCESS == mRespCode;
	}

	/**
	 * 操作失败时获取失败信息
	 * 
	 * @return
	 */
	public String getErrorMsg() {
		return mResult;
	}

	/**
	 * 查询列表成功时获取结果集
	 * 
	 * @param cls
	 * @return
	 */
	public <E> List<E> getArray(Class<E> cls) {
		return this.parseArray(mResult, cls);
	}

	/**
	 * 查询单个数据成功时获取结果
	 * 
	 * @param cls
	 * @return
	 */
	public <E> E getOne(Class<E> cls) {
		return this.parseOne(mResult, cls);
	}

	/**
	 * 增加、删除、修改成功时，获取服务器生成id
	 * 
	 * @return
	 */
	public int getId() {
		return this.mId;
	}

	/**
	 * 根据某个键获取某个json值
	 * 
	 * @param key
	 * @return
	 */
	public Object getValue(String key) {
		try {
			return mJsonResult.get(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private <E> List<E> parseArray(String jsonArray, Class<E> cls) {
		if (TextUtils.isEmpty(jsonArray)) {
			return null;
		}
		try {
			JSONArray array = new JSONArray(jsonArray);
			List<E> datas = new ArrayList<E>();
			int count = array.length();
			for (int i = 0; i < count; i++) {
				JSONObject json = array.getJSONObject(i);
				E e = mGson.fromJson(json.toString(), cls);
				datas.add(e);
			}
			return datas;
		} catch (JSONException e) {
			return null;
		}
	}

	private <E> E parseOne(String json, Class<E> cls) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		E e = mGson.fromJson(json, cls);
		return e;
	}

}
