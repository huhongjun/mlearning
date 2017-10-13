package com.zj.support.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import android.content.Context;

import com.zj.R;

public abstract class TimeUtil {

	public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
	public static final String DATE_FORMAT_NO_YEARANDSECOND = "MM-dd HH:mm";
	public static final String DATE_FORMAT_MM_DD = "MM-dd";

	// 将日期类型的转换成String类型
	public static String formatDate(Date date) {
		return formatDate(date, DATE_FORMAT_NO_YEARANDSECOND);
	}

	// 将日期类型的转换成String类型
	public static String formatDate(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(date);
		return dateString;
	}

	// 将日期类型的转换成String类型
	public static String formatNoYearDate(Date date) {
		return TimeUtil.formatDate(date, DATE_FORMAT_NO_YEARANDSECOND);
	}

	// 将String类型的数据转换成时间格式
	public static Date getDateByStrToYMD(String str) {
		Date date = null;
		if (str != null && str.trim().length() > 0) {
			DateFormat dFormat = new SimpleDateFormat(DATE_FORMAT_FULL);
			try {
				date = dFormat.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * 将字符串时间转为long
	 * 
	 * @param time
	 * @return
	 */
	public static long parseDate(String time) {
		return TimeUtil.parseDate(time, DATE_FORMAT_FULL);
	}

	/**
	 * 将字符串时间转为long
	 * 
	 * @param time
	 * @return
	 */
	public static long parseDate(String time, String format) {
		SimpleDateFormat df = new SimpleDateFormat(format);// 设置日期格式
		try {
			Date date = df.parse(time);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	public static String getCurrentTime() {
		Calendar c = Calendar.getInstance();
		String name = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH)
				+ c.get(Calendar.DAY_OF_MONTH) + c.get(Calendar.HOUR_OF_DAY)
				+ c.get(Calendar.MINUTE) + c.get(Calendar.SECOND);
		return name;
	}

	public static String getCurrentDate() {
		return TimeUtil.formatDate(new Date(), DATE_FORMAT_FULL);
	}

	public static long getCurrentTimeBySeconds() {
		return System.currentTimeMillis() / 1000;
	}

	public static Date getUtcDate(Date date) {
		long dateZoneOffset = date.getTime();
		// 1、取得本地时间：
		java.util.Calendar cal = java.util.Calendar.getInstance();
		// 2、取得时间偏移量：
		long zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
		// 3、取得夏令时差：
		long dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
		// 4、从本地时间里扣除这些差量，即可以取得UTC时间：
		// cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
		dateZoneOffset = dateZoneOffset + zoneOffset + dstOffset;
		Date newDate = new Date(dateZoneOffset);
		return newDate;
	}

	/**
	 * 将秒转为毫秒并返回一个Date
	 * 
	 * @param seconds
	 * @return
	 */
	public static Date getDateBySeconds(long seconds) {
		return new Date(seconds * 1000);
	}

	public static boolean isToday(long seconds) {
		Date date = TimeUtil.getDateBySeconds(seconds);
		String time = TimeUtil.formatDate(date, DATE_FORMAT_YYYY_MM_DD);
		Date nowDate = new Date(System.currentTimeMillis());
		String nowTime = TimeUtil.formatDate(nowDate, DATE_FORMAT_YYYY_MM_DD);
		return time.equals(nowTime);
	}

	public static boolean isLastDay(long seconds) {
		Date date = TimeUtil.getDateBySeconds(seconds);
		String time = TimeUtil.formatDate(date, DATE_FORMAT_YYYY_MM_DD);
		Date nowDate = new Date(System.currentTimeMillis() - 24 * 60 * 60
				* 1000);
		String nowTime = TimeUtil.formatDate(nowDate, DATE_FORMAT_YYYY_MM_DD);
		return time.equals(nowTime);
	}

	/**
	 * 时间（毫秒）格式化
	 * 
	 * @param timeMs
	 * @return
	 */
	public static String formatMillTime(long timeMs) {
		StringBuilder mFormatBuilder = new StringBuilder();
		Formatter mFormatter = new Formatter(mFormatBuilder,
				Locale.getDefault());
		long totalSeconds = timeMs / 1000;

		long seconds = totalSeconds % 60;
		long minutes = (totalSeconds / 60) % 60;
		long hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds)
					.toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	/**
	 * 获取时间提示（如果是当天，则显示多少分钟或小时以前；否则显示月日即可）
	 * 
	 * @param ctx
	 * @param timeSeconds
	 * @return
	 */
	public static String getTimeTips(Context ctx, long timeSeconds) {
		return TimeUtil.getTimeTips(ctx, timeSeconds, DATE_FORMAT_MM_DD);
	}

	/**
	 * 获取时间提示（如果是当天，则显示多少分钟或小时以前；否则显示format格式的时间即可）
	 * 
	 * @param ctx
	 * @param timeSeconds
	 * @param format
	 * @return
	 */
	public static String getTimeTips(Context ctx, long timeSeconds,
			String format) {
		Date date = TimeUtil.getDateBySeconds(timeSeconds);
		if (TimeUtil.isLastDay(timeSeconds)) {
			return ctx.getString(R.string.str_dayago);
		}
		if (!TimeUtil.isToday(timeSeconds)) {
			return TimeUtil.formatDate(date, format);
		}
		final long getTime = date.getTime();
		final long currTime = System.currentTimeMillis();
		// 计算服务器返回时间与当前时间差值
		final long seconds = (currTime - getTime) / 1000;
		final long minute = seconds / 60;
		final long hours = minute / 60;
		if (hours > 0) {
			return hours + ctx.getString(R.string.str_hoursago);
		} else if (minute > 0) {
			return minute + ctx.getString(R.string.str_minsago);
		} else if (seconds > 0) {
			return "1" + ctx.getString(R.string.str_minsago);
		} else {
			return "1" + ctx.getString(R.string.str_minsago); // 都换成分钟前
		}
	}

	/**
	 * 根据秒返回分钟数（多算）
	 * 
	 * @param timeSeconds
	 * @return
	 */
	public static long getTimesBySeconds(long timeSeconds) {
		long temp = timeSeconds % 60;
		long minutes = (timeSeconds / 60);
		if (temp != 0) {
			minutes++;
		}
		return minutes;
	}

	/**
	 * 根据秒返回分钟数或小时数
	 * 
	 * @param timeSeconds
	 * @return
	 */
	public static String getTimesStrBySeconds(long timeSeconds) {
		long temp = timeSeconds % 60;
		long minutes = (timeSeconds / 60);
		if (temp != 0) {// 多算
			minutes++;
		}
		long lessMinutes = (minutes % 60);
		long hours = (minutes / 60);
		StringBuilder sb = new StringBuilder();
		if (hours > 0) {
			sb.append(hours);
			sb.append("小时");
			sb.append(lessMinutes);
			sb.append("分钟");
		} else {
			sb.append(minutes);
			sb.append("分钟");
		}
		return sb.toString();
	}

}
