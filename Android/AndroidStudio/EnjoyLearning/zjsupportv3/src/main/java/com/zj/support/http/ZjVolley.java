package com.zj.support.http;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import com.android.volley.AuthFailureError;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.NoCache;

public class ZjVolley {

	/** Default on-disk cache directory for image. */
	public static final String DEFAULT_CACHE_DIR_IMAGE = "imageCache";

	/** Default on-disk cache directory for http. */
	public static final String DEFAULT_CACHE_DIR_HTTP = "httpCache";

	/** Default maximum disk usage in bytes. */
	private static final int DEFAULT_DISK_USAGE_BYTES = 10 * 1024 * 1024;

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 * 
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param stack
	 *            An {@link HttpStack} to use for the network, or null for
	 *            default.
	 * @param cacheType
	 *            A type about Http request. see HttpCacheType
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context,
			HttpStack stack, int cacheType) {
		File cacheDir = null;
		if (HttpCacheType.IMAGE_CACHE_TYPE == cacheType) {
			cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR_IMAGE);
		} else {
			cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR_HTTP);
		}

		String userAgent = "volley/0";
		try {
			String packageName = context.getPackageName();

			PackageInfo info = context.getPackageManager().getPackageInfo(
					packageName, 0);
			userAgent = packageName + "/" + info.versionCode;
		} catch (NameNotFoundException e) {
		}

		if (stack == null) {
			if (Build.VERSION.SDK_INT >= 9) {
				stack = new HurlStack(new BasicUrlRewriter(), userAgent);
			} else {
				// Prior to Gingerbread, HttpUrlConnection was unreliable.
				// See:
				// http://android-developers.blogspot.com/2011/09/androids-http-clients.html
				stack = new HttpClientStack(
						AndroidHttpClient.newInstance(userAgent),
						new BasicUrlRewriter());
			}
		}

		Network network = new BasicNetwork(stack);
		RequestQueue queue = null;
		if (HttpCacheType.IMAGE_CACHE_TYPE == cacheType) {// 默认最大5M缓存,改为10M
			queue = new RequestQueue(new DiskBasedCache(cacheDir,
					DEFAULT_DISK_USAGE_BYTES), network);
		} else {
			queue = new RequestQueue(new NoCache(), network);
		}
		queue.start();

		return queue;
	}

	/**
	 * Set Logs to enabled or disabled
	 * 
	 * @param isLoggable
	 *            <code>true</code> to enable Logs, <code>false</code> to
	 *            disable logs
	 */
	public static void setLoggable(boolean isLoggable) {
		VolleyLog.sDebug = isLoggable;
	}

	/**
	 * Creates a default instance of the worker pool and calls
	 * {@link RequestQueue#start()} on it.
	 * 
	 * @param context
	 *            A {@link Context} to use for creating the cache dir.
	 * @param cacheType
	 *            A type about Http request. see HttpCacheType
	 * @return A started {@link RequestQueue} instance.
	 */
	public static RequestQueue newRequestQueue(Context context, int cacheType) {
		return newRequestQueue(context, null, cacheType);
	}

	/**
	 * Class to automatically rewrite URLs for GET methods based on whether the
	 * request has params or not
	 */
	private static class BasicUrlRewriter implements HttpStack.UrlRewriter {

		@Override
		public String rewriteUrl(Request<?> request) throws IOException {

			switch (request.getMethod()) {

			case Request.Method.GET: {

				String url = request.getUrl();

				try {
					String encodedParams = request.getEncodedUrlBody();

					if (encodedParams != null && encodedParams.length() > 0) {
						if (!url.endsWith("?")) {
							url += "?";
						}
						url += encodedParams;
					}

				} catch (AuthFailureError e) {
					return null;
				}
				return url;
			}

			case Request.Method.POST:
			case Request.Method.PUT:
			case Request.Method.DELETE: {
				return request.getUrl();
			}

			default:
				throw new IllegalStateException("Unknown request method.");

			}
		}
	}

	public interface HttpCacheType {
		public static final int HTTP_CACHE_TYPE = 0;// Http请求，不要缓存，自己实现
		public static final int IMAGE_CACHE_TYPE = HTTP_CACHE_TYPE + 1;// 图片请求，最大DEFAULT_DISK_USAGE_BYTES的缓存
	}

}
