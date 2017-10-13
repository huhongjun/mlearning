package com.zj.support.cache;

import android.os.AsyncTask;

import com.zj.support.dao.model.CacheInfo;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

public class FetchCacheTask extends AsyncTask<String, Integer, RespOut> {

	private RequestTag mReqTag;
	private CacheHelper mCache;
	private ICallback<RespOut> mCallback;

	public FetchCacheTask(CacheHelper cache, ICallback<RespOut> listener,
			RequestTag reqTag) {
		this.mCache = cache;
		this.mCallback = listener;
		this.mReqTag = reqTag;
	}

	@Override
	protected RespOut doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		CacheInfo info = mCache.getCacheInfoByUrl(url);
		RespOut out = new RespOut(mReqTag);
		if (null != info) {
			out.isSuccess = true;
			out.resp = info.getJsonString();
		}
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
