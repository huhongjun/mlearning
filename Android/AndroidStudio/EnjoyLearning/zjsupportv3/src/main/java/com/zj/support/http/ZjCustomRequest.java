package com.zj.support.http;

import java.io.UnsupportedEncodingException;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.zj.app.AppHelper;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * 自定义Volley请求
 * 
 * @author XingRongJing
 * 
 */
public class ZjCustomRequest extends Request<RespOut> {

	private static final int SOCKET_TIMEOUT = 15 * 1000;
	private static final String TAG = "ZjCustomRequest";
	private ICallback<RespOut> mListener;
	private RequestTag mReqParam;

	public ZjCustomRequest(int method, String url, ICallback<RespOut> listener,
			RequestTag param) {
		super(method, url, null);
		// TODO Auto-generated constructor stub
		this.mListener = listener;
		this.mReqParam = param;
		if (AppHelper.mIsLog) {
			Log.d(TAG, "shan-->sendReq：" + url);
		}
	}

	@Override
	public RetryPolicy getRetryPolicy() {
		// TODO Auto-generated method stub
		RetryPolicy retryPolicy = new DefaultRetryPolicy(SOCKET_TIMEOUT,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return retryPolicy;
	}

	@Override
	protected Response<RespOut> parseNetworkResponse(NetworkResponse response) {// ���߳���
		// TODO Auto-generated method stub
		String parsed;
		try {
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		}
		if (AppHelper.mIsLog) {
			Log.d(TAG, "shan-->resp：" + parsed);
		}
		RespOut out = new RespOut(mReqParam);
		out.isSuccess = true;
		out.resp = parsed;
		return Response.success(out,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	public void deliverError(VolleyError error) {// Main Thread
		// TODO Auto-generated method stub
		RespOut out = new RespOut(mReqParam);
		out.resp = error.getMessage();
		if (AppHelper.mIsLog) {
			NetworkResponse errorResp = error.networkResponse;
			String errorMsg = "";
			if (null != errorResp) {
				Log.d(TAG, "shan-->errorCode：" + errorResp.statusCode);
				errorMsg = error.getMessage();
			} else {
				errorMsg = this.getErrorMsg(error.errorCode);
				Log.d(TAG, "shan-->errorCode：" + error.errorCode);
			}
			Log.d(TAG, "shan-->error：" + errorMsg);
		}
		if (mListener != null) {
			mListener.onResponseError(out);
		}
	}

	@Override
	protected void deliverResponse(RespOut response) {// Main Thread
		// TODO Auto-generated method stub
		if (null != mListener) {
			mListener.onResponse(response);
		}
	}

	private String getErrorMsg(int errorCode) {
		String errorMsg = "";
		switch (errorCode) {
		case VolleyError.ErrorCode.GENERIC_ERROR:
			errorMsg = "一般错误";
			break;
		case VolleyError.ErrorCode.NETWORK_ERROR:
			errorMsg = "网络异常";
			break;
		case VolleyError.ErrorCode.SERVER_ERROR:
			errorMsg = "服务器内部错误";
			break;
		case VolleyError.ErrorCode.TIMEOUT_ERROR:
			errorMsg = "请求超时";
			break;
		case VolleyError.ErrorCode.NO_CONNECTION_ERROR:
			errorMsg = "连接失败";
			break;
		case VolleyError.ErrorCode.PARSE_ERROR:
			errorMsg = "解析异常";
			break;
		case VolleyError.ErrorCode.BAD_REQUEST_ERROR:
			errorMsg = "Bad request";
			break;
		case VolleyError.ErrorCode.AUTH_FAILURE_ERROR:
			errorMsg = "鉴权失败";
			break;
		default:
			errorMsg = "请求失败，不知原因";
			break;
		}
		return errorMsg;
	}

}
