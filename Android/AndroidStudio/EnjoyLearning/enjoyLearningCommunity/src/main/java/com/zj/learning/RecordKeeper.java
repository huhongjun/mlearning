package com.zj.learning;

import android.content.Context;

import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.dao.CoreSharedPreference;
import com.zj.learning.dao.task.FetchRecordTask;
import com.zj.learning.model.course.VideoRecord;
import com.zj.support.http.inter.ICallback;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

/**
 * 记录管家（播放历史记录、学习记录）
 * 
 * @author XingRongJing
 * 
 */
public class RecordKeeper {

	RecordKeeper() {
	}

	public void addVideoRecord(Context ctx, VideoRecord record) {
		// Step 1：保存到本地
		CoreSharedPreference.saveObject(ctx,
				GlobalConfig.SharedPreferenceDao.FILENAME_VIDEO_RECORD,
				record.getVideoUrl(), record);
		// if(((CoreApp) ctx.getApplicationContext()).isNetworkEnable()){
		// // Step 2：保存到服务器
		// Map<String, String> params = new HashMap<String, String>();
		// String content = record.toJson();
		// params.put("content", content);
		// ((CoreApp) ctx.getApplicationContext()).getHttpApi().request(
		// GlobalConfig.URL_COURSE_VIDEO_RECORD_ADD, params, this,
		// new RequestTag(this.hashCode()));
		// }
	}

	public void fetchVideoRecord(Context ctx, ICallback<RespOut> listener,
			RequestTag reqTag) {
		FetchRecordTask task = new FetchRecordTask(ctx, listener, reqTag);
		task.execute(GlobalConfig.SharedPreferenceDao.FILENAME_VIDEO_RECORD);
	}

}
