package com.zj.support.cache;

import android.os.AsyncTask;

import com.zj.support.dao.model.CacheInfo;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.TimeUtil;

public class SaveOrUpdateCacheTask extends AsyncTask<String, Integer, RespOut> {

	private RequestTag mReqTag;
	private CacheHelper mCache;
	private ICallback<RespOut> mCallback;

	public SaveOrUpdateCacheTask(CacheHelper cache, ICallback<RespOut> listener,
			RequestTag reqTag) {
		this.mCache = cache;
		this.mCallback = listener;
		this.mReqTag = reqTag;
	}

	@Override
	protected RespOut doInBackground(String... params) {
		// TODO Auto-generated method stub
		String url = params[0];
		String resp = params[1];
		CacheInfo info = mCache.getCacheInfoByUrl(url);
		if (null == info) {
			info = new CacheInfo(url, resp, TimeUtil.getCurrentDate());
		} else {
			info.setJsonString(resp);
			info.setUpdateTime(TimeUtil.getCurrentDate());
		}
		boolean isSuccess = mCache.saveOrUpdateCacheInfo(info);
		RespOut out = new RespOut(mReqTag);
		out.isSuccess = isSuccess;
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
