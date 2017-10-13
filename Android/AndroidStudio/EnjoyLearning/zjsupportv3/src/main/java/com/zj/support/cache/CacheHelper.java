package com.zj.support.cache;

import java.util.List;

import android.content.Context;

import com.zj.support.dao.SqliteApi;
import com.zj.support.dao.model.CacheInfo;

/**
 * 缓存助手
 * 
 * @author XingRongJing
 * 
 */
public class CacheHelper {

	private Context mCtx;

	CacheHelper(Context ctx) {
		this.mCtx = ctx.getApplicationContext();
	}

	public boolean saveOrUpdateCacheInfo(CacheInfo info) {
		return SqliteApi.getSqliteApi(mCtx).saveOrUpdateEntity(info);
	}

	public boolean deleteCacheInfoByUrl(String url) {
		String sql = "DELETE FROM cacheinfo where url = '" + url + "'";
		return SqliteApi.getSqliteApi(mCtx).executeSql(sql);
	}

	public CacheInfo getCacheInfoByUrl(String url) {
		String sql = "from com.zj.support.dao.model.CacheInfo where url = '"
				+ url + "'";
		List<Object> caches = SqliteApi.getSqliteApi(mCtx).findEntities(sql,
				CacheInfo.class);
		if (null != caches && caches.size() > 0) {
			return (CacheInfo) caches.get(0);
		} else {
			return null;
		}
	}
}
