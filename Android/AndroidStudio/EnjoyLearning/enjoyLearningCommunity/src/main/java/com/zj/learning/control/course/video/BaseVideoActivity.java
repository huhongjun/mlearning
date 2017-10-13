package com.zj.learning.control.course.video;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zj.app.ImageLoadingHelper;
import com.zj.learning.R;
import com.zj.learning.ViewAnimHelper;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.dao.CoreSharedPreference;
import com.zj.learning.model.course.Course;
import com.zj.learning.model.course.CourseSection;
import com.zj.learning.model.course.VideoRecord;
import com.zj.support.util.CommonUtil;
import com.zj.support.util.TimeUtil;

/**
 * 视频播放基类
 * 
 * @author XingRongJing
 * 
 */
public abstract class BaseVideoActivity extends BaseActivity implements
		OnSeekBarChangeListener, SurfaceHolder.Callback, OnTouchListener,
		OnPreparedListener, OnCompletionListener, OnErrorListener {

	private static final String TAG = "BaseVideoActivity";
	public static boolean mLog = true;
	protected SurfaceView mSurface;
	protected SurfaceHolder mSurfaceHolder;
	protected RelativeLayout mRlTop, mRlLoading, mRlError;
	protected LinearLayout mLlBottom;
	protected TextView mTvFileName, mTvCurrTime, mTvTotalTime;
	protected ImageButton mBtnPlayPause;
	protected SeekBar mSeekBar;
	private MediaPlayer mMediaPlayer;

	private Timer mTimer;
	private boolean mIsTimerTaskSchedule = false;
	private int mScreenWidth, mScreenHeight;
	private long mCurrPos = 0;
	private int mProgress = 0;
	/** 顶部、底部bar是否显示 **/
	private boolean mShowing = false;
	/** 是否用户主动暂停 **/
	private boolean mUserPause = false;
	/** 视频模式-默认全屏 **/
	private int mLayout = VIDEO_LAYOUT_FULLSCREEN;
	/** 视频播放地址（远程或本地） **/
	protected String mVideoUrl = "";
	private MediaPlayerHelper mPlayHelper;
	private ViewAnimHelper mAnimHelper;
	private Course mCourse;
	protected CourseSection mSection = null;
	private ImageLoadingHelper mLoadingHelper;
	protected VideoRecord mVideoRecord;
	/** 是否播放记录跳转 **/
	private boolean mIsRecord = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_video);
		mSurface = (SurfaceView) this.findViewById(R.id.course_video_surface);
		mTvFileName = (TextView) this
				.findViewById(R.id.course_video_tv_filename);
		mTvCurrTime = (TextView) this
				.findViewById(R.id.course_video_time_current);
		mTvTotalTime = (TextView) this
				.findViewById(R.id.course_video_time_total);
		mBtnPlayPause = (ImageButton) this
				.findViewById(R.id.course_video_play_pause);
		mSeekBar = (SeekBar) this.findViewById(R.id.course_video_seekbar);

		mRlLoading = (RelativeLayout) this
				.findViewById(R.id.course_video_rl_loading);
		mRlTop = (RelativeLayout) this
				.findViewById(R.id.course_video_rl_titlebar);
		mRlError = (RelativeLayout) this
				.findViewById(R.id.course_video_rl_error);
		mLlBottom = (LinearLayout) this
				.findViewById(R.id.course_video_ll_bottom);
		ImageView iv = (ImageView) this
				.findViewById(R.id.course_video_iv_progress);
		mLoadingHelper = new ImageLoadingHelper(iv);

		mSeekBar.setOnSeekBarChangeListener(this);
		mSurfaceHolder = mSurface.getHolder();
		mSurfaceHolder.setKeepScreenOn(true);
		mSurfaceHolder.addCallback(this);
		// 4.2之后自动设置，但之前不设置的话，会有声音，但无图像
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mSurface.setOnTouchListener(this);

	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		// 禁止屏幕锁屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		this.handleIntentData(savedInstanceState);

		mScreenWidth = CommonUtil.getScreenWidth(this);
		mScreenHeight = CommonUtil.getScreenHeight(this);

		mTimer = new Timer();
		mHandler.sendEmptyMessageDelayed(MSG_FADE_OUT, DEFAULT_TIMEOUT);
		mAnimHelper = new ViewAnimHelper(this);
	}

	private void handleIntentData(Bundle savedInstanceState) {
		String temp = null;
		String course = null;
		String record = null;
		if (null == savedInstanceState) {
			Intent intent = this.getIntent();
			temp = intent.getStringExtra("courseSection");
			course = intent.getStringExtra("course");
			record = intent.getStringExtra("videoRecord");
			mIsRecord = intent.getBooleanExtra("isRecord", false);

			if (!TextUtils.isEmpty(temp)) {
				mSection = CourseSection.toObj(temp);
			}
			if (!TextUtils.isEmpty(course)) {
				mCourse = Course.toObj(course);
			}
			if (!TextUtils.isEmpty(record)) {
				mVideoRecord = VideoRecord.toObj(record);
			}
		} else {
			this.restoreData(savedInstanceState);
		}
	}

	protected void buildVideoRecord(String url) {
		if (!mIsRecord) {// 查询本地记录
			mVideoRecord = CoreSharedPreference
					.getObject(
							this,
							GlobalConfig.SharedPreferenceDao.FILENAME_VIDEO_RECORD,
							url);
		} else {
			mVideoUrl = mVideoRecord.getVideoUrl();
		}
		if (null == mVideoRecord) {
			mVideoRecord = new VideoRecord();
			if (null != mSection) {
				mVideoRecord.setSectionId(mSection.getSectionId());
				mVideoRecord.setSectionName(mSection.getName());
			}
			if (null != mCourse) {
				mVideoRecord.setCourseId(mCourse.getCourseId());
				mVideoRecord.setCourseName(mCourse.getResName());
			}
			mVideoRecord.setVideoUrl(mVideoUrl);
		} else {
			if (!mVideoRecord.isVideoFinish()) {
				mCurrPos = mVideoRecord.getCurrPos();
			}
		}

	}

	protected void setVideoName() {
		if (null != mSection) {
			mTvFileName.setText(mSection.getName());
		} else if (null != mVideoRecord) {
			mTvFileName.setText(mVideoRecord.getSectionName());
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 不可见时自动暂停
		if (null != mPlayHelper && mPlayHelper.isPlaying()) {
			mPlayHelper.pausePlay();
			this.updatePausePlay();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		this.restoreData(savedInstanceState);
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (null == outState) {// 保存数据
			return;
		}
		if (null != mSection) {
			outState.putString("courseSection", mSection.toJson());
		}
		if (null != mCourse) {
			outState.putString("course", mCourse.toJson());
		}
		if (null != mVideoRecord) {
			outState.putString("videoRecord", mVideoRecord.toJson());
		}
		outState.putLong("position", mCurrPos);
		outState.putString("path", mVideoUrl);
		outState.putBoolean("isRecord", mIsRecord);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!mShowing) {
				this.show();
			} else {
				this.hide();
			}
		}
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		int videoWidth = mp.getVideoWidth();
		int videoHeight = mp.getVideoHeight();
		if (mLog) {
			Log.d(TAG, "shan-->onPrepared(): videoWidth = " + videoWidth
					+ " height = " + videoHeight);
		}
		if (0 != videoWidth && 0 != videoHeight) {
			if (!mIsTimerTaskSchedule) {
				mTimer.schedule(mTimerTask, 0, 1000);
				mIsTimerTaskSchedule = true;
			}
			mTvTotalTime.setText(TimeUtil.formatMillTime(mp.getDuration()));
			Log.i(TAG, "shan-->onPrepared() recordPos = " + mCurrPos);
			if (mCurrPos > 0) {// 自动跳到历史记录
				// mIsNeedSeekTo = true;
				mp.seekTo((int) mCurrPos);
			}
			if (!mUserPause) {
				mp.start();
				mBtnPlayPause.setImageResource(R.drawable.video_play_selector);
			}
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (mLog) {
			Log.d(TAG, "shan-->surfaceCreated()");
		}
		this.showCacheLoading();
		this.setViewsUnenable();
		mMediaPlayer = this.buildMediaPlayer(holder);
		mPlayHelper = new MediaPlayerHelper(mMediaPlayer);
		this.onPreparePlay(mPlayHelper);
		this.setVideoLayout(mLayout);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		if (mLog) {
			Log.d(TAG, "shan-->surfaceChanged()");
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		if (mLog) {
			Log.d(TAG, "shan-->surfaceDestroyed()");
		}
		if (null != mPlayHelper) {
			mPlayHelper.release();
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (null == mMediaPlayer) {
			return;
		}
		this.mProgress = progress * mMediaPlayer.getDuration()
				/ seekBar.getMax();

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		mHandler.removeMessages(MSG_FADE_OUT);
		mHandler.sendEmptyMessageDelayed(MSG_FADE_OUT, DEFAULT_TIMEOUT);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		if (mLog) {
			Log.d(TAG, "shan-->onStopTrackingTouch()");
		}
		mMediaPlayer.seekTo(mProgress);
		int duration = mMediaPlayer.getCurrentPosition();
		if (mTvCurrTime != null) {
			mTvCurrTime.setText(TimeUtil.formatMillTime(duration));
		}
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		if (mLog) {
			Log.d(TAG, "shan-->onError(): what=" + what + " extra = " + extra
					+ " mp is null: " + (null == mp));
		}
		// this.showToast(this.getString(R.string.video_error));
		// this.destroy();
		// this.finish();
		this.showError();
		this.hideCacheLoading();
		return true;// 返回true，则不会调用onCompletion()
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (mLog) {
			Log.d(TAG, "shan-->onCompletion()");
		}
		this.showToast(this.getString(R.string.video_completion));
		this.destroy();
		this.finish();
	}

	@Override
	protected void setResultBack() {
		// TODO Auto-generated method stub
		super.setResultBack();
		this.destroy();
	}

	public void onPlayPauseClick(View view) {// 播放/暂停
		if (null == mMediaPlayer) {
			return;
		}
		if (mPlayHelper.isPlaying()) {
			mUserPause = true;
			mPlayHelper.pausePlay();
		} else {
			mUserPause = false;
			mPlayHelper.startPlay();
		}
		this.updatePausePlay();
	}

	public void onClickRetry(View view) {// 点击重试
		this.hideError();
		this.showCacheLoading();
		if (null != mMediaPlayer) {
			mMediaPlayer.reset();
			mMediaPlayer = null;
		}
		mMediaPlayer = this.buildMediaPlayer(mSurfaceHolder);
		mPlayHelper = new MediaPlayerHelper(mMediaPlayer);
		this.onPreparePlay(mPlayHelper);
		this.setVideoLayout(mLayout);
	}

	/**
	 * surfaceCreated，开始播放
	 */
	protected abstract void onPreparePlay(MediaPlayerHelper mPlayHelper);

	/**
	 * 更新播放/暂停按钮背景
	 */
	private void updatePausePlay() {
		if (null == mPlayHelper) {
			return;
		}
		if (mPlayHelper.isPlaying())
			mBtnPlayPause.setImageResource(R.drawable.video_play_selector);
		else {
			mBtnPlayPause.setImageResource(R.drawable.video_pause_selector);
		}
	}

	private MediaPlayer buildMediaPlayer(SurfaceHolder holder) {
		MediaPlayer mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setDisplay(holder);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setScreenOnWhilePlaying(true);
		return mMediaPlayer;
	}

	private void setViewsUnenable() {
		if (null == mSeekBar || null == mBtnPlayPause) {
			return;
		}
		mSeekBar.setEnabled(false);
		mBtnPlayPause.setEnabled(false);
	}

	protected void setViewsEnable() {
		if (null == mSeekBar || null == mBtnPlayPause) {
			return;
		}
		mSeekBar.setEnabled(true);
		mBtnPlayPause.setEnabled(true);
	}

	/**
	 * 重新设置SurfaceView的宽高
	 * 
	 * @param layout
	 */
	private void setVideoLayout(int layout) {
		if (null == mSurface) {
			return;
		}
		ViewGroup.LayoutParams params = mSurface.getLayoutParams();
		if (VIDEO_LAYOUT_FULLSCREEN == layout) {
			params.width = mScreenWidth;
			params.height = mScreenHeight;
		} else if (VIDEO_LAYOUT_ZOOMBYSCREEN == layout) {
			params.width = (int) (0.75f * mScreenWidth);
			params.height = (int) (0.75f * mScreenHeight);
		}
		mSurface.setLayoutParams(params);
	}

	/**
	 * 隐藏控制条
	 */
	private void hide() {
		if (null == mRlTop || null == mLlBottom) {
			return;
		}
		mShowing = false;
		mAnimHelper.startAnimation(mLlBottom, R.anim.slide_out_to_bottom,
				View.GONE);
		mAnimHelper.startAnimation(mRlTop, R.anim.slide_out_to_top, View.GONE);
	}

	/**
	 * 展示控制条
	 */
	private void show() {
		if (null == mRlTop || null == mLlBottom) {
			return;
		}
		mShowing = true;
		mAnimHelper.startAnimation(mLlBottom, R.anim.slide_in_from_bottom,
				View.VISIBLE);
		mAnimHelper.startAnimation(mRlTop, R.anim.slide_in_from_top,
				View.VISIBLE);

		mHandler.removeMessages(MSG_FADE_OUT);
		mHandler.sendEmptyMessageDelayed(MSG_FADE_OUT, DEFAULT_TIMEOUT);
	}

	private void showError() {
		if (null != mRlError) {
			mRlError.setVisibility(View.VISIBLE);
		}
	}

	private void hideError() {
		if (null != mRlError) {
			mRlError.setVisibility(View.GONE);
		}
	}

	private void cancelTimer() {
		if (null == mTimer) {
			return;
		}
		mTimer.cancel();
		mTimer = null;
	}

	private void restoreData(Bundle savedInstanceState) {
		if (null == savedInstanceState) {
			return;
		}
		mCurrPos = savedInstanceState.getLong("position");
		mVideoUrl = savedInstanceState.getString("path");
		mIsRecord = savedInstanceState.getBoolean("isRecord");
		String temp = savedInstanceState.getString("courseSection");
		String course = savedInstanceState.getString("course");
		String record = savedInstanceState.getString("videoRecord");
		if (!TextUtils.isEmpty(temp)) {
			mSection = CourseSection.toObj(temp);
		}
		if (!TextUtils.isEmpty(course)) {
			mCourse = Course.toObj(course);
		}
		if (!TextUtils.isEmpty(record)) {
			mVideoRecord = VideoRecord.toObj(record);
		}
	}

	/**
	 * 销毁Activity时调用
	 */
	private void destroy() {
		try {
			if (null != mVideoRecord && mCurrPos > 0) {
				mVideoRecord.setCurrPos(mCurrPos);
				if (null != mMediaPlayer) {
					mVideoRecord.setTotalPos(mMediaPlayer.getDuration());
				}
				mVideoRecord.setStuDate(TimeUtil.getCurrentTimeBySeconds());
				this.getCoreApp()
						.getRecordKeeper()
						.addVideoRecord(this.getApplicationContext(),
								mVideoRecord);
				if (mIsRecord) {
					Intent intent = new Intent();
					intent.putExtra("videoUrl", mVideoUrl);
					intent.putExtra("currPos", mCurrPos);
					intent.putExtra("stuDate", mVideoRecord.getStuDate());
					this.setResult(RESULT_OK, intent);
				}
			}
			mHandler.removeMessages(MSG_REFRESH_PROGRESS);
			this.cancelTimer();
			if (null != mPlayHelper) {
				mPlayHelper.release();
			}
		} catch (Exception e) {

		}
	}

	private boolean isLoadingShow() {
		return (mRlLoading != null && mRlLoading.getVisibility() == View.VISIBLE) ? true
				: false;
	}

	protected void showCacheLoading() {
		if (this.isLoadingShow()) {
			return;
		}
		mLoadingHelper.startAnimation();
		mRlLoading.setVisibility(View.VISIBLE);
	}

	protected void hideCacheLoading() {
		if (!this.isLoadingShow()) {
			return;
		}
		mLoadingHelper.stopAnimation();
		mRlLoading.setVisibility(View.GONE);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REFRESH_PROGRESS:// 每秒刷新一下进度条以及当前时间
				if (null == mMediaPlayer || null == mSeekBar) {
					return;
				}
				try {
					int position = mMediaPlayer.getCurrentPosition();
					mCurrPos = position;
					int duration = mMediaPlayer.getDuration();

					if (duration > 0) {
						long pos = mSeekBar.getMax() * position / duration;
						mSeekBar.setProgress((int) pos);
					}
					if (null != mTvCurrTime) {
						mTvCurrTime.setText(TimeUtil.formatMillTime(position));
					}
					break;
				} catch (Exception e) {

				}
			case MSG_FADE_OUT:
				hide();
				break;
			case MSG_SHOW:
				show();
				break;
			case MSG_EXIT:
				// mDoubleTabExit = false;
				break;
			}

		};
	};

	/* 通过定时器和Handler来更新进度条 */
	private TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (mPlayHelper.isPlaying() && mSeekBar.isPressed() == false) {
				mHandler.sendEmptyMessage(MSG_REFRESH_PROGRESS);
			}
		}
	};

	private static final int DEFAULT_TIMEOUT = 5 * 1000;
	private static final int MSG_REFRESH_PROGRESS = 0;
	private static final int MSG_FADE_OUT = 1;
	private static final int MSG_SHOW = 2;
	private static final int MSG_EXIT = 3;

	public static final int VIDEO_LAYOUT_FULLSCREEN = 0;
	public static final int VIDEO_LAYOUT_ZOOMBYSCREEN = 1;

	public static final int PREPARE_FAILS = 111;
}
