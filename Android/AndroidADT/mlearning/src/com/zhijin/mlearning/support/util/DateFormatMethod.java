package com.zhijin.mlearning.support.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormatMethod
{

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final String DATE_FORMAT_2 = "yyyy-MM-dd HH:mm";

	// 将日期类型的转换成String类型
	public static String formatDate(Date date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
		if (date != null) {
			String dateString = formatter.format(date);
			return dateString;
		} else {
			return "";
		}

	}

	public static Date getUtcDate(Date date)
	{
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

	// 将日期类型的转换成String类型
	public static String formatNoYearDate(Date date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_2);
		String dateString = formatter.format(date);
		return dateString;
	}

	// 将String类型的数据转换成时间格式
	public static Date getDateByStrToYMD(String str)
	{
		Date date = null;
		if (str != null && str.trim().length() > 0) {
			DateFormat dFormat = new SimpleDateFormat(DATE_FORMAT);
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
	public static long parseDate(String time)
	{
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);// 设置日期格式
		try {
			Date date = df.parse(time);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0l;
	}

	public static String getCurrentTime()
	{
		Calendar c = Calendar.getInstance();
		String name = "" + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH)
				+ c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) + c.get(Calendar.SECOND);
		return name;
	}

	public static String getCurrentDate()
	{
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);// 设置日期格式
		String currentDate = df.format(new Date(System.currentTimeMillis()));// new
																				// Date()为获取当前系统时间
		return currentDate;
	}

	public static long getStartTime(Date startDate)
	{
		String dateTime = formatDate(startDate);
		int index = dateTime.indexOf(" ");
		String hms = dateTime.substring(index);
		String currentDate = getCurrentDate();
		int indexTwo = currentDate.indexOf(" ");
		String ymd = currentDate.substring(0, indexTwo);
		String rightDate = ymd + " " + hms;
		return parseDate(rightDate);

	}

	// 将String类型的数据转换成时间格式
	public static Date getRightTime(Date startDate)
	{
		String dateTime = formatDate(startDate);
		int index = dateTime.indexOf(" ");
		String hms = dateTime.substring(index);
		String currentDate = getCurrentDate();
		int indexTwo = currentDate.indexOf(" ");
		String ymd = currentDate.substring(0, indexTwo);
		String rightDate = ymd + " " + hms;
		return getDateByStrToYMD(rightDate);
	}

	public static boolean compareDate(Date date1, Date date2)
	{
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		int n1 = cal.get(Calendar.DATE);
		cal.setTime(date2);
		int n2 = cal.get(Calendar.DATE);
		return n1 < n2;
	}

	public static long getCurrentTimeForLong()
	{
		Date date = new Date();
		return date.getTime();
	}
}
