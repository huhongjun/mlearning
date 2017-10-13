package com.zj.support.http;

import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zj.app.AppHelper;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * MIME上传回调
 * 
 * @author XingRongJing
 * 
 */
public class MIMERequestCallback extends RequestCallBack<String> {

	private static final String TAG = "MIMERequestCallback";
	private ICallback<RespOut> mListener;
	private RespOut mOut;

	public MIMERequestCallback(RequestTag mReqParam,
			ICallback<RespOut> mCallback) {
		this.mListener = mCallback;
		mOut = new RespOut(mReqParam);
	}

	@Override
	public void onSuccess(ResponseInfo<String> responseInfo) {
		try {
			String result = responseInfo.result;
			if (AppHelper.mIsLog) {
				Log.d(TAG, "shan-->MIME:result：" + result);
			}
			if (result.startsWith("\ufeff")) {// 预防Php编码问题产生的Bom
				result = result.substring(1);
			}

			mOut.isSuccess = true;
			mOut.resp = result;
			if (null != mListener) {
				mListener.onResponse(mOut);
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.onFails(e.getMessage());
		}

	}

	@Override
	public void onFailure(HttpException arg0, String arg1) {
		this.onFails((null == arg0) ? arg1 : arg0.getMessage().toString());
	}

	private void onFails(String error) {
		if (AppHelper.mIsLog) {
			Log.d(TAG, "shan-->error：" + error);
		}
		mOut.isSuccess = false;
		mOut.resp = error;
		if (null != mListener) {
			mListener.onResponseError(mOut);
		}
	}
}
