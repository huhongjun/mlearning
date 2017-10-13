package com.zj.learning.control.course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.CoreApp;
import com.zj.learning.CustomDialog;
import com.zj.learning.LoadingHelper;
import com.zj.learning.R;
import com.zj.learning.ResourceHouseKeeper;
import com.zj.learning.ResourcePathHelper;
import com.zj.learning.TimerHelper;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.adapter.TabFragmentAdapter;
import com.zj.learning.control.common.base.BaseFragmentActivity;
import com.zj.learning.control.course.exam.AnswerFragment;
import com.zj.learning.control.course.exam.AnswerResultActivity;
import com.zj.learning.control.course.exam.BaseExamFragment;
import com.zj.learning.control.course.exam.CloseExamFragment;
import com.zj.learning.control.course.exam.MulitpleChoiceExamFragment;
import com.zj.learning.control.course.exam.ShortAnswerExamFragment;
import com.zj.learning.control.course.exam.SingleChoiceExamFragment;
import com.zj.learning.model.course.AnswerCardItem;
import com.zj.learning.model.course.Course;
import com.zj.learning.model.course.Question;
import com.zj.learning.model.course.Resource;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.CommonUtil;
import com.zj.support.util.TimeUtil;

/**
 * 考试/作业界面
 * 
 * @author XingRongJing
 * 
 */
public class ResourceDetailActivity extends BaseFragmentActivity implements
		OnPageChangeListener, DialogInterface.OnClickListener, Handler.Callback {

	private static final int MSG_SCROLL_NEXT = 1;
	private ViewPager mViewPager;
	private TabFragmentAdapter mFragmentAdapter;
	private TextView mTvTitle, mTvAnswerCard;
	private RelativeLayout mRlEmpty;
	private LinearLayout mLlCommitLoading;
	private List<Fragment> mFragments;
	private List<Question> mQuestions;
	private Course mCourse;
	private Resource mResource;
	/** 答题卡选项 **/
	private ArrayList<AnswerCardItem> mAnswerItems;
	private long mBeginTime = 0;
	/** 用户总得分、试题总分 **/
	private float mScore = 0.0f, mTotalScore = 0.0f;
	/** 答对题数 **/
	private int mRightCount = 0;
	private LoadingHelper mCommitLoadingHelper;
	private TimerHelper mTimerHelper;
	private Handler mHandler;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_question);
		mViewPager = (ViewPager) this.findViewById(R.id.course_exam_viewpager);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvAnswerCard = (TextView) this
				.findViewById(R.id.titlebar_back_tv_right);
		mRlEmpty = (RelativeLayout) this
				.findViewById(R.id.course_question_rl_empty);
		LinearLayout mLlLoading = (LinearLayout) this
				.findViewById(R.id.course_question_loading);
		mLlCommitLoading = (LinearLayout) this
				.findViewById(R.id.course_question_ll_commit_loading);
		this.buildLoading(mLlLoading);
		mCommitLoadingHelper = new LoadingHelper(this, mLlCommitLoading);
		mViewPager.setOnPageChangeListener(this);
		mTvAnswerCard.setVisibility(View.VISIBLE);
		mTvAnswerCard.setText(this.getString(R.string.course_exam_answer_card));
		mTvTitle.setText(this.getString(R.string.course_video_time));
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		mBeginTime = TimeUtil.getCurrentTimeBySeconds();
		String res = "", course = "";
		if (null != savedInstanceState) {
			res = savedInstanceState.getString("resource");
			course = savedInstanceState.getString("course");

		} else {
			Intent intent = this.getIntent();
			res = intent.getStringExtra("resource");
			course = intent.getStringExtra("course");
		}
		mResource = Resource.toObj(res);
		mCourse = Course.toObj(course);
		mFragments = new ArrayList<Fragment>();
		mQuestions = new ArrayList<Question>();
		mAnswerItems = new ArrayList<AnswerCardItem>();

		mFragmentAdapter = new TabFragmentAdapter(
				this.getSupportFragmentManager(), mFragments);
		mViewPager.setAdapter(mFragmentAdapter);
		mTimerHelper = new TimerHelper(mTvTitle);
		mHandler = new Handler(this);

		// Step 1：获取数据
		this.fetchDatas();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		if (null != outState && null != mResource) {
			outState.putString("resource", mResource.toJson());
			outState.putString("course", mCourse.toJson());
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		this.setQustionsCount(arg0);
		this.notifyAnswerCardStateChanged(arg0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseCache(out);
		// Step 2.1：获取本地xml数据成功
		List<Object> temps = (List<Object>) out.result;
		this.handleDataSuccess(temps);
		this.hideLoading();
		if (this.isDataEmpty()) {
			this.showDataEmpty();
		}else{
			mTimerHelper.startTimer();
		}
	}

	@Override
	public void onResponse(RespOut arg0) {
		// TODO Auto-generated method stub
		super.onResponse(arg0);
		if (null == arg0 || null == arg0.param) {
			return;
		}
		int opeartor = arg0.param.operator;
		switch (opeartor) {
		case GlobalConfig.Operator.COURSE_EXAM_QUESTIONS_FETCH:
			// Step 2.1：获取远程数据成功
			RequestDataParser parser = new RequestDataParser(arg0.resp);
			if (parser.isSuccess()) {
				List<Question> questions = parser.getArray(Question.class);
				this.handleDataSuccess(questions);
				this.hideLoading();
				mTimerHelper.startTimer();
			} else {
				this.showRetry();
			}
			break;
		case GlobalConfig.Operator.COURSE_EXAM_QUESTIONS_COMMIT:// 提交成功
			this.hideCommitLoading();
			if (null != mTimerHelper) {
				mTimerHelper.stopTimer();
			}
			this.showToast(this.getString(R.string.course_exam_commit_success));
			this.startAnswerResult();
			this.finish();
			break;
		}
	}

	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		if (null == out || null == out.param) {
			return;
		}
		int opeartor = out.param.operator;
		switch (opeartor) {
		case GlobalConfig.Operator.COURSE_EXAM_QUESTIONS_FETCH:// 获取数据失败
			super.onResponseError(out);
			break;
		case GlobalConfig.Operator.COURSE_EXAM_QUESTIONS_COMMIT:// 提交失败
			this.showToast(this.getString(R.string.request_fail));
			this.hideCommitLoading();
			break;
		}
	}

	@SuppressWarnings("unchecked")
	private void handleDataSuccess(List<?> questions) {
		if (null != mQuestions && null != questions && !questions.isEmpty()) {
			mQuestions.addAll((List<Question>) questions);
		} else {
			this.showDataEmpty();
		}
		this.prepareFragments();
	}

	@Override
	protected void setResultBack() {
		// TODO Auto-generated method stub
		super.setResultBack();
		if (null != mTimerHelper) {
			mTimerHelper.stopTimer();
		}
	}

	private boolean isDataEmpty() {
		return null == mQuestions ? true : mQuestions.isEmpty();
	}

	private void showDataEmpty() {
		if (null != mRlEmpty) {
			mRlEmpty.setVisibility(View.VISIBLE);
		}
		if (null != mViewPager) {
			mViewPager.setVisibility(View.GONE);
		}
	}

	private void showCommitLoading() {
		if (null != mCommitLoadingHelper) {
			mCommitLoadingHelper.showLoading();
		}
		mLlCommitLoading.requestFocus();
		mLlCommitLoading.setFocusable(true);
	}

	private void hideCommitLoading() {
		if (null != mCommitLoadingHelper) {
			mCommitLoadingHelper.hideLoading();
		}
		mLlCommitLoading.clearFocus();
		mLlCommitLoading.setFocusable(false);
	}

	private void prepareFragments() {
		if (null == mQuestions) {
			return;
		}

		int size = mQuestions.size();
		for (int i = 0; i < size; i++) {
			Question exam = mQuestions.get(i);
			int type = exam.getType();
			BaseExamFragment fragment = null;
			switch (type) {
			case Question.TYPE_SINGLE_CHOICE:
			case Question.TYPE_JUDGE:
				fragment = new SingleChoiceExamFragment();
				break;
			case Question.TYPE_MULTIPLE_CHOICE:
				fragment = new MulitpleChoiceExamFragment();
				break;
			case Question.TYPE_SHORT_ANSWER:
				fragment = new ShortAnswerExamFragment();
				break;
			case Question.TYPE_CLOSE:
				fragment = new CloseExamFragment();
				break;
			}
			if (null != fragment) {
				Bundle meArgs = new Bundle();
				meArgs.putString("examTitle", mResource.getResName());
				meArgs.putInt("index", i);
				meArgs.putString("examItem", exam.toJson());
				fragment.setArguments(meArgs);
				this.adddFragment(fragment);

				// 初始化答题卡Item
				AnswerCardItem counts = new AnswerCardItem();
				counts.setQuestionId(exam.getId());
				counts.setIndex(i);
				counts.setQuestionType(exam.getType());
				counts.setTotalAnswersCount(exam.getAnswersCount());
				mAnswerItems.add(counts);
			}
		}
		Fragment answer = new AnswerFragment();
		Bundle meArgs = new Bundle();
		meArgs.putString("examTitle", mResource.getResName());
		meArgs.putParcelableArrayList("answerCounts", mAnswerItems);
		answer.setArguments(meArgs);
		this.adddFragment(answer);
		this.notifyDataChanged();
		this.setQustionsCount(0);
	}

	private ResourceHouseKeeper getResourceHouseKeeper() {
		return ((CoreApp) this.getApplication()).getResourceHouseKeeper();
	}

	private void fetchDatas() {
		if (mCourse.isOnline()) {// 远程
			this.fetchDatasFromServer(mResource.getResFileName());
		} else {// 本地
			this.fetchDatasFromXml(mResource.getResType(),
					mResource.getResFileName());
		}
	}

	private void fetchDatasFromXml(int resType, String resFileName) {
		String path = this.getPathByType(resType, resFileName);
		// 本地已有此文件，直接解析
		if (ResourcePathHelper.isFileExist(path)) {
			this.showLoading();
			this.getResourceHouseKeeper().fetchQuestions(path, this,
					new RequestTag(mReqTag));
		} else {
			this.showToast(this.getString(R.string.course_offline_res_not_find));
			this.showDataEmpty();
		}
	}

	private void fetchDatasFromServer(String resFileName) {
		this.showLoading();
		Map<String, String> params = new HashMap<String, String>();
		params.put("resFileName", resFileName);
		this.getHttpApi().request(
				GlobalConfig.URL_COURSE_EXAM_LIST_DETAIL,
				params,
				this,
				new RequestTag(mReqTag,
						GlobalConfig.Operator.COURSE_EXAM_QUESTIONS_FETCH));
	}

	private void notifyDataChanged() {
		if (null == mFragmentAdapter) {
			return;
		}
		mFragmentAdapter.notifyDataSetChanged();
	}

	private void adddFragment(Fragment fragment) {
		if (null == mFragments) {
			return;
		}
		mFragments.add(fragment);
	}

	private Fragment getFragmentByIndex(int index) {
		return null == mFragments ? null : mFragments.get(index);
	}

	private void setQustionsCount(int index) {
		int mCurrPage = (index + 1);
		try {
			BaseExamFragment fragment = (BaseExamFragment) this
					.getFragmentByIndex(index);
			fragment.setExamCount(mCurrPage + "/" + mQuestions.size() + "");
		} catch (Exception e) {
		}
	}

	/**
	 * 通知改变答题卡状态
	 * 
	 * @param index
	 */
	private void notifyAnswerCardStateChanged(int index) {
		try {
			int lastIndex = mFragmentAdapter.getCount() - 1;
			// 如果选中答题卡页时，刷新答题卡状态
			if (index == lastIndex) {
				AnswerFragment answerCard = (AnswerFragment) this
						.getFragmentByIndex(lastIndex);
				answerCard.notifyDataChanged();
			}
		} catch (Exception e) {
		}
	}

	private String getPathByType(int type, String fileName) {
		if (Resource.TYPE_EXAM == type) {
			return ResourcePathHelper.getExamsPath(fileName);
		} else if (Resource.TYPE_HOMEWORK == type) {
			return ResourcePathHelper.getHomeworksPath(fileName);
		}
		return "";
	}

	/**
	 * 设置每题答案
	 * 
	 * @param index
	 * @param answers
	 */
	public void setAnswers(int index, List<String> answers) {
		try {
			AnswerCardItem count = mAnswerItems.get(index);
			count.setAnswers(answers);
		} catch (Exception e) {
		}

	}

	public void scrollPage(int index) {
		if (null != mViewPager) {
			mViewPager.setCurrentItem(index);
		}
	}

	public void scrollNextPage(int index) {
		if (mHandler.hasMessages(MSG_SCROLL_NEXT)) {
			mHandler.removeMessages(MSG_SCROLL_NEXT);
		}
		Message msg = mHandler.obtainMessage(MSG_SCROLL_NEXT, index, 0);
		mHandler.sendMessageDelayed(msg, 500);
	}

	public void onClickReload(View view) {// 没网或失败时，重新加载
		// Step 2：手动获取数据
		this.hideRetry();
		this.fetchDatas();
	}

	public void onClickRightBtn(View view) {// 答题卡
		if (null == mFragmentAdapter) {
			return;
		}
		this.scrollPage(mFragmentAdapter.getCount() - 1);
	}

	public void onClickAnswerCount(View view) {// 点击答题卡某项时
		AnswerCardItem card = (AnswerCardItem) view.getTag();
		this.scrollPage(card.getIndex());
	}

	public void onClickCommit(View view) {
		if (this.isAnswerAll()) {
			this.onCommit();
			return;
		}
		CustomDialog fd = CustomDialog.newInstance("",
				this.getString(R.string.course_exam_commit_dialog_tips), this);
		fd.show(this.getSupportFragmentManager(), "CustomDialog");

	}

	private void reset() {
		mScore = 0;
		mTotalScore = 0;
		mRightCount = 0;
	}

	private boolean isAnswerAll() {
		if (null == mAnswerItems) {
			return false;
		}
		int size = mAnswerItems.size();
		for (int i = 0; i < size; i++) {
			AnswerCardItem item = mAnswerItems.get(i);
			if (!item.isAnswerAll()) {
				return false;
			}
		}
		return true;
	}

	private void onCommit() {
		this.showCommitLoading();
		this.reset();
		Map<String, String> params = new HashMap<String, String>();
		params.put("examContent", this.buildExamContent());
		params.put("examInfo", this.buildExamInfo(mResource));
		this.getHttpApi().request(
				GlobalConfig.URL_COURSE_EXAM_COMMIT,
				params,
				this,
				new RequestTag(mReqTag,
						GlobalConfig.Operator.COURSE_EXAM_QUESTIONS_COMMIT));
	}

	private String buildExamInfo(Resource res) {
		JSONObject json = new JSONObject();
		try {
			json.put("eqId", res.getId());
			json.put("eqName", res.getResName());
			json.put("eqType", res.getResType());
			json.put("eqBtime", mBeginTime);
			json.put("eqEtime", TimeUtil.getCurrentTimeBySeconds());
			json.put("userId", this.getUser().getUserId());
			json.put("userName", this.getUser().getLoginName());
			json.put("totalScore", mScore);
		} catch (Exception e) {
		}
		return json.toString();
	}

	private String buildExamContent() {
		JSONArray jsonArr = new JSONArray();
		try {
			int size = mAnswerItems.size();
			for (int i = 0; i < size; i++) {
				JSONObject json = new JSONObject();
				Question question = mQuestions.get(i);
				AnswerCardItem item = mAnswerItems.get(i);
				List<String> answers = item.getAnswers();
				json.put("title", question.getTitle());
				json.put("type", question.getType());
				json.put("answer", this.buildAnswers(answers));
				boolean isRight = CommonUtil.isEqualsIgnoreCaseAndOrder(
						question.getAnswers(), answers);
				item.setRight(isRight);
				float score = question.getScore();
				mTotalScore += score;
				if (isRight) {
					json.put("score", score);
					mScore += score;
					mRightCount++;
				} else {
					json.put("score", 0);
				}
				jsonArr.put(json);
			}
		} catch (Exception e) {
		}
		return jsonArr.toString();
	}

	private String buildAnswers(List<String> answers) {
		if (null == answers || answers.isEmpty()) {
			return "";
		}
		JSONArray jsonArr = new JSONArray();
		for (String answer : answers) {
			jsonArr.put(answer);
		}
		return jsonArr.toString();
	}

	private void startAnswerResult() {
		Intent intent = new Intent(this, AnswerResultActivity.class);
		intent.putExtra("resource", mResource.toJson());
		intent.putExtra("score", mScore);
		intent.putExtra("totalScore", mTotalScore);
		intent.putExtra("rightCount", mRightCount);
		intent.putExtra("totalCount", mQuestions.size());
		intent.putExtra("time", mTimerHelper.getDuration());
		intent.putParcelableArrayListExtra("answerCounts", mAnswerItems);
		this.startActivity(intent);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		switch (which) {
		case DialogInterface.BUTTON_POSITIVE:// 确定提交
			this.onCommit();
			break;
		}
		if (null != dialog) {
			dialog.dismiss();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		try {
			int index = msg.arg1;
			index++;
			scrollPage(index);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
