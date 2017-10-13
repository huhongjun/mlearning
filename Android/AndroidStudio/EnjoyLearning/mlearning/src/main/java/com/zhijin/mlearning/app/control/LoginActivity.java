package com.zhijin.mlearning.app.control;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.api.StoreOperation;
import com.zhijin.mlearning.app.constant.GlobalConfig;
import com.zhijin.mlearning.app.control.base.BaseActivity;
import com.zhijin.mlearning.app.dao.preference.SharedPreferenceDao;
import com.zhijin.mlearning.app.model.LearningProgress;
import com.zhijin.mlearning.support.network.Param;
import com.zhijin.mlearning.support.sqlite.SqliteUtil;
import com.zhijin.mlearning.support.util.MyToast;

public class LoginActivity extends BaseActivity
{
	EditText memberEmailEt;
	String spEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		mEnableDoubleExit = true;

		// 获得本地缓存的用户登录邮箱，无需用户再次填写
		spEmail = SharedPreferenceDao.getLocalData(this, GlobalConfig.KEY_USER);
		memberEmailEt = (EditText) findViewById(R.id.memberId);
		memberEmailEt.setText(spEmail);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
	}

	public void onBtnClick(View view)
	{
		String stEmail = memberEmailEt.getText().toString();
		if (!CoreApp.getApp().isEmail(stEmail)) 
		{
			MyToast.showText("格式不对，请输入正确的邮箱");
			memberEmailEt.setText("");
			
			return;
		}
		if (!stEmail.equals(spEmail)) {
			SharedPreferenceDao.saveLocalData(this, GlobalConfig.KEY_USER, stEmail);
		}
		
		Intent intent = new Intent(this, CourseChapterActivity.class);
		intent.putExtra("memberEmail", stEmail);
		startActivity(intent);
		CoreApp.getApp().setMemberId(stEmail);
		
		// 访问服务器获得学习进度 =>callback notifySuccessFindLearningProgress
		// 
		Param param = new Param();
		param.setHashCode(this.hashCode());
		if (CoreApp.getApp().isNetworkConnected()) {
			StoreOperation.getInstance().getLearingProgress(param);	// 异步
		} else {
			// 网络不通时，获取本地SQLLite的学习进度
			List<LearningProgress> learningProgressList = SqliteUtil.getInstance().getLocalProgressList();
			CoreApp.getApp().setLearingProgressList(learningProgressList);
		}
	}

	/**
	 * 获取学习记录成功(成功后 将得到的同步到本地。同时将内存中的记录更新)
	 */
	@Override
	public void notifySuccessFindLearningProgress(Param out)
	{
		if (out.getResult() != null) {
			try {
				List<LearningProgress> learningProgressListResult = (List<LearningProgress>) out.getResult();
				for (int i = 0; i < learningProgressListResult.size(); i++) {
					LearningProgress learningProgress = (LearningProgress) learningProgressListResult.get(i);
					SqliteUtil.getInstance().saveOrUpdateDomain(learningProgress);
				}
				CoreApp.getApp().setLearingProgressList(learningProgressListResult);
			} catch (Exception e) {
				CoreApp.getApp().setLearingProgressList(null);
			}
		}
	}

	@Override
	public void onFails(Param out)
	{
		CoreApp.getApp().setLearingProgressList(SqliteUtil.getInstance().getLocalProgressList());
		super.onFails(out);
	}

}
