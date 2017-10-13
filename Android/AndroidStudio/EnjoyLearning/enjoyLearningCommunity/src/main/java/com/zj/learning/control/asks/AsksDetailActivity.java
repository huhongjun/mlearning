package com.zj.learning.control.asks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListActivity;
import com.zj.learning.model.asks.Answer;
import com.zj.learning.model.asks.Question;
import com.zj.learning.view.asks.AnswerItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.TimeUtil;
import com.zj.support.widget.ZjImageView;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

public class AsksDetailActivity extends BaseRefreshListActivity implements
		OnClickListener {

	private static final int REQ_CODE_ANSWER = 10;
	private static final int REQ_CODE_COMMENT = 11;
	private ZjImageView mIvAvator, mIvThumb;
	private View mHeaderView;
	private TextView mTvTitle, mTvUserName, mTvTime, mTvDesc, mTvAnswerCount;
	private Question mQuestion;
	private AsksHelper mAsksHelper;
	private List<Answer> mAnswers;
	/** 是否回答，用于更新ListView中回答数 **/
	private boolean mIsAnswer = false;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		this.setContentView(R.layout.asks_detail);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mHeaderView = this.getLayoutInflater().inflate(
				R.layout.asks_detail_header, null);
		mIvAvator = (ZjImageView) mHeaderView
				.findViewById(R.id.user_show_iv_avator);
		mIvThumb = (ZjImageView) mHeaderView
				.findViewById(R.id.asks_detail_header_iv_thumb);
		mTvTime = (TextView) mHeaderView.findViewById(R.id.user_show_tv_time);
		mTvUserName = (TextView) mHeaderView
				.findViewById(R.id.user_show_tv_name);
		mTvDesc = (TextView) mHeaderView
				.findViewById(R.id.asks_detail_header_tv_desc);
		mTvAnswerCount = (TextView) mHeaderView
				.findViewById(R.id.asks_detail_header_tv_answer_count);
		mListView = (PullToRefreshListView) this
				.findViewById(R.id.asks_detail_listview);
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		super.prepareDatas(savedInstanceState);
		this.addHeaderView(mHeaderView);
		mAsksHelper = new AsksHelper();
		mAnswers = new ArrayList<Answer>();
		mAdapter = new ItemSingleAdapter<AnswerItemView, Answer>(this, mAnswers);
		((ItemSingleAdapter<AnswerItemView, Answer>) mAdapter).addParams(
				"asksHelper", mAsksHelper);
		mListView.setAdapter(mAdapter);
		Intent intent = this.getIntent();
		mQuestion = Question.toObj(intent.getStringExtra("asks"));
		if (null == mQuestion) {
			return;
		}
		// mAsksHelper.handleAvator(mIvAvator, mQuestion.getUserId());
		mAsksHelper.handleThumb(mQuestion.getFileName(), mIvThumb, this);
		mTvTitle.setText(mQuestion.getUserName()
				+ this.getString(R.string.asks_whos_question));
		mTvUserName.setText(mQuestion.getUserName());
		mTvDesc.setText(mQuestion.getQuestionContent());
		mTvTime.setText(TimeUtil.formatDate(
				TimeUtil.getDateBySeconds(mQuestion.getAddTime()),
				TimeUtil.DATE_FORMAT_NO_YEARANDSECOND));
		mTvAnswerCount.setText(mAsksHelper.getAnswerCountTextSpan(this,
				mQuestion.getAnswerCount()));
		// 一切数据的源头
		this.fetchCacheDatas();
		// fetchDatas(false);
		// initData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position < 2) {
			return;
		}
		Intent intent = new Intent(this, AnswerCommentListActivity.class);
		Answer answer = mAnswers.get(position - 2);
		intent.putExtra("answer", answer.toJson());
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.asks_detail_header_iv_thumb:// 问题缩略图
			mAsksHelper.onClickThumb(v);
			break;
		}
	}

	@Override
	protected void fetchCacheDatas() {
		// Step 1：先看缓存
		this.showLoading();
		RequestTag tag = new RequestTag(mReqTag);
		this.getCacheManager().getCacheByUrl(this, tag,
				GlobalConfig.URL_ANSWER_LIST + mQuestion.getQuestionId());
	}

	@Override
	protected void handleSuccessDatas(List<?> result) {
		if (result != null && result.size() > 0) {
			Answer quest = ((List<Answer>) result).get(result.size() - 1);
			this.setTimestamp(quest.getAddTime());
			mAnswers.addAll((List<Answer>) result);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		Map<String, String> params = new HashMap<String, String>();
		if (!needCache) {
			params.put("addTime", this.getTimestamp() + "");
		}
		RequestTag posts = new RequestTag(mReqTag,
				GlobalConfig.Operator.LIST_FETCH);
		if (needCache) {
			posts.cacheUrl = GlobalConfig.URL_ANSWER_LIST
					+ mQuestion.getQuestionId();
		}
		params.put("max", "" + 8);
		params.put("questionId", mQuestion.getQuestionId() + "");
		this.getHttpApi().request(GlobalConfig.URL_ANSWER_LIST, params, this,
				posts);
	}

	@Override
	public void onResponse(RespOut arg0) {
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			if (arg0.param.operator == GlobalConfig.Operator.LIST_FETCH) {
				this.hideLoading();
				ArrayList<Answer> answerList = (ArrayList<Answer>) parser
						.getArray(Answer.class);
				RequestTag tag = arg0.param;
				// Step 2.2.2 缓存需要缓存的数据
				if (null != tag && !TextUtils.isEmpty(tag.cacheUrl)) {
					this.saveCache(tag.cacheUrl, arg0.resp);
				}
				this.handleFetchDataSuccess(answerList);
			}
		} else {
			this.handleFetchDataFail();
		}
		super.onResponse(arg0);
	}

	@Override
	public void onResponseCache(RespOut out) {
		super.onResponseCache(out);// Step 1.1：加载缓存完成（不论成功与否、有无缓存）
		if (null == out) {
			return;
		}
		List<Answer> answers = null;
		String resp = out.resp;
		if (!TextUtils.isEmpty(resp)) {// 缓存不为空
			RequestDataParser parser = new RequestDataParser(resp);
			answers = parser.getArray(Answer.class);
		}
		this.handleFetchCacheComplete(answers);
	}

	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseError(out);
	}

	@Override
	protected boolean isDataEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void clearDatas() {
		if (mAnswers != null) {
			mAnswers.clear();
		}
	}

	public void onClickAnswerMe(View view) {
		Intent intent = new Intent(this, AsksAddActivity.class);
		intent.putExtra("question", mQuestion.toJson());
		this.startActivityForResult(intent, REQ_CODE_ANSWER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		String json = data.getStringExtra("answer");
		if (TextUtils.isEmpty(json)) {
			return;
		}
		Answer answer = Answer.toObj(json);
		if (REQ_CODE_ANSWER == requestCode) {
			mIsAnswer = true;
			// 回答数加1
			int temp = mQuestion.getAnswerCount();
			temp += 1;
			mQuestion.setAnswerCount(temp);
			mTvAnswerCount.setText(this.getString(R.string.asks_reply_couts)
					+ "(" + temp + ")");
			if (null == mAnswers) {
				return;
			}
			List<Answer> tempList = new ArrayList<Answer>();
			tempList.addAll(mAnswers);
			mAnswers.clear();
			mAnswers.add(answer);
			mAnswers.addAll(tempList);
			this.notifyDataChanged();
		} else if (REQ_CODE_COMMENT == requestCode) {// 刷新评论数
			Answer temp = this.getAnswerById(answer.getAnswerId());
			if (null == temp) {
				return;
			}
			temp.setCommentCount(answer.getCommentCount());
			this.notifyDataChanged();
		}
	}

	private Answer getAnswerById(int aid) {
		if (null == mAnswers || mAnswers.isEmpty()) {
			return null;
		}
		for (Answer answer : mAnswers) {
			if (aid == answer.getAnswerId()) {
				return answer;
			}
		}
		return null;
	}

	@Override
	public void onSwipeFinish() {
		this.setResultBack();
		super.onSwipeFinish();
	}

	protected void setResultBack() {
		if (!mIsAnswer || null == mQuestion) {
			this.setResult(RESULT_CANCELED);
			return;
		}
		Intent intent = new Intent();
		intent.putExtra("question", mQuestion.toJson());
		this.setResult(RESULT_OK, intent);
	}
}
