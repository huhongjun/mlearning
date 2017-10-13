package com.zj.support.http.api;

import java.util.Map;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.zj.support.http.ZjCustomRequest;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

public class HttpApi {

	private RequestQueue mReqQueue;

	public HttpApi(RequestQueue reqQueue) {
		this.mReqQueue = reqQueue;
	}

	public void request(String url, Map<String, String> params,
			ICallback<RespOut> listener, RequestTag tag) {
		this.request(Method.POST, url, params, listener, tag);
	}

	public void request(int method, String url, Map<String, String> params,
			ICallback<RespOut> listener, RequestTag tag) {
		if (null == mReqQueue) {
			throw new IllegalStateException("RequestQueue cannot be null");
		}
		if(mReqQueue.isAdd(url)){
			return;
		}
		ZjCustomRequest req = new ZjCustomRequest(method, url, listener, tag);
		req.setParams(params);
		req.setTag(tag.tag);
		mReqQueue.add(req);
	}

	public void cancelAll(Object tag) {
		if (null != mReqQueue) {
			mReqQueue.cancelAll(tag);
		}
	}
}
