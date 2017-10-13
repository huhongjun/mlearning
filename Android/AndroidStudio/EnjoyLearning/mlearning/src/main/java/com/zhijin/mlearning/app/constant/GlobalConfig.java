package com.zhijin.mlearning.app.constant;

public class GlobalConfig
{

	public static final int MAX = 8;
	public static final int OFFSET = 0;
	public static final String XML_URL = "/course/course_build.xml";
	public static final String KEY_USER = "user_email";
	public static final String KEY_COURSE = "course";
	public static final String KEY_COURSEPATH = "coursePath";

	public static class Interface
	{
		public static final int I_SAVEPROGRESS = 0;
		public static final int I_GETPROGRESS = I_SAVEPROGRESS + 1;
	}

	public static class Url
	{
		// public static final String U_LOCALGETPROGRESS =
		// "http://192.168.0.30:8080/jeresydemonew/rest/courseRes/getCourseProgress/";
		// public static final String U_LOCALSAVEPROGRESS =
		// "http://192.168.0.30:8080/jeresydemonew/rest/courseRes/saveCourseProgress/";

		public static final String U_LOCALGETPROGRESS = "http://192.168.0.171:8080/mlearning/rest/courseRes/getCourseProgress/";
		public static final String U_LOCALSAVEPROGRESS = "http://192.168.0.171:8080/mlearning/rest/courseRes/saveCourseProgress/";

		// public static final String U_GETPROGRESS =
		// "http://116.213.113.196/WebRoot/rest/courseRes/getCourseProgress/";
		// public static final String U_SAVEPROGRESS =
		// "http://116.213.113.196/WebRoot/rest/courseRes/saveCourseProgress/";
	}

	public static class Method
	{
		public static final int M_SAVEPROGRESS = 0;
		public static final int M_SAVEPROGRESSLIST = M_SAVEPROGRESS + 1;
		public static final int M_SAVEUPLOADPROGRESSLIST = M_SAVEPROGRESSLIST + 1;
	}
}
