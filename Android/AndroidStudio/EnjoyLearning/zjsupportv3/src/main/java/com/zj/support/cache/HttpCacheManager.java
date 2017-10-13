package com.zj.support.cache;

import android.content.Context;
import android.text.TextUtils;

import com.zj.support.dao.model.CacheInfo;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * Http缓存管理
 * 
 * @author XingRongJing
 * 
 */
public class HttpCacheManager {

	private static final String TAG = HttpCacheManager.class.getName();
	private static HttpCacheManager mCacheMgr;
	private CacheHelper mCache;

	private HttpCacheManager(Context ctx) {
		mCache = new CacheHelper(ctx);
	}

	public static HttpCacheManager getHttpCacheManager(Context ctx) {
		if (null == mCacheMgr) {
			mCacheMgr = new HttpCacheManager(ctx);
		}
		return mCacheMgr;
	}

	public void getCacheByUrl(ICallback<RespOut> listener, RequestTag tag,
			String url) {
		FetchCacheTask task = new FetchCacheTask(mCache, listener, tag);
		task.execute(url);
	}

	public void saveOrUpdateCache(ICallback<RespOut> listener, RequestTag tag,
			String url, String resp) {
		SaveOrUpdateCacheTask task = new SaveOrUpdateCacheTask(mCache,
				listener, tag);
		task.execute(url, resp);
	}

	// public CacheInfo getCacheInfoByUrl(String url) {
	// if (TextUtils.isEmpty(url) || null == mCache) {
	// return null;
	// }
	// return mCache.getCacheInfoByUrl(url);
	// }
	//
	// public boolean saveOrUpdateCacheInfo(CacheInfo info) {
	// if (null == info || null == mCache) {
	// return false;
	// }
	// return mCache.saveOrUpdateCacheInfo(info);
	// }
	//
	// public void deleteCacheInfoByUrl(String url) {
	// if (TextUtils.isEmpty(url) || null == mCache) {
	// return;
	// }
	// mCache.deleteCacheInfoByUrl(url);
	// }

}
