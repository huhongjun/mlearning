package com.zj.learning.control.course.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import com.zj.learning.model.course.Course;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * 本地获取资源任务
 * 
 * @author XingRongJing
 * 
 */
public class FetchCourseTask extends BaseTask {

	public FetchCourseTask(ParseHelper parserHelper,
			ICallback<RespOut> callback, RequestTag tag) {
		super(parserHelper, callback, tag);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected RespOut doInBackground(String... params) {
		// TODO Auto-generated method stub
		String path = params[0];
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			List<Course> resources = this.getParseHelper().getCourses(fis);
			return this.buildSuccessRespOut(resources);
		} catch (FileNotFoundException e) {
			return this.buildFailRespOut(BaseTask.ERROR_FILE_NOT_FIND);
		} catch (XmlPullParserException e) {
			return this.buildFailRespOut(BaseTask.ERROR_XML_PARSER);
		} catch (IOException e) {
			return this.buildFailRespOut(BaseTask.ERROR_IO_EXCEPTION);
		}
	}

}
