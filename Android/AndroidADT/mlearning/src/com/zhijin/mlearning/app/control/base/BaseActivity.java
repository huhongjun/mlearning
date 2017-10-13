package com.zhijin.mlearning.app.control.base;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.api.StoreOperation;
import com.zhijin.mlearning.support.network.Param;
import com.zhijin.mlearning.support.network.inter.ICallback;
import com.zhijin.mlearning.support.network.obsever.Observer;

/**
 * 整个项目Activity的基类(除了TabActivity，其有自己基类，但逻辑类型此基类)
 * 
 * @author XingRong m
 */
public class BaseActivity extends Activity implements ICallback
{

	/** 是否允许双击退出程序 （默认不允许） **/
	protected boolean mEnableDoubleExit = false;
	private boolean mDoubleExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		this.addDelegate();
		this.prepareViews();
		this.prepareDatas();
	}

	@Override
	protected void onResume()
	{
		this.prepareViewsChanged();
		this.prepareDatasChanged();
		super.onResume();
	}

	@Override
	protected void onPause()
	{
		this.preparePause();
		super.onPause();
	}

	/**
	 * 暂停--onPause()里
	 */
	protected void preparePause()
	{

	}

	@Override
	protected void onStop()
	{
		this.prepareRelease();
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		this.removeDelegate();
		this.prepareDestroy();
		super.onDestroy();
	}

	protected void prepareViews()
	{

	}

	protected void prepareViewsChanged()
	{

	}

	protected void prepareDatasChanged()
	{

	}

	protected void prepareDatas()
	{
	}

	protected void prepareRelease()
	{

	}

	protected void prepareDestroy()
	{

	}

	private void addDelegate()
	{
		// 注册消息接收
		Observer.getObserver().addCallback(this.hashCode(), this);
		// 
		CoreApp.getApp().addActivity(this);
	}

	private void removeDelegate()
	{
		Observer.getObserver().deleteCallback(this.hashCode(), this);
		CoreApp.getApp().popActivity(this);
	}

	@Override
	public void onBackPressed()
	{
		if (!mEnableDoubleExit) {
			super.onBackPressed();
			return;
		}
		if (mDoubleExit) {
			super.onBackPressed();
			CoreApp.getApp().exit();
			return;
		}
		mDoubleExit = true;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run()
			{
				mDoubleExit = false;
			}
		}, 2000);
		this.showToast(this.getString(R.string.exit));
	}

	protected void showToast(String msg)
	{
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	protected StoreOperation getStoreOperation()
	{
		return StoreOperation.getInstance();
	}

	@Override
	public void notifySuccessSaveLearningProgress(Param out)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void notifySuccessFindLearningProgress(Param out)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void onFails(Param out)
	{
	}
}
