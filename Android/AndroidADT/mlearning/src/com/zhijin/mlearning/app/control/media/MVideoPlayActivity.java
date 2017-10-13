package com.zhijin.mlearning.app.control.media;

import java.util.Formatter;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.constant.GlobalConfig;
import com.zhijin.mlearning.app.control.base.BaseActivity;
import com.zhijin.mlearning.app.model.Course;
import com.zhijin.mlearning.app.model.LearningProgress;
import com.zhijin.mlearning.support.sqlite.SqliteUtil;

public class MVideoPlayActivity extends BaseActivity implements OnCompletionListener, OnPreparedListener, OnErrorListener,
		OnBufferingUpdateListener, OnSeekBarChangeListener, SurfaceHolder.Callback, OnTouchListener,
		DialogInterface.OnClickListener, OnInfoListener
{

	protected SurfaceView mSurface;
	protected SurfaceHolder mSurfaceHolder;
	protected RelativeLayout mRlTop;
	protected LinearLayout mLlBottom;
	protected TextView mTvFileName, mTvCurrTime, mTvTotalTime;
	protected ImageButton mBtnPlayPause;
	protected SeekBar mSeekBar;
	protected MediaPlayer mMediaPlayer;
	protected Course mMiCourse;
	/** 视频路径或URI **/
	protected String mPath;
	protected boolean mShowing = false;
	protected boolean mDoubleTabExit = false;
	protected int mProgress = 0;
	protected StringBuilder mFormatBuilder;
	protected Formatter mFormatter;
	protected Timer mTimer;
	/** 播放记录 **/
	protected LearningProgress mLearningProgress;
	protected long mPausePosition = 0l;
	protected long lastClickTime = 0l;
	protected int mScreenWidth, mScreenHeight;
	protected int mLayout = VIDEO_LAYOUT_ZOOMBYSCREEN;
	/** 初始时，是否需要seekTo **/
	private boolean mIsNeedSeekTo = false;
	private boolean mIsTimerTaskSchedule = false;
	private int mBufferPercent = 0;
	private int mAttempts = 0;
	private int mCurrPos = 0;
	private LearningProgress learningProgress;

	@Override
	protected void prepareViews()
	{
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.prepareViews();
		this.setContentView(R.layout.video_view);
		mSurface = (SurfaceView) this.findViewById(R.id.video_view_surface);
		mTvFileName = (TextView) this.findViewById(R.id.video_view_tv_filename);
		mTvCurrTime = (TextView) this.findViewById(R.id.video_view_time_current);
		mTvTotalTime = (TextView) this.findViewById(R.id.video_view_time_total);
		mBtnPlayPause = (ImageButton) this.findViewById(R.id.video_view_play_pause);
		mSeekBar = (SeekBar) this.findViewById(R.id.video_view_seekbar);

		mRlTop = (RelativeLayout) this.findViewById(R.id.video_view_rl_titlebar);
		mLlBottom = (LinearLayout) this.findViewById(R.id.video_view_ll_bottom);

		mSeekBar.setOnSeekBarChangeListener(this);
		mSurfaceHolder = mSurface.getHolder();
		mSurfaceHolder.setKeepScreenOn(true);
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

		mSurface.setOnTouchListener(this);
	}

	@Override
	protected void prepareDatas()
	{
		super.prepareDatas();
		// 禁止屏幕锁屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		DisplayMetrics disp = this.getResources().getDisplayMetrics();
		mScreenWidth = disp.widthPixels;
		mScreenHeight = disp.heightPixels;

		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

		mTimer = new Timer();
		mHandler.sendEmptyMessageDelayed(MSG_FADE_OUT, sDefaultTimeout);
		Intent intent = this.getIntent();
		mPath = intent.getStringExtra(GlobalConfig.KEY_COURSEPATH);
		mMiCourse = (Course) intent.getSerializableExtra(GlobalConfig.KEY_COURSE);
		if (null != mMiCourse) {
			String mFileName = mMiCourse.getName();
			mTvFileName.setText(mFileName);
		}
	}

	@Override
	protected void prepareViewsChanged()
	{
		super.prepareViewsChanged();
		if (!this.isPlaying()) {
			this.startPlay();
			this.updatePausePlay();
		}
	}

	@Override
	protected void preparePause()
	{
		super.preparePause();
		if (null != mMediaPlayer) {
			mPausePosition = mMediaPlayer.getCurrentPosition();
		}
		if (this.isPlaying()) {
			this.pausePlay();
			this.updatePausePlay();
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp)
	{
		int videoWidth = mp.getVideoWidth();
		int videoHeight = mp.getVideoHeight();
		if (0 != videoWidth && 0 != videoHeight) {
			if (!mIsTimerTaskSchedule) {
				mTimer.schedule(mTimerTask, 0, 1000);
				mIsTimerTaskSchedule = true;
			}
			mTvTotalTime.setText(this.stringForTime(mp.getDuration()));
			long recordPos = this.getHistoryPosition();// 此处为 获取学习进度
			// Log.i(TAG, "zj onPrepared() recordPos = " + recordPos);
			if (recordPos > 0) {
				mIsNeedSeekTo = true;
				mp.seekTo((int) recordPos);
			}
			mp.start();
			this.setViewsEnable();
		}

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent)
	{
		mBufferPercent = percent;
		if (null == mSeekBar) {
			return;
		}
		mSeekBar.setSecondaryProgress(percent);
		int progress = mSeekBar.getProgress();
		if (mIsNeedSeekTo) {
			if (percent > progress) {// 在线需要seekTo且缓冲进度大于当前进度时，其他情况在onPrepared()
				this.setViewsEnable();
				// this.hideLoading();
				mIsNeedSeekTo = false;
			}
		}
	}

	@Override
	public boolean onInfo(MediaPlayer mp, int what, int extra)
	{
		Log.i(TAG, "zj onInfo() what = " + what);
		switch (what) {
		case MEDIA_INFO_BUFFERING_START:
			// this.showLoading();
			break;
		case MEDIA_INFO_BUFFERING_END:
			mp.start();
			// this.hideLoading();
			break;
		}
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{
		this.showToast("播放完毕");
		this.destroy();
		this.finish();
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra)
	{
		Log.e(TAG, "zj onError() what = " + what + " extra = " + extra + " mCurrPos = " + mCurrPos);
		if (mAttempts < MAX_ATTEMPT) {
			// this.showLoading();
			mAttempts++;
			// 此处为 保存当前播放记录
			// this.saveProgress(mCurrPos);
			this.release();
			this.openVideo();
		} else {
			this.showDialog(DIALOG_ERROR_ID);
		}
		return true;// 返回true，则不会调用onCompletion()
	}

	@Override
	public void onClick(DialogInterface dialog, int which)
	{
		this.dismissDialog(DIALOG_ERROR_ID);
		this.destroy();
		this.finish();
	}

	@Override
	public void onBackPressed()
	{
		if (mDoubleTabExit) {
			this.destroy();
			super.onBackPressed();
			return;
		}
		mDoubleTabExit = true;
		this.showToast(this.getString(R.string.exit));
		mHandler.sendEmptyMessageDelayed(MSG_EXIT, 2000);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
	{
		if (null == mMediaPlayer) {
			return;
		}
		this.mProgress = progress * mMediaPlayer.getDuration() / seekBar.getMax();

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar)
	{

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar)
	{
		if (null == mMediaPlayer) {
			return;
		}
		mMediaPlayer.seekTo(mProgress);
		int duration = mMediaPlayer.getCurrentPosition();
		if (mTvCurrTime != null) {
			mTvCurrTime.setText(this.stringForTime(duration));
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		this.setViewsUnenable();
		this.release();
		this.openVideo();
		this.setVideoLayout(mLayout);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
	{

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (!mShowing) {
				this.show();
			}
			if (null == mSurface) {
				return false;
			}
			if (System.currentTimeMillis() - lastClickTime < 500) {// 双击
				if (VIDEO_LAYOUT_FULLSCREEN == mLayout) {
					mLayout = VIDEO_LAYOUT_ZOOMBYSCREEN;
				} else if (VIDEO_LAYOUT_ZOOMBYSCREEN == mLayout) {
					mLayout = VIDEO_LAYOUT_FULLSCREEN;
				}
				this.setVideoLayout(mLayout);
				return false;
			} else {
				lastClickTime = System.currentTimeMillis();
			}

		}
		return false;
	}

	public void onPlayPauseClick(View view)
	{
		if (null == mMediaPlayer) {
			return;
		}
		if (this.isPlaying()) {
			this.pausePlay();
		} else {
			this.startPlay();
		}
		this.updatePausePlay();
	}

	/**
	 * 更新播放/暂停按钮背景
	 */
	private void updatePausePlay()
	{
		if (null == mMediaPlayer) {
			return;
		}
		if (this.isPlaying())
			mBtnPlayPause.setImageResource(R.drawable.video_play_selector);
		else {
			mBtnPlayPause.setImageResource(R.drawable.mediacontroller_pause_button);
		}
	}

	public void onBackBtnClick(View view)
	{
		this.destroy();
		this.finish();
	}

	private void setViewsUnenable()
	{
		if (null == mSeekBar || null == mBtnPlayPause) {
			return;
		}
		mSeekBar.setEnabled(false);
		mBtnPlayPause.setEnabled(false);
	}

	private void setViewsEnable()
	{
		if (null == mSeekBar || null == mBtnPlayPause) {
			return;
		}
		mSeekBar.setEnabled(true);
		mBtnPlayPause.setEnabled(true);
	}

	private void openVideo()
	{
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setDisplay(mSurfaceHolder);
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mMediaPlayer.setDataSource(mPath);
			mMediaPlayer.prepare();
		} catch (Exception e) {
			this.onError(mMediaPlayer, 0, 0);
			e.printStackTrace();
		}
		mMediaPlayer.setOnCompletionListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setScreenOnWhilePlaying(true);
	}

	private boolean isPlaying()
	{
		return null != mMediaPlayer && mMediaPlayer.isPlaying() ? true : false;
	}

	private void startPlay()
	{
		if (null != mMediaPlayer) {
			mMediaPlayer.start();
		}
	}

	private void pausePlay()
	{
		if (null != mMediaPlayer) {
			mMediaPlayer.pause();
		}
	}

	private void stopPlay()
	{
		if (null != mMediaPlayer) {
			mMediaPlayer.stop();
		}
	}

	private void release()
	{
		if (null != mMediaPlayer) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	/**
	 * 隐藏控制条
	 */
	private void hide()
	{
		if (null == mRlTop || null == mLlBottom) {
			return;
		}
		mShowing = false;
		mRlTop.setVisibility(View.GONE);
		mLlBottom.setVisibility(View.GONE);
	}

	/**
	 * 展示控制条
	 */
	private void show()
	{
		if (null == mRlTop || null == mLlBottom) {
			return;
		}
		mShowing = true;
		mRlTop.setVisibility(View.VISIBLE);
		mLlBottom.setVisibility(View.VISIBLE);

		mHandler.removeMessages(MSG_FADE_OUT);
		mHandler.sendEmptyMessageDelayed(MSG_FADE_OUT, sDefaultTimeout);
	}

	/**
	 * 重新设置SurfaceView的宽高
	 * 
	 * @param layout
	 */
	private void setVideoLayout(int layout)
	{
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
	 * 销毁Activity时调用
	 */
	private void destroy()
	{
		if (null != mMediaPlayer) {
			if (mMediaPlayer.getCurrentPosition() > 0) {
				this.saveProgress(mMediaPlayer.getCurrentPosition());
			}
		}
		this.cancelTimer();
		if (this.isPlaying()) {
			this.stopPlay();
		}
		this.release();
	}

	private void saveProgress(int pos)
	{
		if (learningProgress == null) {
			learningProgress = new LearningProgress();
			CoreApp.getApp().getLearingProgressList().add(learningProgress);
		}
		learningProgress.setMemberId(CoreApp.getApp().getMemberId());
		learningProgress.setSectionId(mMiCourse.getId());
		learningProgress.setSectionName(mMiCourse.getName());
		learningProgress.setSectionProgress(pos);
		if (CoreApp.getApp().getLearingProgressList().contains(learningProgress)) {
			int index = CoreApp.getApp().getLearingProgressList().indexOf(learningProgress);
			learningProgress.setId(CoreApp.getApp().getLearingProgressList().get(index).getId());
			CoreApp.getApp().getLearingProgressList().set(index, learningProgress);
		} else {
			CoreApp.getApp().getLearingProgressList().add(learningProgress);
		}

		SqliteUtil.getInstance().saveOrUpdateDomain(learningProgress);
		learningProgress = null;
	}

	/**
	 * 获取某视频之前的播放位置
	 * 
	 * @return
	 */
	private long getHistoryPosition()
	{
		if (mPausePosition > 0) {
			return mPausePosition;
		}
		if (null == mMiCourse) {
			return 0l;
		}
		long tempPos = 0l;
		// 当前播放的视频还没有播放记录，则生成一个记录，否则不生成
		if (null != CoreApp.getApp().getLearingProgressList() && CoreApp.getApp().getLearingProgressList().size() > 0) {
			for (int i = 0; i < CoreApp.getApp().getLearingProgressList().size(); i++) {
				if (CoreApp.getApp().getLearingProgressList().get(i).getSectionId() == mMiCourse.getId()) {
					learningProgress = (LearningProgress) CoreApp.getApp().getLearingProgressList().get(i);
					tempPos = learningProgress.getSectionProgress();
					return tempPos;
				} else {
					continue;
				}
			}
		} else {
			learningProgress = new LearningProgress();
			CoreApp.getApp().getLearingProgressList().add(learningProgress);
		}
		return tempPos;
	}

	private void cancelTimer()
	{
		if (null == mTimer) {
			return;
		}
		mTimer.cancel();
		mTimer = null;
	}

	/* 通过定时器和Handler来更新进度条 */
	private TimerTask mTimerTask = new TimerTask() {
		@Override
		public void run()
		{
			if (isPlaying() && mSeekBar.isPressed() == false) {
				mHandler.sendEmptyMessage(MSG_REFRESH_PROGRESS);
			}
		}
	};

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			case MSG_REFRESH_PROGRESS:// 每秒刷新一下进度条以及当前时间
				if (null == mMediaPlayer || null == mSeekBar) {
					return;
				}
				int position = mMediaPlayer.getCurrentPosition();
				mCurrPos = position;
				int duration = mMediaPlayer.getDuration();

				if (duration > 0) {
					long pos = mSeekBar.getMax() * position / duration;
					mSeekBar.setProgress((int) pos);
				}
				if (null != mTvCurrTime) {
					mTvCurrTime.setText(stringForTime(position));
				}
				break;
			case MSG_FADE_OUT:
				hide();
				break;
			case MSG_SHOW:
				show();
				break;
			case MSG_EXIT:
				mDoubleTabExit = false;
				break;
			}

		};
	};

	private String stringForTime(int timeMs)
	{
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

	private static final String TAG = "BaseVideoPlayActivity";
	private static final int DIALOG_ERROR_ID = 1;
	public static final int VIDEO_LAYOUT_FULLSCREEN = 0;
	public static final int VIDEO_LAYOUT_ZOOMBYSCREEN = 1;
	private static final int sDefaultTimeout = 5000;
	private static final int MSG_REFRESH_PROGRESS = 0;
	private static final int MSG_FADE_OUT = 1;
	private static final int MSG_SHOW = 2;
	private static final int MSG_EXIT = 3;
	private static final int MAX_ATTEMPT = 3;

	/** 缓冲视频开始--与MediaPlayer中保持一致 **/
	private static final int MEDIA_INFO_BUFFERING_START = 701;
	/** 缓冲视频结束--与MediaPlayer中保持一致 **/
	private static final int MEDIA_INFO_BUFFERING_END = 702;
}
