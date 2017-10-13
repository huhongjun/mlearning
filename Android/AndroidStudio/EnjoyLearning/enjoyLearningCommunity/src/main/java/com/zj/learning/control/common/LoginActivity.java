package com.zj.learning.control.common;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.dao.CoreSharedPreference;
import com.zj.learning.model.user.User;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.DigestUtil;

/**
 * 登录
 * 
 * @author XingRongJing
 * 
 */
public class LoginActivity extends BaseActivity implements TextWatcher {

	private static final int REQ_CODE_REGISTER = 1;
	private TextView mTvTitle;
	private EditText mEtUsername, mEtPswd;
	private LinearLayout mLlProgress;
	private User mUser;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		this.setContentView(R.layout.user_login);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mLlProgress = (LinearLayout) findViewById(R.id.user_login_ll_loading);
		mEtUsername = (EditText) findViewById(R.id.user_login_et_username);
		mEtPswd = (EditText) findViewById(R.id.user_login_et_pswd);
		mEtUsername.addTextChangedListener(this);

		this.buildLoading(mLlProgress);
		mTvTitle.setText(this.getString(R.string.login));
	}

	public void onLoginClick(View view) {
		String username = mEtUsername.getText().toString().trim();
		String pswd = mEtPswd.getText().toString().trim();

		String tips = this.validate(username, pswd);
		if (!TextUtils.isEmpty(tips)) {
			this.showToast(tips);
			return;
		}
		this.onLogin(username, pswd);
	}

	public void onClickRegister(View view) {
		this.startActivityForResult(new Intent(this, RegisterActivity.class),
				REQ_CODE_REGISTER);
	}

	private String validate(String username, String pswd) {
		if (TextUtils.isEmpty(username)) {
			return this.getString(R.string.user_name_not_null);
		}
		int lengthUsername = username.length();
		if (lengthUsername > GlobalConfig.Limit.MAX_LENGTH_USERNAME
				|| lengthUsername < GlobalConfig.Limit.MIN_LENGTH_USERNAME) {
			return this.getString(R.string.user_name_limit);
		}
		if (TextUtils.isEmpty(pswd)) {
			return this.getString(R.string.user_pswd_not_null);
		}
		int pswdLength = pswd.length();
		if (pswdLength > GlobalConfig.Limit.MAX_LENGTH_PSWD
				|| pswdLength < GlobalConfig.Limit.MIN_LENGTH_PSWD) {
			return this.getString(R.string.user_pswd_limit);
		}
		if (!this.isNetworkEnable()) {
			return this.getString(R.string.no_network);
		}
		return null;
	}

	/**
	 * 登录事件
	 * 
	 * @param memberName
	 * @param memberPassword
	 */
	private void onLogin(String memberName, String memberPassword) {
		this.closeInputMethod(mEtPswd);
		this.onFocusChangedLogining();
		this.showLoading();

		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", memberName);
		params.put("pwd", DigestUtil.getMD5(memberPassword));
		RequestTag tag = new RequestTag(mReqTag);
		this.getHttpApi().request(GlobalConfig.URL_LOGIN, params, this, tag);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		if (null != mEtPswd.getText()) {
			mEtPswd.setText("");
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		this.hideLoading();
		this.onFocusChangedLoginFinish();
		if (null == arg0) {
			return;
		}
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			this.showToast(this.getString(R.string.login_success));
			mUser = parser.getOne(User.class);
			this.saveUserInfo(mUser);
			this.setUser(mUser);
			this.setResultBack();
			this.finish();
		} else {
			this.onFails(parser.getErrorMsg());
		}
	}

	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseError(out);
		this.onFails(null);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		if (REQ_CODE_REGISTER != requestCode || null == data) {
			return;
		}
		try {
			String temp = data.getStringExtra("User");
			if (TextUtils.isEmpty(temp)) {
				return;
			}
			mUser = User.toObj(temp);
			this.saveUserInfo(mUser);
			this.setUser(mUser);
		} catch (Exception e) {
		}
		this.setResultBack();
		this.finish();

	}

	private void onFails(String tips) {
		if (TextUtils.isEmpty(tips)) {
			this.showToast(this.getString(R.string.request_fail));
		} else {
			this.showToast(tips);
		}
		this.hideLoading();
		this.onFocusChangedLoginFinish();
		this.openInputMethod();
	}

	private void onFocusChangedLogining() {
		mLlProgress.requestFocus();
		mEtUsername.setFocusable(false);
		mEtPswd.setFocusable(false);
	}

	private void onFocusChangedLoginFinish() {
		mLlProgress.clearFocus();
		mEtUsername.setFocusable(true);
		mEtUsername.setFocusableInTouchMode(true);
		mEtPswd.setFocusable(true);
		mEtPswd.setFocusableInTouchMode(true);
		mEtUsername.requestFocus();
	}

	private void closeInputMethod(EditText edt) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		boolean isOpen = imm.isActive();
		if (isOpen) {
			imm.hideSoftInputFromWindow(edt.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private void openInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 没有显示则显示
	}

	private void saveUserInfo(User user) {
//		CoreSharedPreference.saveMember(this, user);
		CoreSharedPreference.saveObject(this, GlobalConfig.SharedPreferenceDao.FILENAME_USER, GlobalConfig.SharedPreferenceDao.KEY_USER, user);
	}

	@Override
	protected void setResultBack() {
		// TODO Auto-generated method stub
		super.setResultBack();
		if (null != mUser) {
			this.setResult(RESULT_OK);
		} else {
			this.setResult(RESULT_CANCELED);
		}
	
	}

 
	
}
