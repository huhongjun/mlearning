package com.zj.support.cache.image;

import java.io.File;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.zj.support.http.ZjVolley;

/**
 * 图片缓存助手--内存(1/8)+本地缓存(10M)
 * 
 * @author XingRongJing
 * 
 */
public class ImageCacheHelper {

	private ImageLoader mImageLoader;
	private ImageLruCache mImageCache;

	public ImageCacheHelper(RequestQueue imageReqQueue) {
		int maxMemory = (int) (Runtime.getRuntime().maxMemory());
		int maxSize = maxMemory / 8;
		mImageCache = new ImageLruCache(maxSize);
		mImageLoader = new ImageLoader(imageReqQueue, mImageCache);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}

	public void requestBitmap(String url, ImageListener listener) {
		try {
			this.mImageLoader.get(url, listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteBitmapFromLocal(Context ctx, String url) {
		try {
			mImageCache.removeBitmap(this.getCacheKey(url, 0, 0));
			String dir = ctx.getCacheDir() + "/"
					+ ZjVolley.DEFAULT_CACHE_DIR_IMAGE;
			String path = dir + "/" + this.getFilenameForKey(url);
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean deleteBitmap(Context ctx, String url) {
		try {
			mImageCache.removeBitmap(this.getCacheKey(url, 0, 0));
			String dir = ctx.getCacheDir() + "/"
					+ ZjVolley.DEFAULT_CACHE_DIR_IMAGE;
			String path = dir + "/" + this.getFilenameForKey(url);
			File file = new File(path);
			if (file.exists()) {
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getCacheKey(String url, int maxWidth, int maxHeight) {
		return new StringBuilder(url.length() + 12).append("#W")
				.append(maxWidth).append("#H").append(maxHeight).append(url)
				.toString();
	}

	private String getFilenameForKey(String key) {
		int firstHalfLength = key.length() / 2;
		String localFilename = String.valueOf(key.substring(0, firstHalfLength)
				.hashCode());
		localFilename += String.valueOf(key.substring(firstHalfLength)
				.hashCode());
		return localFilename;
	}

}
