package com.zj.learning;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * 无tile带确认、取消按钮的Dialog
 * 
 * @author XingRongJing
 * 
 */
public class CustomDialog extends DialogFragment implements OnClickListener {

	private static DialogInterface.OnClickListener mDialogListener;
	private TextView mTvMsg;

	public static CustomDialog newInstance(String title, String msg,
			DialogInterface.OnClickListener listener) {
		CustomDialog fd = new CustomDialog();
		Bundle b = new Bundle();
		b.putString("msg", msg);
		b.putString("title", title);
		mDialogListener = listener;
		fd.setStyle(0, R.style.custom_dialog_style);
		fd.setArguments(b);
		return fd;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.custom_dialog, null);
		mTvMsg = (TextView) view.findViewById(R.id.dialog_tv_msg);
		Bundle b = this.getArguments();
		mTvMsg.setText(b.getString("msg"));
		Button mBtnSure = (Button) view.findViewById(R.id.dialog_btn_sure);
		Button mBtnCancel = (Button) view.findViewById(R.id.dialog_btn_cancel);
		mBtnSure.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_btn_sure:
			if (null != mDialogListener) {
				mDialogListener.onClick(getDialog(), Dialog.BUTTON_POSITIVE);
			}
			break;
		case R.id.dialog_btn_cancel:
			if (null != mDialogListener) {
				mDialogListener.onClick(getDialog(), Dialog.BUTTON_NEGATIVE);
			}
			break;
		}
	}

}
