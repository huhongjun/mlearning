package com.zj.learning.control.course.video;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnInfoListener;
import android.net.Uri;

public class MediaPlayerHelper {

	private MediaPlayer mMediaPlayer;

	public MediaPlayerHelper(MediaPlayer player) {
		if (null == player) {
			throw new IllegalArgumentException("MediaPlayer cannot be null");
		}
		this.mMediaPlayer = player;
	}

	public void setOnInfoListener(OnInfoListener listener) {
		if (null != mMediaPlayer) {
			mMediaPlayer.setOnInfoListener(listener);
		}
	}

	public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
		if (null != mMediaPlayer) {
			mMediaPlayer.setOnBufferingUpdateListener(listener);
		}
	}

	public boolean isPlaying() {
		return null != mMediaPlayer && mMediaPlayer.isPlaying() ? true : false;
	}

	public void startPlay() {
		if (null != mMediaPlayer) {
			mMediaPlayer.start();
		}
	}

	public void pausePlay() {
		if (null != mMediaPlayer) {
			mMediaPlayer.pause();
		}
	}

	public void stopPlay() {
		if (null != mMediaPlayer) {
			mMediaPlayer.stop();
		}
	}

	public void release() {
		if (this.isPlaying()) {
			this.stopPlay();
		}
		if (null != mMediaPlayer) {
			mMediaPlayer.reset();
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

	public void prepare(String url) throws IllegalStateException, IOException {
		if (null != mMediaPlayer) {
			mMediaPlayer.setDataSource(url);
			mMediaPlayer.prepare();
		}
	}

	public void prepareAsyc(Context ctx, String url)
			throws IllegalStateException, IOException {
		if (null != mMediaPlayer) {
			mMediaPlayer.setDataSource(ctx, Uri.parse(url));
			mMediaPlayer.prepareAsync();
		}
	}
}
