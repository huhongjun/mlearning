package com.zj.learning;

import java.util.ArrayList;
import java.util.List;

import com.zj.learning.control.course.parse.BaseTask;
import com.zj.learning.control.course.parse.FetchCourseTask;
import com.zj.learning.control.course.parse.FetchQuestionsTask;
import com.zj.learning.control.course.parse.FetchResourceTask;
import com.zj.learning.control.course.parse.FetchSectionsTask;
import com.zj.learning.control.course.parse.ParseHelper;
import com.zj.learning.model.course.Course;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * 资源管家-分管课程资源、考试资源、作业资源
 * 
 * @author XingRongJing
 * 
 */
public class ResourceHouseKeeper {

	private ParseHelper mParserHelper;
	/** 考试、作业资源集 **/
	// private List<Resource> mResources;
	/** 课程资源集 **/
	private List<Course> mCourses;

	ResourceHouseKeeper() {
		// mResources = new ArrayList<Resource>();
		mCourses = new ArrayList<Course>();
	}

	private ParseHelper getResParseHelper() {
		if (null == mParserHelper) {
			mParserHelper = new ParseHelper();
		}
		return mParserHelper;
	}

	// private boolean isResListEmpty() {
	// return (null == mResources ? true : mResources.isEmpty());
	// }

	private boolean isCourseListEmpty() {
		return (null == mCourses ? true : mCourses.isEmpty());
	}

	// public List<Resource> getResourcesByType(int type) {
	// List<Resource> temps = new ArrayList<Resource>();
	// if (this.isResListEmpty()) {
	// return temps;
	// }
	// for (Resource res : mResources) {
	// if (type != res.getResType()) {
	// continue;
	// }
	// temps.add((CourseResource) res);
	// }
	// return temps;
	//
	// }

	/**
	 * 获取试题资源--作业、考试
	 * 
	 * @param path
	 * @param callback
	 * @param tag
	 * @param type
	 * @param ids
	 */
	public void fetchResource(String path, ICallback<RespOut> callback,
			RequestTag tag, int type, List<Integer> ids) {
		BaseTask task = new FetchResourceTask(this.getResParseHelper(),
				callback, tag, type, ids);
		task.execute(path);
	}

	public void fetchCourse(String path, ICallback<RespOut> callback,
			RequestTag tag) {
		BaseTask task = new FetchCourseTask(this.getResParseHelper(), callback,
				tag);
		task.execute(path);
	}

	public void fetchChapters(String path, ICallback<RespOut> callback,
			RequestTag tag) {
		BaseTask task = new FetchSectionsTask(this.getResParseHelper(),
				callback, tag);
		task.execute(path);
	}

	public void fetchQuestions(String path, ICallback<RespOut> callback,
			RequestTag tag) {
		BaseTask task = new FetchQuestionsTask(this.getResParseHelper(),
				callback, tag);
		task.execute(path);
	}

	/**
	 * 课程资源入栈
	 * 
	 * @param courses
	 */
	public void pushCourses(List<Object> courses) {
		if (null == mCourses || null == courses) {
			return;
		}
		for (Object obj : courses) {
			if (obj instanceof Course) {
				Course course = (Course) obj;
				course.setOnline(false);
				mCourses.add(course);
			}
		}
	}

	/**
	 * 分页获取课程资源-from memory
	 * 
	 * @param start
	 * @param offset
	 * @return
	 */
	public List<Course> getCourses(int start, int offset) {
		List<Course> temps = new ArrayList<Course>();
		if (this.isCourseListEmpty()) {
			return temps;
		}
		int size = mCourses.size();
		int less = start + offset;
		for (int i = start; i < less; i++) {
			if (less >= size) {
				break;
			}
			temps.add(mCourses.get(i));
		}
		return temps;
	}

	// public Resource getResourceById(int resId) {
	// if (this.isResListEmpty()) {
	// return null;
	// }
	// for (Resource res : mResources) {
	// int id = res.getId();
	// if (resId == id) {
	// return res;
	// }
	// }
	// return null;
	// }

	// public CourseResource getCourseResourceById(int resId) {
	// if (this.isCourseResListEmpty()) {
	// return null;
	// }
	// for (CourseResource res : mCourseResources) {
	// int id = res.getId();
	// if (resId == id) {
	// return res;
	// }
	// }
	// return null;
	// }

	// public List<CourseSection> parseCourseSection(String resp) {
	// List<CourseSection> sections = new ArrayList<CourseSection>();
	//
	// return sections;
	// }
}
