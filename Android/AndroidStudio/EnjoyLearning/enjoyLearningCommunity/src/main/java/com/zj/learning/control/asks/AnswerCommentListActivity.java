package com.zj.learning.control.asks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListActivity;
import com.zj.learning.model.asks.Answer;
import com.zj.learning.model.asks.Comment;
import com.zj.learning.model.asks.Question;
import com.zj.learning.view.asks.AnswerItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

@SuppressLint("NewApi")
public class AnswerCommentListActivity extends BaseRefreshListActivity {

	private Answer mAnswer;
	private List<Comment> mComments;
	private EditText commentContentEt;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		this.setContentView(R.layout.asks_comment_list);
		mListView = (PullToRefreshListView) findViewById(R.id.asks_detail_listview);
		commentContentEt = (EditText) findViewById(R.id.asks_comment_content_et);
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		super.prepareDatas(savedInstanceState);
		mComments = new ArrayList<Comment>();
		mAdapter = new ItemSingleAdapter<AnswerItemView, Comment>(this,
				mComments);
		mListView.setAdapter(mAdapter);
		Intent intent = this.getIntent();
		mAnswer = Answer.toObj(intent.getStringExtra("answer"));
		if (null == mAnswer) {
			return;
		}
		// mAsksHelper.handleAvator(mIvAvator, mQuestion.getUserId());
		// mAsksHelper.handleThumb(mQuestion.getFileName(), mIvThumb, this);
		// 一切数据的源头
		this.fetchCacheDatas();
		// fetchDatas(false);
		// initData();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

	}

	@Override
	protected void fetchCacheDatas() {
		// Step 1：先看缓存
		this.showLoading();
		RequestTag tag = new RequestTag(mReqTag);
		this.getCacheManager().getCacheByUrl(this, tag,
				GlobalConfig.URL_ANSWER_COMMENT_LIST + mAnswer.getAnswerId());
	}

	@Override
	protected void handleSuccessDatas(List<?> result) {
		if (result != null && result.size() > 0) {
			Comment comment = ((List<Comment>) result).get(result.size() - 1);
			this.setTimestamp(comment.getAddTime());
			mComments.addAll((List<Comment>) result);
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
			posts.cacheUrl = GlobalConfig.URL_ANSWER_COMMENT_LIST
					+ mAnswer.getAnswerId();
		}
		params.put("max", "" + 8);
		params.put("answerId", mAnswer.getAnswerId() + "");
		this.getHttpApi().request(GlobalConfig.URL_ANSWER_COMMENT_LIST, params,
				this, posts);
	}

	@Override
	public void onResponse(RespOut arg0) {
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			if (arg0.param.operator == GlobalConfig.Operator.LIST_FETCH) {
				this.hideLoading();
				ArrayList<Comment> commentList = (ArrayList<Comment>) parser
						.getArray(Comment.class);
				RequestTag tag = arg0.param;
				// Step 2.2.2 缓存需要缓存的数据
				if (null != tag && !TextUtils.isEmpty(tag.cacheUrl)) {
					this.saveCache(tag.cacheUrl, arg0.resp);
				}
				this.handleFetchDataSuccess(commentList);
				if (commentList == null) {
					this.setEmptyView();
				}
			} else if (arg0.param.operator == GlobalConfig.Operator.OBJECT_ADD) {
				Comment comment = (Comment) parser.getOne(Comment.class);
				if (comment != null) {
					mComments.add(comment);
					mAdapter.notifyDataSetChanged();
				}
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
		List<Comment> comments = null;
		String resp = out.resp;
		if (!TextUtils.isEmpty(resp)) {// 缓存不为空
			RequestDataParser parser = new RequestDataParser(resp);
			comments = parser.getArray(Comment.class);
		}
		this.handleFetchCacheComplete(comments);
	}

	public void onCommentAddClick(View view) {
		String commentContent = commentContentEt.getText().toString().trim();
		if (commentContent.isEmpty()) {
			return;
		} else {
			HashMap<String, Object> commentMap = new HashMap<String, Object>();
			commentMap.put("answerId", mAnswer.getAnswerId());
			commentMap.put("message", commentContent);
			Map<String, String> params = new HashMap<String, String>();
			RequestTag posts = new RequestTag(mReqTag,
					GlobalConfig.Operator.OBJECT_ADD);
			params.put("userName", "test");
			params.put("content", Question.toJson(commentMap));
			this.getHttpApi().request(GlobalConfig.URL_ANSWER_COMMENT_ADD,
					params, this, posts);
		}
	}

	@Override
	public void onResponseError(RespOut out) {
		super.onResponseError(out);
	}

	@Override
	protected void clearDatas() {
		if (mComments != null) {
			mComments.clear();
		}
	}

	@Override
	protected boolean isDataEmpty() {
		return mComments == null ? true : false;
	}

}
