package com.zhijin.mlearning.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.constant.GlobalConfig;
import com.zhijin.mlearning.app.model.LearningProgress;
import com.zhijin.mlearning.support.network.Param;
import com.zhijin.mlearning.support.network.obsever.Observer;
import com.zhijin.mlearning.support.util.MyToast;
import com.zhijin.mlearning.support.util.sdcard.DevInfo;
import com.zhijin.mlearning.support.util.sdcard.ExtSDCardInfo;

public class CoreApp extends Application implements Handler.Callback
{

	private ExecutorService mExecutors = Executors.newFixedThreadPool(5);
	private static CoreApp coreApp;
	private Handler mHandler = new Handler(this);
	private static Stack<Activity> mActivityStack;
	private String memberId;
	private DevInfo devInfo;
	private ExtSDCardInfo sdCardInfo;
	private List<LearningProgress> learingProgressList;// 当前用户的所有学习记录

	@Override
	public void onCreate()
	{
		super.onCreate();
		coreApp = this;
	}

	/**
	 * 添加Activity到容器中
	 */
	public void addActivity(Activity activity)
	{
		if (mActivityStack == null) {
			mActivityStack = new Stack<Activity>();
		}
		mActivityStack.add(activity);
	}

	public List<LearningProgress> getLearingProgressList()
	{
		if (learingProgressList == null) {
			learingProgressList = new ArrayList<LearningProgress>();
		}
		return learingProgressList;
	}

	public void setLearingProgressList(List<LearningProgress> learingProgressList)
	{
		this.learingProgressList = learingProgressList;
	}

	/**
	 * 退出栈顶Activity
	 * 
	 * @param activity
	 */
	public void popActivity(Activity activity)
	{
		if (mActivityStack != null) {
			if (activity != null) {
				int index = mActivityStack.indexOf(activity);
				if (index != -1) {
					mActivityStack.remove(index);
				}
			}
		}
	}

	public static CoreApp getApp()
	{
		return coreApp;
	}

	public String getMemberId()
	{
		return memberId;
	}

	public void setMemberId(String memberId)
	{
		this.memberId = memberId;
	}

	public ExecutorService getExecutors()
	{
		return mExecutors;
	}

	public void notifyOperationSuccess(Param out)
	{
		this.sendMsgToTarget(R.id.msg_success, out);
	}

	public void notifyOperationFails(Param out)
	{
		this.sendMsgToTarget(R.id.msg_fails, out);
	}

	private void sendMsgToTarget(int msgId, Param out)
	{
		Message msg = mHandler.obtainMessage(msgId);
		msg.obj = out;
		msg.sendToTarget();
	}

	@Override
	public boolean handleMessage(Message msg)
	{
		switch (msg.what) {
		case R.id.msg_success:
			this.dispatchSuccess((Param) msg.obj);
			break;
		case R.id.msg_fails:
			this.dispatchFails((Param) msg.obj);
			break;
		}
		return false;
	}

	/**
	 * 根据方法类型分发给各个监听者请求的成功结果
	 * 
	 * @param param
	 */
	private void dispatchSuccess(Param param)
	{
		int method = param.getMethod();
		switch (method) {
		case GlobalConfig.Interface.I_GETPROGRESS:
			Observer.getObserver().notifySuccessFindLearningProgress(param);
			break;
		case GlobalConfig.Interface.I_SAVEPROGRESS:
			Observer.getObserver().notifySuccessSaveLearningProgress(param);
			break;
		}
	}

	/**
	 * 处理请求出错（暂时不分发到具体的错误方法）
	 * 
	 * @param param
	 */
	private void dispatchFails(Param param)
	{
		Observer.getObserver().notifyFails(param);
		Object result = param.getResult();
		if (null != result) {
			if (result instanceof List<?>) {
				List<Object> list = (List<Object>) result;
				for (Object temp : list) {
					MyToast.showToast(temp);
				}
			} else if (result instanceof Object[]) {
				Object[] objs = (Object[]) result;
				for (Object temp : objs) {
					MyToast.showToast(temp);
				}
			} else {
				// MyToast.showText(this.getString(R.string.netError));
			}
		} else {
			// MyToast.showText(this.getString(R.string.netError));
		}
	}

	private void popAllActivity()
	{
		if (null == mActivityStack) {
			return;
		}
		for (int i = mActivityStack.size() - 1; i >= 0; i--) {
			if (!mActivityStack.get(i).isFinishing()) {
				mActivityStack.get(i).finish();
				mActivityStack.remove(i);
			}
		}
		mActivityStack = null;
	}

	/**
	 * 检查当前是否有网络连接
	 * 
	 * @return
	 */
	public boolean isNetworkConnected()
	{
		ConnectivityManager connectivity = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					// 当网络的状态为连接时，返回true，否则返回false
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 退出应用程序
	 * 
	 * @param isLogout
	 *            是否注销
	 */
	public void exit()
	{
		// Step 1:退出activity栈
		this.popAllActivity();
		mHandler = null;
		coreApp = null;
		if (null != mExecutors) {
			mExecutors.shutdown();
			mExecutors = null;
		}
		System.exit(0);
	}

	/**
	 * 本地是否存在离线文件
	 * 
	 * @return Path 存在则返回路径，否则返回null
	 */
	public String getOfflineFileExist(String courseUrl)
	{
//		if (TextUtils.isEmpty(courseUrl)) {
//			return null;
//		}
//		try {
//			String filePath = null;
//			sdCardInfo = ExtSDCardInfo.getInstance();
//			devInfo = sdCardInfo.getExternalInfo();
//			if (devInfo.getPath().contains("sdcard")) {
//				filePath = devInfo.getPath() + courseUrl;
//			}
//			File file = new File(filePath);
//			if (file.exists()) {
//				return filePath;
//			} else {
//				filePath = getsdCardFileIsExist(courseUrl);
//				return filePath;
//			}
//		} catch (Exception e) {
//			return null;
//		}
		
		return "/storage/4F35-EF74/"+courseUrl;
	}

	/**
	 * 本地是否存在离线文件(没有sd卡的情况)
	 * 
	 * @return file 存在则返回File，否则返回null
	 */
	private String getsdCardFileIsExist(String courseUrl)
	{
		if (TextUtils.isEmpty(courseUrl)) {
			return null;
		}
		try {
			String devoPath = Environment.getExternalStorageDirectory().getAbsolutePath();
			String filePath = devoPath + courseUrl;
			File file = new File(filePath);
			if (file.exists()) {
				return filePath;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public boolean isEmail(String email)
	{
		String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		Pattern emailer = Pattern.compile(check);
		return emailer.matcher(email.trim()).matches();
	}
}
