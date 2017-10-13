package com.zj.support.util;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

public abstract class CommonUtil {

	public static int getScreenWidth(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getWidth();
	}

	public static int getScreenHeight(Context context) {
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		return display.getHeight();
	}

	/**
	 * 获取一个文件名（当前时间+3位随机数）
	 * 
	 * @return
	 */
	public static String getFileNameByTimestamp() {
		StringBuilder sb = new StringBuilder();
		String time = TimeUtil.formatDate(new Date(System.currentTimeMillis()),
				"yyyyMMddHHmmss");
		sb.append(time);
		sb.append(CommonUtil.getRandom(3));
		return sb.toString();
	}

	/**
	 * 获取长度为len的随机数
	 * 
	 * @param len
	 * @return
	 */
	public static String getRandom(int len) {
		Random random = new Random();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len; i++) {
			int temp = random.nextInt(10);
			sb.append(temp);
		}
		return sb.toString();
	}

	/**
	 * 格式化文件大小
	 * 
	 * @param fileS
	 * @return
	 */
	public static String formetFileSize(long fileS) {
		DecimalFormat df = new DecimalFormat("#.0");
		String fileSizeString = "";
		if (fileS == 0) {
			fileSizeString = "0.0B";
		} else if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 分割路径（远程或本地），将一个完整路径以文件名为基准分割为两部分
	 * 
	 * @param path
	 *            相对路径或完整路径
	 * @return String[0]：切割完成之后的文件路径，不包含文件名 String[1]：切割完成之后的文件名称
	 */
	public static String[] dividePath(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		String[] temp = new String[2];
		int lastIndex = path.lastIndexOf("/");
		if (-1 == lastIndex) {
			temp[0] = path;
			temp[1] = path;
			return temp;
		}
		temp[0] = path.substring(0, lastIndex + 1);
		temp[1] = path.substring(lastIndex + 1, path.length());
		return temp;
	}

	public static String transferHtmlToJava(String temp) {
		if (TextUtils.isEmpty(temp)) {
			return "";
		}
		temp = temp.replaceAll("&amp;", "&");
		temp = temp.replaceAll("&#039;", "'");
		temp = temp.replaceAll("&quot;", "\"");
		temp = temp.replaceAll("&ldquo;", "”");
		temp = temp.replaceAll("&rdquo;", "“");
		temp = temp.replaceAll("&lt;", "<");
		temp = temp.replaceAll("&gt;", ">");
		temp = temp.replaceAll("&quot;", ";");
		temp = temp.replaceAll("&reg;", "®");
		temp = temp.replaceAll("&copy", "©");
		temp = temp.replaceAll("&trade;", "™");
		temp = temp.replaceAll("&bull;", ".");
		temp = temp.replaceAll("&nbsp;", " ");
		temp = temp.replaceAll("&laquo;", "<<");
		temp = temp.replaceAll("&raquo;", ">>");
		temp = temp.replaceAll("&#8206;", "");
		temp = temp.replaceAll("&hellip;", "…");
		temp = temp.replaceAll("<br/>", "\n");
		temp = temp.replaceAll("<br>", "\n");
		temp = temp.replaceAll("<br />", "\n");
		return temp;
	}

	/**
	 * 解决Android2.2版本之前的httpconnection连接的bug
	 * 
	 */
	public static void disableConnectionReuseIfNecessary() {
		if (hasHttpConnectionBug()) {
			System.setProperty("http.keepAlive", "false");
		}
	}

	public static boolean hasHttpConnectionBug() {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO;
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dp2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * Float转string（保留小数点后几位）
	 * 
	 * @param value
	 * @param less
	 *            小数点后保留位数（仅支持1、2、3位，默认1位）
	 * @return
	 */
	public static String floatToString(float value, int less) {
		DecimalFormat decimalFormat;
		switch (less) {
		case 1:
			decimalFormat = new DecimalFormat("0.0");
			break;
		case 2:
			decimalFormat = new DecimalFormat("0.00");
			break;
		case 3:
			decimalFormat = new DecimalFormat("0.000");
			break;
		default:
			decimalFormat = new DecimalFormat("0.0");
			break;
		}
		return decimalFormat.format(value);
	}

	public static boolean isLocalUrl(String url) {
		if (TextUtils.isEmpty(url)) {
			return false;
		}
		if (url.startsWith("http") || url.startsWith("https")
				|| url.startsWith("rtsp")) {
			return false;
		}
		return true;
	}

	/**
	 * 两个String集合是否相同，忽略顺序和大小写
	 * 
	 * @param rightAnswers
	 * @param userAnswers
	 * @return
	 */
	public static boolean isEqualsIgnoreCaseAndOrder(List<String> rightAnswers,
			List<String> userAnswers) {
		if (null == rightAnswers || rightAnswers.isEmpty()
				|| null == userAnswers || userAnswers.isEmpty()) {
			return false;
		}
		if (rightAnswers.equals(userAnswers)) {// 顺序和值完全一致时
			return true;
		}
		for (String temp : rightAnswers) {
			boolean isContains = isContainsIgnoreCase(userAnswers, temp);
			if (!isContains) {
				return false;
			}
		}
		for (String temp : userAnswers) {
			boolean isContains = isContainsIgnoreCase(rightAnswers, temp);
			if (!isContains) {
				return false;
			}
		}
		return true;
	}

	public static boolean isContainsIgnoreCase(List<String> rightAnswers,
			String value) {
		if (null == rightAnswers || rightAnswers.isEmpty()) {
			return false;
		}
		if (rightAnswers.contains(value)) {
			return true;
		}
		for (String temp : rightAnswers) {
			if (value.equalsIgnoreCase(temp)) {
				return true;
			}
		}
		return false;
	}
}
