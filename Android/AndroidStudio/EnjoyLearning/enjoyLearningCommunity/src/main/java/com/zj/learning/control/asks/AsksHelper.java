package com.zj.learning.control.asks;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.zj.learning.CoreApp;
import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.support.cache.image.ZjImageListener;
import com.zj.support.util.CommonUtil;
import com.zj.support.widget.ZjImageView;

/**
 * 问答模块助手
 * 
 * @author XingRongJing
 * 
 */
public class AsksHelper {

	private String mAnswerCountPrefix, mCommentCountPrefix;

	public AsksHelper() {
	}

	/**
	 * 处理缩略图
	 * 
	 * @param thumb
	 *            服务器返回的缩略图地址（实际是原图地址，需要自己重新拼接）
	 * @param mIvThumb
	 *            缩略图展示ImageView
	 * @param clickListener
	 *            缩略图点击事件
	 */
	public void handleThumb(String thumb, ZjImageView mIvThumb,
			OnClickListener clickListener) {
		if (TextUtils.isEmpty(thumb)) {
			mIvThumb.setVisibility(View.GONE);
		} else {
			String[] temp = CommonUtil.dividePath(thumb);
			if (null == temp || temp.length == 0) {
				mIvThumb.setVisibility(View.GONE);
			} else {
				String filePath = temp[0];
				String fileName = temp[1];
				String thumbFileName = GlobalConfig.ImageSize.SIZE170 + "_"
						+ fileName;
				String realThumb = filePath + thumbFileName;
				mIvThumb.setImageUrl(realThumb);
				mIvThumb.setVisibility(View.VISIBLE);
				mIvThumb.setTag(thumb);
				mIvThumb.setOnClickListener(clickListener);
			}
		}
	}

	public void handleAvator(ImageView iv, int userId) {
		String url = this.getAvatorUrlByUid(userId);
		((CoreApp) (iv.getContext().getApplicationContext()))
				.getImageCacheHelper()
				.requestBitmap(
						url,
						new ZjImageListener(iv, true, R.drawable.avator_default));
	}

	/**
	 * 获取头像路径
	 * 
	 * @param userId
	 * @return
	 */
	public String getAvatorUrlByUid(int userId) {
		return GlobalConfig.URL_PIC_ASKS_THUMB + GlobalConfig.SEPERATOR
				+ userId;
	}

	public SpannableStringBuilder getTextSpan(int color, String src, int start,
			int end) {
		SpannableStringBuilder style = new SpannableStringBuilder(src);
		style.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

	public SpannableStringBuilder getAnswerCountTextSpan(Context ctx,
			int answerCount) {
		if (TextUtils.isEmpty(mAnswerCountPrefix)) {
			mAnswerCountPrefix = ctx.getString(R.string.asks_reply_couts) + "(";
		}
		int start = mAnswerCountPrefix.length();
		int temp = (answerCount + "").length();
		int end = start + temp;
		SpannableStringBuilder mAnswerCountStyle = this.getTextSpan(ctx
				.getResources().getColor(R.color.bg_color), this
				.buildCountTips(mAnswerCountPrefix, answerCount), start, end);

		return mAnswerCountStyle;
	}

	public SpannableStringBuilder getCommentCountTextSpan(Context ctx,
			int commentCount) {
		if (TextUtils.isEmpty(mCommentCountPrefix)) {
			mCommentCountPrefix = ctx.getString(R.string.asks_comment) + "(";
		}
		int start = mCommentCountPrefix.length();
		int temp = (commentCount + "").length();
		int end = start + temp;
		SpannableStringBuilder mAnswerCountStyle = this.getTextSpan(ctx
				.getResources().getColor(R.color.bg_color), this
				.buildCountTips(mCommentCountPrefix, commentCount), start, end);

		return mAnswerCountStyle;
	}

	public String buildCountTips(String prefix, int count) {
		StringBuilder sb = new StringBuilder();
		sb.append(prefix);
		sb.append(count);
		sb.append(")");
		return sb.toString();
	}

	/**
	 * 点击缩略图查看原图时调用
	 * 
	 * @param v
	 */
	public void onClickThumb(View v) {
		Object obj = v.getTag();
		String tag = "";
		if (null != obj) {
			tag = obj + "";
		}
		// Intent intent = new Intent(v.getContext(), AskPicActivity.class);
		// intent.putExtra("picUrl", tag);
		// v.getContext().startActivity(intent);

	}
}
