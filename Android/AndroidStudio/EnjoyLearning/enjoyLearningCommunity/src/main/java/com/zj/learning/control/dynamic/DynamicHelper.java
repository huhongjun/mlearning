package com.zj.learning.control.dynamic;

import android.content.res.Resources;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.zj.learning.R;
import com.zj.learning.model.dynamic.Dynamic;

/**
 * 动态助手
 * 
 * @author XingRongJing
 * 
 */
public class DynamicHelper {

	private static DynamicHelper mHelper;

	private DynamicHelper() {
	}

	public static DynamicHelper getDynamicHelper() {
		if (null == mHelper) {
			mHelper = new DynamicHelper();
		}
		return mHelper;
	}

	public int getIconByType(int type) {
		int resId = -1;
		switch (type) {
		case Dynamic.TYPE_ASKS:
			resId = R.drawable.dynamic_icon_asks;
			break;
		case Dynamic.TYPE_USER:
			resId = R.drawable.dynamic_icon_user;
			break;
		case Dynamic.TYPE_CERTIFICATE:
			resId = R.drawable.dynamic_icon_user;
			break;
		case Dynamic.TYPE_COURSE:
			resId = R.drawable.dynamic_icon_course;
			break;
		case Dynamic.TYPE_FORUM:
			resId = R.drawable.dynamic_icon_fourm;
			break;
		case Dynamic.TYPE_SYSTEM:
			resId = R.drawable.dynamic_icon_user;
			break;
		}
		return resId;
	}

	public SpannableStringBuilder getContentByEventType(Resources res,
			String username, int type, int eventType, String eventTitle) {
		StringBuilder sb = new StringBuilder();
		sb.append(username);
		sb.append(" ");
		sb.append(" ");
		int start = 0;
		if (!TextUtils.isEmpty(username)) {
			start = username.length();
		}
		String temp = "";
		switch (type) {
		case Dynamic.TYPE_ASKS:
			temp = this.getAsksTipsByType(res, eventType);
			break;
		case Dynamic.TYPE_USER:
			temp = this.getUserTipsByType(res, eventType);
			break;
		case Dynamic.TYPE_CERTIFICATE:
			temp = this.getCetificatesTipsByType(res, eventType);
			break;
		case Dynamic.TYPE_COURSE:
			temp = this.getCourseTipsByType(res, eventType);
			break;
		case Dynamic.TYPE_FORUM:
			temp = this.getForumTipsByType(res, eventType);
			break;
		case Dynamic.TYPE_SYSTEM:
			temp = this.getSystemTipsByType(res, eventType);
			break;
		}
		int end = sb.length() + temp.length();
		sb.append(temp);
		sb.append(" ");
		sb.append(" ");
		sb.append(eventTitle);
		SpannableStringBuilder ssb = this.getTextSpan(
				res.getColor(R.color.divider_color), sb.toString(), start, end);
		return ssb;
	}

	/**
	 * 根据EventType获取用户类提示语
	 * 
	 * @param res
	 * @param eventType
	 * @return
	 */
	public String getUserTipsByType(Resources res, int eventType) {
		String tips = null;
		switch (eventType) {
		case Dynamic.EVENT_TYPE_USER_ADD:
			tips = res.getString(R.string.dynamic_tips_user_add);
			break;
		case Dynamic.EVENT_TYPE_USER_UPDATE:
			tips = res.getString(R.string.dynamic_tips_user_update);
			break;
		}
		return tips;
	}

	/**
	 * 根据EventType获取课程类提示语
	 * 
	 * @param res
	 * @param eventType
	 * @return
	 */
	public String getCourseTipsByType(Resources res, int eventType) {
		String tips = null;
		switch (eventType) {
		case Dynamic.EVENT_TYPE_COURSE_FOCUS:
			tips = res.getString(R.string.dynamic_tips_course_focus);
			break;
		case Dynamic.EVENT_TYPE_COURSE_LEARN:
			tips = res.getString(R.string.dynamic_tips_course_learn);
			break;
		case Dynamic.EVENT_TYPE_COURSE_LEARN_COMPLETE:
			tips = res.getString(R.string.dynamic_tips_course_learn_complete);
			break;
		case Dynamic.EVENT_TYPE_COURSE_REVIEW:
			tips = res.getString(R.string.dynamic_tips_course_review);
			break;
		}
		return tips;
	}

	/**
	 * 根据EventType获取论坛类提示语
	 * 
	 * @param res
	 * @param eventType
	 * @return
	 */
	public String getForumTipsByType(Resources res, int eventType) {
		String tips = null;
		switch (eventType) {
		case Dynamic.EVENT_TYPE_FORUM_ADD:
			tips = res.getString(R.string.dynamic_tips_forum_add);
			break;
		case Dynamic.EVENT_TYPE_FORUM_REVIEW:
			tips = res.getString(R.string.dynamic_tips_forum_review);
			break;
		case Dynamic.EVENT_TYPE_FORUM_GOOD:
			tips = res.getString(R.string.dynamic_tips_forum_good);
			break;
		}
		return tips;
	}

	/**
	 * 根据EventType获取问答类提示语
	 * 
	 * @param res
	 * @param eventType
	 * @return
	 */
	public String getAsksTipsByType(Resources res, int eventType) {
		String tips = null;
		switch (eventType) {
		case Dynamic.EVENT_TYPE_ASKS_ADD:
			tips = res.getString(R.string.dynamic_tips_asks_add);
			break;
		case Dynamic.EVENT_TYPE_ASKS_ANSWER:
			tips = res.getString(R.string.dynamic_tips_asks_answer);
			break;
		case Dynamic.EVENT_TYPE_ASKS_ANSWER_REVIEW:
			tips = res.getString(R.string.dynamic_tips_asks_answer_review);
			break;
		}
		return tips;
	}

	/**
	 * 根据EventType获取证书类提示语
	 * 
	 * @param res
	 * @param eventType
	 * @return
	 */
	public String getCetificatesTipsByType(Resources res, int eventType) {
		String tips = null;
		switch (eventType) {
		case Dynamic.EVENT_TYPE_CERTIFICATE_GOT:
			tips = res.getString(R.string.dynamic_tips_certificate_got);
			break;
		}
		return tips;
	}

	/**
	 * 根据EventType获取系统类提示语
	 * 
	 * @param res
	 * @param eventType
	 * @return
	 */
	public String getSystemTipsByType(Resources res, int eventType) {
		String tips = null;
		switch (eventType) {
		case Dynamic.EVENT_TYPE_SYSTEM_NEW_COURSE:
			tips = res.getString(R.string.dynamic_tips_system_course_add);
			break;
		case Dynamic.EVENT_TYPE_SYSTEM_APP_UPDATE:
			tips = res.getString(R.string.dynamic_tips_system_app_update);
			break;
		case Dynamic.EVENT_TYPE_SYSTEM_HISTORY:
			tips = res.getString(R.string.dynamic_tips_system_history);
			break;
		}
		return tips;
	}

	private SpannableStringBuilder getTextSpan(int color, String src,
			int start, int end) {
		SpannableStringBuilder style = new SpannableStringBuilder(src);
		style.setSpan(new ForegroundColorSpan(color), start, end,
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}

}
