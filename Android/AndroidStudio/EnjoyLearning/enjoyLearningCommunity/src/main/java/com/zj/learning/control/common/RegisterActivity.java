package com.zj.learning.control.common;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.zj.learning.model.user.User;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.DigestUtil;

/**
 * 注册
 * 
 * @author XingRongJing
 * 
 */
public class RegisterActivity extends BaseActivity implements TextWatcher {

	private TextView mTvTitle;
	private EditText mEtUsername, mEtPswd, mEtPswdSure;
	private LinearLayout mLlProgress;
	private User mUser;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		this.setContentView(R.layout.user_register);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mLlProgress = (LinearLayout) findViewById(R.id.user_register_ll_loading);
		mEtUsername = (EditText) findViewById(R.id.user_register_et_username);
		mEtPswd = (EditText) findViewById(R.id.user_register_et_pswd);
		mEtPswdSure = (EditText) findViewById(R.id.user_register_et_pswd_sure);
		mEtUsername.addTextChangedListener(this);
		this.buildLoading(mLlProgress);
		mTvTitle.setText(this.getString(R.string.register));
	}

	public void onRegisterClick(View view) {
		String username = mEtUsername.getText().toString().trim();
		String pswd = mEtPswd.getText().toString().trim();
		String pswdSure = mEtPswdSure.getText().toString().trim();

		String tips = this.validate(username, pswd, pswdSure);
		if (!TextUtils.isEmpty(tips)) {
			this.showToast(tips);
			return;
		}
		this.onRegister(username, pswd, pswdSure);
	}

	private void onRegister(String username, String pswd, String pswdSure) {
		this.closeInputMethod(mEtPswd);
		this.onFocusChangedLogining();
		this.showLoading();
		Map<String, String> params = new HashMap<String, String>();
		params.put("content",
				this.buildContent(username, DigestUtil.getMD5(pswd)));
		this.getHttpApi().request(GlobalConfig.URL_REGISTER, params, this,
				new RequestTag(mReqTag));
	}

	private String validate(String username, String pswd, String pswdSure) {
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
		if (TextUtils.isEmpty(pswdSure)) {
			return this.getString(R.string.user_pswd_sure_not_null);
		}
		if (!pswdSure.equals(pswd)) {
			return this.getString(R.string.user_pswd_not_equals);
		}
		if (!this.isNetworkEnable()) {
			return this.getString(R.string.no_network);
		}
		return null;
	}

	private String buildContent(String loginName, String pswd) {
		JSONObject json = new JSONObject();
		try {
			json.put("loginName", loginName);
			json.put("pwd", pswd);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json.toString();
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
			mUser = parser.getOne(User.class);
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

	private void onFails(String error) {
		if(TextUtils.isEmpty(error)){
			this.showToast(this.getString(R.string.request_fail));
		}else{
			this.showToast(error);
		}
		this.hideLoading();
		this.onFocusChangedLoginFinish();
		this.openInputMethod();
	}

	private void onFocusChangedLogining() {
		mLlProgress.requestFocus();
		mEtUsername.setFocusable(false);
		mEtPswd.setFocusable(false);
		mEtPswdSure.setFocusable(false);
	}

	private void onFocusChangedLoginFinish() {
		mLlProgress.clearFocus();
		mEtUsername.setFocusable(true);
		mEtUsername.setFocusableInTouchMode(true);
		mEtPswd.setFocusable(true);
		mEtPswd.setFocusableInTouchMode(true);
		mEtPswdSure.setFocusable(true);
		mEtPswdSure.setFocusableInTouchMode(true);
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

	@Override
	protected void setResultBack() {
		// TODO Auto-generated method stub
		super.setResultBack();
		if (null != mUser) {
			Intent intent = new Intent();
			intent.putExtra("User", mUser.toJson());
			this.setResult(RESULT_OK, intent);
		} else {
			this.setResult(RESULT_CANCELED);
		}
	
	}

	 
}
