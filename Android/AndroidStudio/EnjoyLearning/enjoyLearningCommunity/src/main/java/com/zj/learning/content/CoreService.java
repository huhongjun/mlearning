package com.zj.learning.content;

import com.zj.learning.RecordKeeper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 核心服务
 * 
 * @author XingRongJing
 * 
 */
public class CoreService extends Service {

	private static final String TAG = "CoreService";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.d(TAG, "shan-->CoreService is created");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "shan-->CoreService is destroy");
	}

}
