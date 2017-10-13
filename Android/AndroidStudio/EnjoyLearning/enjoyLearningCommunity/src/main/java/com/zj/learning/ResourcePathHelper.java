package com.zj.learning;

import com.zj.learning.constant.GlobalConfig;
import com.zj.support.util.FileUtil;
import com.zj.support.util.SdcardUtil;

public class ResourcePathHelper {

	/**
	 * 是否存在
	 * 
	 * @param path
	 *            绝对路径
	 * @return
	 */
	public static boolean isFileExist(String path) {
		return FileUtil.fileIsExist(path);
	}

	private static String getSdcardPath() {
		return SdcardUtil.getSdcardPath();
	}

	public static String getResourcesPath() {
		StringBuilder sb = new StringBuilder();
		sb.append(ResourcePathHelper.getSdcardPath());
		sb.append(GlobalConfig.PATH_RESOURES);
		return sb.toString();
	}

	public static String getCoursePath(String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ResourcePathHelper.getSdcardPath());
		sb.append(GlobalConfig.DIR_COURSE);
		sb.append(GlobalConfig.SEPERATOR);
		sb.append(fileName);
		return sb.toString();
	}

	public static String getExamsPath(String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ResourcePathHelper.getSdcardPath());
		sb.append(GlobalConfig.DIR_EXAM);
		sb.append(GlobalConfig.SEPERATOR);
		sb.append(fileName);
		return sb.toString();
	}

	public static String getHomeworksPath(String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ResourcePathHelper.getSdcardPath());
		sb.append(GlobalConfig.DIR_HOMEWORK);
		sb.append(GlobalConfig.SEPERATOR);
		sb.append(fileName);
		return sb.toString();
	}

	public static String getImagePath(String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ResourcePathHelper.getSdcardPath());
		sb.append(GlobalConfig.DIR_IMG);
		sb.append(GlobalConfig.SEPERATOR);
		sb.append(fileName);
		return sb.toString();
	}

	public static String getVideosPath(String fileName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ResourcePathHelper.getSdcardPath());
		sb.append(GlobalConfig.DIR_VIDEO);
		sb.append(GlobalConfig.SEPERATOR);
		sb.append(fileName);
		return sb.toString();
	}
}
