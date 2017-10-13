package com.zj.support.cache.image;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

/**
 * 图片内存缓存--遵循Lru原则
 * 
 * @author XingRongJing
 * 
 */
public class ImageLruCache extends LruCache<String, Bitmap> implements
		ImageCache {

	public ImageLruCache(int maxSize) {
		super(maxSize);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("NewApi")
	@Override
	protected int sizeOf(String key, Bitmap bitmap) {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return bitmap.getAllocationByteCount()/1024;
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount()/1024;
		} else {
			return (bitmap.getRowBytes() * bitmap.getHeight())/1024;
		}
	}

	@Override
	public Bitmap getBitmap(String key) {
		// TODO Auto-generated method stub
		return this.get(key);
	}

	@Override
	public void putBitmap(String key, Bitmap value) {
		// TODO Auto-generated method stub
		this.put(key, value);
	}

	public void removeBitmap(String key) {
		this.remove(key);
	}

}
