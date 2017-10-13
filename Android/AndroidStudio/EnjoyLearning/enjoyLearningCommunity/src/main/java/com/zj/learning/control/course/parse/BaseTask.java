package com.zj.learning.control.course.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.os.AsyncTask;

import com.zj.learning.model.course.Resource;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * 本地获取资源任务
 * 
 * @author XingRongJing
 * 
 */
public abstract class BaseTask extends AsyncTask<String, Integer, RespOut> {

	private ParseHelper mParserHelper;
	private ICallback<RespOut> mCallback;
	private RequestTag mReqTag;

	public BaseTask(ParseHelper parserHelper,
			ICallback<RespOut> callback, RequestTag tag) {
		this.mParserHelper = parserHelper;
		this.mCallback = callback;
		this.mReqTag = tag;
	}

	protected RespOut buildSuccessRespOut(Object result) {
		RespOut out = new RespOut(mReqTag);
		out.isSuccess = true;
		out.result = result;
		return out;
	}
	
	protected RespOut buildFailRespOut(String error) {
		RespOut out = new RespOut(mReqTag);
		out.isSuccess = false;
		out.resp = error;
		return out;
	}

	protected ParseHelper getParseHelper() {
		return this.mParserHelper;
	}

	@Override
	protected void onPostExecute(RespOut result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (null == mCallback || null == result) {
			return;
		}
		if (result.isSuccess) {
			mCallback.onResponseCache(result);
		} else {
			mCallback.onResponseError(result);
		}
	}
	
	public static final String ERROR_FILE_NOT_FIND = "文件不存在";
	public static final String ERROR_XML_PARSER = "Xml解析错误";
	public static final String ERROR_IO_EXCEPTION = "IO异常";
}
