package com.zj.support.http.upload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.util.Log;
import org.apache.http.protocol.HTTP;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.multipart.HttpMultipartMode;
import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.zj.support.http.MIMERequestCallback;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.inter.IUploadListener;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

public class UploadApi {

	/**
	 * 上传文件
	 * 
	 * @param path
	 * @param url
	 * @param httpParams
	 * @param listener
	 */
	@SuppressLint("NewApi")
	public void upload(String path, String url, Map<String, Object> httpParams,
			IUploadListener listener) {
		HttpUtils http = new HttpUtils();
		RequestParams params = new RequestParams(); // 默认编码UTF-8
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型
		if (null != httpParams) {
			Set<Map.Entry<String, Object>> entrys = httpParams.entrySet();
			Map.Entry<String, Object> entry = null;
			String key = "";
			Object value = "";
			Iterator<Map.Entry<String, Object>> it = entrys.iterator();
			while (it.hasNext()) {
				entry = (Map.Entry<String, Object>) it.next();
				key = entry.getKey();
				value = entry.getValue();
				params.addBodyParameter(key, value.toString());
				// params.addQueryStringParameter(key, value.toString());
			}
		}
		if (path != null && !path.isEmpty()) {
			params.addBodyParameter("file", new File(path));
		}
		http.send(HttpRequest.HttpMethod.POST, url, params,
				new UploadRequestCallBack(path, listener));
	}

	/**
	 * 上传文件加文本或单文本均可（遵循MIME格式）
	 * 
	 * @param url
	 * @param httpParams
	 * @param filePath
	 * @param mCallback
	 * @param tag
	 */
	public void uploadMIME(String url, Map<String, String> httpParams,
			String filePath, ICallback<RespOut> mCallback, RequestTag tag) {
		MIMERequestCallback callBack = new MIMERequestCallback(tag, mCallback);
		MultipartEntity multipartEntity = new MultipartEntity(
				HttpMultipartMode.STRICT, null, Charset.forName(HTTP.UTF_8));
		RequestParams params = new RequestParams(); // 默认编码UTF-8
		if (null != httpParams) {
			Set<Map.Entry<String, String>> entrys = httpParams.entrySet();
			Map.Entry<String, String> entry = null;
			String key = "";
			Object value = "";
			Iterator<Map.Entry<String, String>> it = entrys.iterator();
			while (it.hasNext()) {
				entry = (Map.Entry<String, String>) it.next();
				key = entry.getKey();
				value = entry.getValue();
				try {
					multipartEntity.addPart(key,
							new StringBody(value.toString()));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			HttpUtils utils = new HttpUtils();
			if (filePath != null) {
				File file = new File(filePath);
				if (file.exists()) {
					multipartEntity.addPart("file", new FileBody(file));
				}
			}
			params.setBodyEntity(multipartEntity);
			utils.send(HttpRequest.HttpMethod.POST, url, params, callBack);
		}
	}

}
