package com.zj.support.cache.image;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.zj.support.util.BitmapUtil;

/**
 * 图片加载回调监听
 * 
 * @author XingRongJing
 * 
 */
public class ZjImageListener implements ImageLoader.ImageListener {

	private ImageView mIvShow;
	/** 默认图片资源id **/
	private int mDefaultImageId = -1;
	/** 错误图片资源id **/
	private int mErrorImageId = -1;
	/** 是否圆角，默认不是 **/
	private boolean isRoundCorner = false;

	/**
	 * 
	 * @param iv
	 * @param isRoundCorner
	 *            是否圆角
	 * @param defaultImageId
	 *            默认图片资源id
	 */
	public ZjImageListener(ImageView iv, boolean isRoundCorner,
			int defaultImageId) {
		this.mIvShow = iv;
		this.isRoundCorner = isRoundCorner;
		this.mDefaultImageId = defaultImageId;
	}

	/**
	 * 
	 * @param iv
	 * @param isRoundCorner
	 *            是否圆角
	 * @param defaultImageId
	 *            默认图片资源id
	 * @param errorImageId
	 *            错误图片资源id
	 */
	public ZjImageListener(ImageView iv, boolean isRoundCorner,
			int defaultImageId, int errorImageId) {
		this.mIvShow = iv;
		this.isRoundCorner = isRoundCorner;
		this.mDefaultImageId = defaultImageId;
		this.mErrorImageId = errorImageId;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		// TODO Auto-generated method stub
		// 6-13 Add by XingRongJing 当图片不存在时，服务器无返回，会导致ListView图片重复
		// if (null != mIvShow) {
		// mIvShow.setImageResource(mErrorImageId);
		// }
	}

	@Override
	public void onResponse(ImageContainer response, boolean isImmediate) {
		// TODO Auto-generated method stub
		Bitmap bm = response.getBitmap();
		if (null == mIvShow) {
			return;
		}
		if (null == bm) {
			mIvShow.setImageResource(mDefaultImageId);
			return;
		}
		if (isRoundCorner) {
			bm = BitmapUtil.toRoundBitmap(bm);
		}
		mIvShow.setImageBitmap(bm);
	}

}
