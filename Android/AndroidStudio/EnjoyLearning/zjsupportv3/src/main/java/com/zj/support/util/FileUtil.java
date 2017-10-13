package com.zj.support.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Random;

import android.text.TextUtils;

public abstract class FileUtil {

	/***
	 * 判断文件是否存在
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 存在为true，否则为false
	 */
	public static boolean fileIsExist(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			return false;
		}

		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return true;
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
		if (0 == fileS) {
			return "0B";
		}
		DecimalFormat df = new DecimalFormat("#.0");
		String fileSizeString = "";
		if (fileS < 1024) {
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
	 * 一个文件或文件夹是否存在
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isFileExist(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 创建一个目录-已存在则直接返回
	 * 
	 * @param path
	 * @return
	 */
	public static boolean createDir(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		if (FileUtil.isFileExist(path)) {
			return true;
		}
		File file = new File(path);
		if (file.isDirectory()) {
			return file.mkdirs();
		}
		return false;
	}

	/**
	 * 删除文件夹（级联删除，即其下面所有文件均删除）
	 * 
	 * @param dir
	 * @return
	 */
	public static boolean deleteDirectory(String dir) {
		if (null == dir) {
			return false;
		}
		File file = new File(dir);

		if (file == null || !file.exists()) {
			return false;
		}
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (int i = 0; i < list.length; i++) {
				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}
		file.delete();
		return true;
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 * @return 是否成功
	 */
	public static boolean deleteFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		File file = new File(filePath);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

}
