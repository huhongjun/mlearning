package com.zhijin.mlearning.app.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.constant.GlobalConfig;
import com.zhijin.mlearning.app.dao.http.HttpBaseDao;
import com.zhijin.mlearning.app.model.LearningProgress;
import com.zhijin.mlearning.support.network.Param;

public class StoreOperation
{

	private static StoreOperation instance;
	private static final int HTTP_TIMEOUT = 60 * 1000;
	private static final int HTTP_BUFFER_SIZE = 1024;
	private static final int HTTP_OK = 200;
	private static final String HTTP_ERROR_CONST = "httpError";

	private StoreOperation()
	{
	}

	public static StoreOperation getInstance()
	{
		if (instance == null) {
			instance = new StoreOperation();
		}
		return instance;
	}

	public void getLearingProgress(Param param)
	{
		param.setMethod(GlobalConfig.Interface.I_GETPROGRESS);
		String url = GlobalConfig.Url.U_LOCALGETPROGRESS + CoreApp.getApp().getMemberId();
		CoreApp.getApp().getExecutors().submit(new HttpGetRequestTask(param, url));
	}

	// public void saveLearingProgress(LearningProgress learningProgress,
	// Param param) {
	// param.setMethod(GlobalConfig.Interface.I_SAVEPROGRESS);
	// String content = JsonTools.beanToJson(learningProgress);
	// String url = GlobalConfig.Url.U_SAVEPROGRESS + content;
	// CoreApp.getApp().getExecutors()
	// .submit(new HttpPostRequestTask(param, url));
	// }

	public void saveLearingProgressList(List<LearningProgress> learningProgress, Param param)
	{
		String jsonString = listToJson(learningProgress);
		JSONObject jObj = new JSONObject();
		try {
			jObj.put("memberId", CoreApp.getApp().getMemberId());
			jObj.put("progressJson", jsonString);
			CoreApp.getApp().getExecutors()
					.submit(new HttpPostRequestTask(param, GlobalConfig.Url.U_LOCALSAVEPROGRESS, jObj.toString()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	// get请求任务
	private class HttpGetRequestTask extends HttpBaseDao implements Runnable
	{

		private Param param;
		private String url;

		public HttpGetRequestTask(Param param, String url)
		{
			this.param = param;
			this.url = url;
		}

		@Override
		public void run()
		{
			String result = doGet(url);
			this.handleRequestResult(result);
		}

		/**
		 * 网络请求结果以及回调处理
		 * 
		 * @param result
		 */
		private void handleRequestResult(String result)
		{
			if (TextUtils.isEmpty(result)) {
				param.setResult("");
				success(param);
			} else {
				if (HTTP_ERROR_CONST.equals(result)) {
					param.setResult("Http请求出错");
					return;
				}
				if (process(result)) {
					success(param);
				} else {
					param.setResult("请求处理结果出错：" + result);
					fails(param);
				}
			}
		}

		@Override
		public boolean process(String e)
		{
			try {
				JSONObject object = new JSONObject(e);
				if (object != null) {
					String respCode = object.get("respCode").toString();
					if (respCode.equals("1")) {
						String resultJson = object.getString("result").toString();
						if (resultJson != null) {
							JSONObject jsonObject = new JSONObject(resultJson);
							String progressListJson = jsonObject.getString("progressJson");
							if (progressListJson != null && !progressListJson.equals("")) {
								JSONArray jsonArray = new JSONArray(progressListJson);
								if (jsonArray != null && jsonArray.length() > 0) {
									List<LearningProgress> learningProgressList = new ArrayList<LearningProgress>();
									for (int i = 0; i < jsonArray.length(); i++) {
										JSONObject obj = jsonArray.getJSONObject(i);
										if (obj != null) {
											int id = obj.getInt("id");
											String memberId = obj.getString("memberId");
											String sectionName = obj.getString("sectionName");
											int sectionId = obj.getInt("sectionId");

											long sectionProgress = obj.getLong("sectionProgress");
											LearningProgress learningProgress = new LearningProgress();
											learningProgress.setMemberId(memberId);
											learningProgress.setId(id);
											learningProgress.setSectionId(sectionId);
											learningProgress.setSectionName(sectionName);
											learningProgress.setSectionProgress(sectionProgress);
											learningProgressList.add(learningProgress);

										}
									}
									param.setResult(learningProgressList);
									return true;
								}
							}
						}
					} else {
						param.setResult(null);
						return false;
					}
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
				return false;
			}
			// ProgressJson obj = (ProgressJson) JsonUtil.getObjectJsonString(e,
			// ProgressJson.class);
			// if (obj != null) {
			// if (obj.getRespCode().equals("1")) {
			// CourseProgress courseProgress = obj.getCourseProgressJson();
			// param.setResult(courseProgress.getProgressJson());
			// return true;
			// } else {
			// return false;
			// }
			// }
			return false;
		}
	}

	// post请求任务
	private class HttpPostRequestTask extends HttpBaseDao implements Runnable
	{

		private Param param;
		private String url;
		private String paramJson;

		public HttpPostRequestTask(Param param, String url, String paramJson)
		{
			this.param = param;
			this.url = url;
			this.paramJson = paramJson;
		}

		@Override
		public void run()
		{
			String result = doPost(url, paramJson);
			this.handleRequestResult(result);
		}

		/**
		 * 网络请求结果以及回调处理
		 * 
		 * @param result
		 */
		private void handleRequestResult(String result)
		{
			if (TextUtils.isEmpty(result)) {
				param.setResult("");
				success(param);
			} else {
				if (HTTP_ERROR_CONST.equals(result)) {
					param.setResult("Http请求出错");
					return;
				}
				if (process(result)) {
					success(param);
				} else {
					param.setResult("请求处理结果出错：" + result);
					fails(param);
				}
			}
		}

		@Override
		public boolean process(String e)
		{
			try {
				JSONObject object = new JSONObject(e);
				if (object != null) {
					String respCode = object.get("respCode").toString();
					if (respCode != null && respCode.equals("1")) {
						param.setResult("success");
						return true;
					} else {
						String errorMsg = object.get("errorMsg").toString();
						param.setResult(errorMsg);
						return false;
					}
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
				return false;
			}
			return super.process(e);
		}
	}

	private String doGet(String url)
	{
		try {
			BufferedReader in;
			// 定义HttpClient
			HttpClient client = new DefaultHttpClient();
			// 实例化HTTP方法
			HttpGet request = new HttpGet();
			request.setURI(new URI(url));
			HttpResponse response = client.execute(request);
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			String content = sb.toString();
			return content;
		} catch (URISyntaxException e) {
			System.out.println("URISyntaxException:" + e.toString());
			return null;
		} catch (ClientProtocolException e) {
			System.out.println("ClientProtocolException:" + e.toString());
			return null;
		} catch (IOException e) {
			System.out.println("IOException:" + e.toString());
			return null;
		}
	}

	private String doPost(String url, String paramJson)
	{
		HttpClient client = null;
		try {
			HttpPost post = new HttpPost(url);
			post.setHeader("Accept-Language", "zh-cn,zh;q=0.5");
			post.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");
			if (paramJson != null && !paramJson.equals("")) {
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("jsonParam", paramJson));
				HttpEntity entity = new UrlEncodedFormEntity(params, "UTF-8");
				post.setEntity(entity);
			}
			// 请求参数的设置
			BasicHttpParams httpParams = new BasicHttpParams();
			httpParams.setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
			HttpConnectionParams.setConnectionTimeout(httpParams, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, HTTP_TIMEOUT);
			HttpConnectionParams.setSocketBufferSize(httpParams, HTTP_BUFFER_SIZE);
			client = new DefaultHttpClient(httpParams);
			HttpResponse response = client.execute(post);
			if (null != response) {
				int responseCode = response.getStatusLine().getStatusCode();// 获得响应码
				if (HTTP_OK == responseCode) {
					return EntityUtils.toString(response.getEntity());
				} else {
					return HTTP_ERROR_CONST;
				}
			} else {
				throw new IOException("HttpResponse为空");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return HTTP_ERROR_CONST;
		} finally {
			if (null != client) {
				client.getConnectionManager().shutdown();
			}
		}
	}

	private String listToJson(List<LearningProgress> list)
	{
		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			LearningProgress lp = list.get(i);
			JSONObject obj = new JSONObject();
			try {
				obj.put("id", lp.getId());
				obj.put("memberId", lp.getMemberId());
				obj.put("sectionName", lp.getSectionName());
				obj.put("sectionId", lp.getSectionId());
				obj.put("sectionProgress", lp.getSectionProgress());
				jsonArray.put(obj);
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}
		return jsonArray.toString();
	}

}
