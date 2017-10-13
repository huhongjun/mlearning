package com.zj.learning.control.course.video;

import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnInfoListener;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zj.learning.R;

public class VideoOnlineActivity extends BaseVideoActivity implements
		OnInfoListener, OnBufferingUpdateListener {

	private static final String TAG = "VideoOnlineActivity";
	private int mBufferPercent = 0;
//	private boolean mIsNeedSeekTo = false;
	private TextView mTvBuffer;
	
	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		mTvBuffer = (TextView)this.findViewById(R.id.course_video_tv_progress);
	}
	
	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		if (null != mSection) {
			// mVideoUrl = mSection.getVideoUrl();
			mVideoUrl = "http://192.168.0.75:8887/SL/test1.mp4";
//			mVideoUrl = "http://192.168.0.180/vod/video.mp4";
		} 
		if (mLog) {
			Log.d(TAG, "shan-->" + mVideoUrl);
		}
		this.buildVideoRecord(mVideoUrl);
		this.setVideoName();
	}

	@Override
	protected void onPreparePlay(MediaPlayerHelper mPlayHelper) {
		// TODO Auto-generated method stub
		try {
			mPlayHelper.setOnInfoListener(this);
			mPlayHelper.setOnBufferingUpdateListener(this);
			mPlayHelper.prepareAsyc(this, mVideoUrl);
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
	
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		int pro = seekBar.getProgress();
		if (pro > mBufferPercent) {
			this.showCacheLoading();
		}
		super.onStopTrackingTouch(seekBar);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		// TODO Auto-generated method stub
		if (mLog) {
//			Log.d(TAG, "shan-->onBufferingUpdate() percent" +percent);
		}
		mBufferPercent = percent;
		if (null == mSeekBar) {
			return;
		}
		mTvBuffer.setText(percent+"%");
		mSeekBar.setSecondaryProgress(percent);
		int progress = mSeekBar.getProgress();
//		if (mIsNeedSeekTo) {
			if (percent > progress) {// 在线需要seekTo且缓冲进度大于当前进度时，其他情况在onPrepared()
				this.setViewsEnable();
				this.hideCacheLoading();
				mTvBuffer.setText("0%");
//				mIsNeedSeekTo = false;
			}
//		}
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		if (mLog) {
			Log.d(TAG, " shan-->onInfo what = " + what + " extra = "
					+ extra);
		}
		switch (what) {
		case MEDIA_INFO_BUFFERING_START://开始缓冲
			this.showCacheLoading();
			break;
		case MEDIA_INFO_BUFFERING_END://缓冲结束
			mp.start();
			this.hideCacheLoading();
			mTvBuffer.setText("0%");
			break;
		}
		return false;
	}

	/** 缓冲视频开始--与MediaPlayer中保持一致 **/
	private static final int MEDIA_INFO_BUFFERING_START = 701;
	/** 缓冲视频结束--与MediaPlayer中保持一致 **/
	private static final int MEDIA_INFO_BUFFERING_END = 702;
}
