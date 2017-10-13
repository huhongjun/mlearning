package com.zj.learning.dao.task;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.zj.learning.dao.CoreSharedPreference;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * 本地获取记录任务
 * 
 * @author XingRongJing
 * 
 */
public class FetchRecordTask extends AsyncTask<String, Integer, RespOut> {

	private Context mCtx;
	private RequestTag mReqTag;
	private ICallback<RespOut> mCallback;

	public FetchRecordTask(Context mCtx, ICallback<RespOut> listener,
			RequestTag reqTag) {
		this.mCtx = mCtx;
		this.mCallback = listener;
		this.mReqTag = reqTag;
	}

	@Override
	protected RespOut doInBackground(String... params) {
		// TODO Auto-generated method stub
		String filename = params[0];
		List<Object> results = CoreSharedPreference
				.getAllObject(mCtx, filename);
		RespOut out = new RespOut(mReqTag);
		out.isSuccess = true;
		out.result = results;
		return out;
	}

	@Override
	protected void onPostExecute(RespOut result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (null != mCallback) {
			mCallback.onResponseCache(result);
		}
	}

}
