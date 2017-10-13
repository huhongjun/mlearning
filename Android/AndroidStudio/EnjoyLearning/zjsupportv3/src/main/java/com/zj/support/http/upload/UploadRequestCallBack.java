package com.zj.support.http.upload;

import android.util.Log;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zj.app.AppHelper;
import com.zj.support.http.inter.IUploadListener;

public class UploadRequestCallBack extends RequestCallBack<String> {

	private static final String TAG = "UploadRequestCallBack";
	private IUploadListener mListener;
	private String mPath;

	public UploadRequestCallBack(String path, IUploadListener listener) {
		this.mListener = listener;
		this.mPath = path;
	}

	@Override
	public void onLoading(long total, long current, boolean isUploading) {
		// TODO Auto-generated method stub
		super.onLoading(total, current, isUploading);
		if (isUploading) {
			if (null != mListener) {
				mListener.onUploadTransfered(mPath, total, current);
			}
		}
	}

	@Override
	public void onFailure(HttpException arg0, String arg1) {
		// TODO Auto-generated method stub
		if (AppHelper.mIsLog) {
			Log.d(TAG, "shan-->onUploadFails(): " + mPath + " error = " + arg1);
		}
		if (null != mListener) {
			mListener.onUploadFails(mPath, arg1);
		}
	}

	@Override
	public void onSuccess(ResponseInfo<String> arg0) {
		// TODO Auto-generated method stub
		if (AppHelper.mIsLog) {
			Log.i(TAG, "shan-->onUploadSuccess(): " + mPath);
		}
		if (null != mListener) {
			mListener.onUploadSuccess(mPath);
		}
	}

}