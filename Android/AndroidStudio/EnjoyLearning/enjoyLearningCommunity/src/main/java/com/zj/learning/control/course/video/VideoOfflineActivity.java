package com.zj.learning.control.course.video;

import java.io.IOException;

import com.zj.learning.constant.GlobalConfig;
import com.zj.support.util.SdcardUtil;

import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * 本地视频播放
 * 
 * @author XingRongJing
 * 
 */
public class VideoOfflineActivity extends BaseVideoActivity {

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		if (null != mSection) {
			mVideoUrl = SdcardUtil.getSdcardPath() + GlobalConfig.DIR_VIDEO
					+ GlobalConfig.SEPERATOR + mSection.getUrl();
		}
		this.buildVideoRecord(mVideoUrl);
		this.setVideoName();
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		this.hideCacheLoading();
		this.setViewsEnable();
		super.onPrepared(mp);
	}

	@Override
	protected void onPreparePlay(MediaPlayerHelper mPlayHelper) {
		// TODO Auto-generated method stub
		try {
			mPlayHelper.prepare(mVideoUrl);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.onError(null, BaseVideoActivity.PREPARE_FAILS,
					BaseVideoActivity.PREPARE_FAILS);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.onError(null, BaseVideoActivity.PREPARE_FAILS,
					BaseVideoActivity.PREPARE_FAILS);
		}
	}

}
