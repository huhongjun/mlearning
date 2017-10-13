package com.zj.support.http.inter;

import com.android.volley.Response.Listener;

/**
 * 每个请求(本地/远程)操作的回调接口
 * 
 * @author XingRongJing
 * 
 */
public interface ICallback<T> extends Listener<T> {

	public void onResponseError(T out);
	
	public void onResponseCache(T out);
}
