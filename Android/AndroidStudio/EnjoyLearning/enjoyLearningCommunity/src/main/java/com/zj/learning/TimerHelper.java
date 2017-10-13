package com.zj.learning;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.zj.support.util.TimeUtil;

public class TimerHelper implements Handler.Callback {

	private static final int MSG_TIMER = 1;
	private static final int PERIOD = 1000;
	/** 时间（秒） **/
	private long mDuration = 0;
	private Timer mTimer;
	private TextView mTvTime;
	private Handler mHandler;

	public TimerHelper(TextView mTvTime) {
		this.mTvTime = mTvTime;
		mHandler = new Handler(this);
	}

	public void startTimer() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mHandler.sendEmptyMessage(MSG_TIMER);
			}
		}, 0, PERIOD);
	}

	public long getDuration() {
		return mDuration;
	}

	public void stopTimer() {
		if (null != mTimer) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case MSG_TIMER:
			++mDuration;
			if (null != mTvTime) {
				mTvTime.setText(TimeUtil.formatMillTime(mDuration * 1000));
			}
			break;
		}
		return false;
	}
}
