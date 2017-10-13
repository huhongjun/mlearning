package com.zj.learning.control.asks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.model.asks.Answer;
import com.zj.learning.model.asks.Question;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.SdcardUtil;

/**
 * 一个Activity,分别用作提交问题和回答问题
 */
@SuppressLint("NewApi")
public class AsksAddActivity extends BaseActivity implements OnDismissListener,
		OnFocusChangeListener {

	private int opreator = 0;
	private TextView titlebar_back_tv_title, titlebar_back_tv_right;
	private EditText asks_content_et, asks_add_memo_tv;
	private Answer mAnswer;
	private Question mQuestion;
	private boolean isAnswer = false;
	private PopupWindowHelper mImageHelper;
	private boolean isOpenPop;
	private static final int REQ_CODE_TAKE_PIC = 2;
	private static final int REQ_CODE_CHOOSE_PIC = 3;
	private static final int REQ_CODE_CROP_PIC = 4;
	private static final String TEMP_FILENAME = "temp.jpg";
	private static final int OUTPUT_X = 480;
	private static final int OUTPUT_Y = 320;
	private RelativeLayout asks_add_top_layout;
	private String mFilePath;
	private Uri uri = null, mImageUri = null;
	private ImageView asks_img_iv;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		setContentView(R.layout.asks_add);
		asks_add_top_layout = (RelativeLayout) findViewById(R.id.asks_add_top);
		titlebar_back_tv_title = (TextView) findViewById(R.id.titlebar_back_tv_title);
		titlebar_back_tv_right = (TextView) findViewById(R.id.titlebar_back_tv_right);
		titlebar_back_tv_right.setText(getString(R.string.asks_commit));
		titlebar_back_tv_right.setVisibility(View.VISIBLE);
		asks_content_et = (EditText) findViewById(R.id.asks_content_et);
		asks_img_iv = (ImageView) findViewById(R.id.asks_img_iv);
		LayoutInflater lay = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = lay.inflate(R.layout.asks_img_choice, null);
		mImageHelper = new PopupWindowHelper(this, v,
				R.style.popup_window_image_select_style);
		asks_content_et.setOnFocusChangeListener(this);
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		Intent intent = getIntent();
		if (intent != null) {
			String questionStr = intent.getStringExtra("question");
			mQuestion = Question.toObj(questionStr);
		}
		if (mQuestion != null) {
			titlebar_back_tv_title.setText(getString(R.string.asks_answer));
			isAnswer = true;
		} else {
			titlebar_back_tv_title.setText(getString(R.string.asks_add));
		}
		super.prepareDatas(savedInstanceState);
	}

	public void onClickRightBtn(View view) {
		String content = asks_content_et.getText().toString().trim();
		HashMap<String, Object> objMap = new HashMap<String, Object>();
		if (content.isEmpty()) {
			return;
		} else {
			String url;
			// 回答问题或提交问题 =>保存
			if (isAnswer) {
				objMap.put("questionId", mQuestion.getQuestionId());
				objMap.put("answerContent", content);
				objMap.put("anonymous", false);
				url = GlobalConfig.URL_ANSWER_ADD;
			} else {
				objMap.put("questionContent", content);
				objMap.put("anonymous", false);
				url = GlobalConfig.URL_QUESTION_ADD;
			}
			RequestTag postsTag = new RequestTag(mReqTag,
					GlobalConfig.Operator.OBJECT_ADD);
			Map<String, String> params = new HashMap<String, String>();
			params.put("content", Question.toJson(objMap));
			params.put("userName", "test");
			if (mFilePath != null) {
				File file = new File(mFilePath);
				if (file.exists()) {
					this.getUploadApi().uploadMIME(url, params, mFilePath,
							this, postsTag);
				} else {
					this.getUploadApi().uploadMIME(url, params, null, this,
							postsTag);
				}
			} else {
				this.getUploadApi().uploadMIME(url, params, null, this,
						postsTag);
			}

		}
	}

	public void onAddImageClick(View view) {
		isOpenPop = !isOpenPop;
		if (isOpenPop) {
			this.lostEtFocus(asks_content_et);
			mImageHelper.showAsDropDown(asks_add_top_layout);
			mImageHelper.setOnDimissListener(this);
		} else {
			mImageHelper.dismiss();
		}
	}

	public void onBtnTakePhotoClick(View view) {
		mImageHelper.dismiss();
		mImageUri = this.buildImageUri();
		if (null == mImageUri) {
			this.showToast(this.getString(R.string.no_sdcard));
			return;
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		this.startActivityForResult(intent, REQ_CODE_TAKE_PIC);
	}

	public void onBtnLocalImageClick(View view) {
		mImageHelper.dismiss();
		mImageUri = this.buildImageUri();
		if (null == mImageUri) {
			this.showToast(this.getString(R.string.no_sdcard));
			return;
		}
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
		intent.setType("image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 2);
		intent.putExtra("outputX", OUTPUT_X);
		intent.putExtra("outputY", OUTPUT_Y);
		intent.putExtra("scale", true);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", false); // no face detection
		startActivityForResult(intent, REQ_CODE_CHOOSE_PIC);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			mFilePath = "";
			return;
		}
		switch (requestCode) {
		case REQ_CODE_TAKE_PIC:
			this.cropImageUri(mImageUri, OUTPUT_X, OUTPUT_Y, REQ_CODE_CROP_PIC);
			break;
		case REQ_CODE_CHOOSE_PIC:
			if (mImageUri != null) {
				Bitmap bitmap = decodeUriAsBitmap(mImageUri);
				asks_img_iv.setImageBitmap(bitmap);
			}
			break;
		case REQ_CODE_CROP_PIC:
			if (mImageUri != null) {
				Bitmap bitmap = decodeUriAsBitmap(mImageUri);
				asks_img_iv.setImageBitmap(bitmap);
			}
			break;
		default:
			break;
		}
	}

	private Bitmap decodeUriAsBitmap(Uri uri) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(getContentResolver()
					.openInputStream(uri));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 调用系统Ativity - 图片处理,那边完成后自动回到这里并执行onActivityResult
	 * @param uri
	 * @param outputX
	 * @param outputY
	 * @param requestCode
	 */
	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 3);
		intent.putExtra("aspectY", 2);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	private Uri buildImageUri() {
		if (SdcardUtil.isSdcardExist()) {
			String path = SdcardUtil.getSdcardPath() + GlobalConfig.DIR_ROOT;
			if (!isExit(path)) {
				File file = new File(path);
				file.mkdirs();
			}
			File temp = new File(path, TEMP_FILENAME);
			mFilePath = temp.getPath();
			Uri uri = Uri.fromFile(temp);
			return uri;
		}
		return null;
	}

	public void onBtnCancleClick(View view) {
		mImageHelper.dismiss();
	}

	public void onClickEtContent(View view) {
		this.requestEtFocus(asks_content_et);
	}

	private void requestEtFocus(EditText et) {
		et.setFocusable(true);
		et.setFocusableInTouchMode(true);
		et.requestFocus();
	}

	private void lostEtFocus(EditText et) {
		et.setFocusable(false);
		et.setFocusableInTouchMode(false);
		et.clearFocus();
	}

	@Override
	public void onDismiss() {
		isOpenPop = false;
	}

	@Override
	public void onResponse(RespOut arg0) {
		super.onResponse(arg0);
		Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			if (arg0.param.operator == GlobalConfig.Operator.OBJECT_ADD) {
				if (isAnswer) {
					Answer answer = parser.getOne(Answer.class);
					Intent intent = new Intent();
					intent.putExtra("answer", answer.toJson());
					this.setResult(RESULT_OK, intent);
				} else {
					Question question = parser.getOne(Question.class);
					Intent intent = new Intent();
					intent.putExtra("question", question.toJson());
					this.setResult(RESULT_OK, intent);
				}
			}
			this.finish();
		}
	}

	@Override
	public void onResponseError(RespOut out) {
		super.onResponseError(out);
	}

	@Override
	public void onFocusChange(View arg0, boolean arg1) {

	}

	public void onBtnBackClick(View view) {
		this.finish();
	}

	private boolean isExit(String path) {
		if (path != null) {
			File file = new File(path);
			if (file.exists()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
