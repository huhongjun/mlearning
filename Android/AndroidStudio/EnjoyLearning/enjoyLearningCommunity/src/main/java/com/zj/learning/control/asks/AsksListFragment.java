package com.zj.learning.control.asks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListFragment;
import com.zj.learning.model.asks.Answer;
import com.zj.learning.model.asks.Question;
import com.zj.learning.model.course.Course;
import com.zj.learning.model.forum.Posts;
import com.zj.learning.model.forum.Tag;
import com.zj.learning.view.asks.QuestionItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 全部问题/常见问题界面/搜索问题
 * 
 * @author wliting
 * 
 */
public class AsksListFragment extends BaseRefreshListFragment {

	private List<Question> mQuestions;
	private int mOperatorType = 1;
	private AsksHelper mAsksHelper;
	/** 提交问题ResultCode **/
	private static final int REQ_CODE_ADD_ASKS = 3;
	/** 直接回答问题ResultCode **/
	public static final int REQ_CODE_ANSWER_ASKS = 4;
	/** 详情回答问题ResultCode **/
	public static final int REQ_CODE_DETAIL_ANSWER_ASKS = 5;

	@Override
	public void setArguments(Bundle args) {
		super.setArguments(args);
		mOperatorType = args.getInt("operatorType");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mQuestions = new ArrayList<Question>();
		mAdapter = new ItemSingleAdapter<QuestionItemView, Question>(
				this.getActivity(), mQuestions);
		mAsksHelper = new AsksHelper();
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.asks_list, null);
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.asks_list_listview);
		mListView.setAdapter(mAdapter);
		((ItemSingleAdapter<QuestionItemView, Question>) mAdapter).addParams(
				"fragment", this);
		((ItemSingleAdapter<QuestionItemView, Question>) mAdapter).addParams(
				"asksHelper", mAsksHelper);
		mListView.setOnItemClickListener(this);
		this.prepareByView();
		// 如果还未加载过
		if (!this.isLoaded()) {
			// Step 1:加载缓存
			this.fetchCacheDatas();
			this.setIsLoaded(true);
		} else if (this.isDataEmpty()) {
			if (this.isNetworkEnable() && !this.isReqFails()) {
				this.setEmptyView();
			} else {
				this.setNoNetworkView();
			}
		}
		return view;
	}

	/**
	 * 选中此页时，数据初始化之源
	 */
	public void onSelected() {
		// 如果是还未加载
		if (!this.isLoaded()) {
			this.prepareByView();
			this.fetchCacheDatas();
			this.setIsLoaded(true);
		} else if (this.isDataEmpty()) {
			if (this.isNetworkEnable() && !this.isReqFails()) {
				this.setEmptyView();
			} else {
				this.setNoNetworkView();
			}
		} else {
			this.prepareByView();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Question question = (Question) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this.getActivity(), AsksDetailActivity.class);
		intent.putExtra("asks", question.toJson());
		this.startActivityForResult(intent, REQ_CODE_DETAIL_ANSWER_ASKS);
	}

	@Override
	protected void fetchCacheDatas() {
		// Step 1：先看缓存
		this.showLoading();
		RequestTag tag = new RequestTag(mReqTag);
		this.getCacheManager().getCacheByUrl(this, tag,
				GlobalConfig.URL_QUESTION_LIST + mOperatorType);
	}

	@Override
	protected void handleSuccessDatas(List<?> result) {
		if (result != null && result.size() > 0) {
			Question quest = ((List<Question>) result).get(result.size() - 1);
			this.setTimestamp(quest.getAddTime());
			mQuestions.addAll((List<Question>) result);
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		RequestTag posts = new RequestTag(mReqTag,
				GlobalConfig.Operator.LIST_FETCH);
		if (needCache) {
			posts.cacheUrl = GlobalConfig.URL_QUESTION_LIST + mOperatorType;
		}
		Map<String, String> params = new HashMap<String, String>();
		if (!needCache) {
			params.put("addTime", this.getTimestamp() + "");
		}
		if (mOperatorType == Question.OPERATOR_COMMON) {
			params.put("categoryId", mOperatorType + "");
		}
		params.put("max", "" + 8);
		this.getHttpApi().request(GlobalConfig.URL_QUESTION_LIST, params, this,
				posts);
	}

	@Override
	public void onResponse(RespOut out) {
		RequestDataParser parser = new RequestDataParser(out.resp);
		if (parser.isSuccess()) {
			if (out.param.operator == GlobalConfig.Operator.LIST_FETCH) {
				ArrayList<Question> questionList = (ArrayList<Question>) parser
						.getArray(Question.class);
				RequestTag tag = out.param;
				// Step 2.2.2 缓存需要缓存的数据
				if (null != tag && !TextUtils.isEmpty(tag.cacheUrl)) {
					this.saveCache(tag.cacheUrl, out.resp);
				}
				if (questionList != null) {
					this.handleFetchDataSuccess(questionList);
				} else {
					this.setEmptyView();
				}

			}
		} else {
			this.handleFetchDataFail();
		}
		super.onResponse(out);
	}

	@Override
	public void onResponseCache(RespOut out) {
		super.onResponseCache(out);
		// Step 1.1：加载缓存完成（不论成功与否、有无缓存）
		if (null == out) {
			return;
		}
		List<Question> questions = null;
		String resp = out.resp;
		if (!TextUtils.isEmpty(resp)) {// 缓存不为空
			RequestDataParser parser = new RequestDataParser(resp);
			questions = parser.getArray(Question.class);
		}
		this.handleFetchCacheComplete(questions);
	}

	@Override
	public void onResponseError(RespOut out) {
		super.onResponseError(out);
		this.setNoNetworkView();

	}

	@Override
	protected boolean isDataEmpty() {
		return (null == mQuestions ? false : mQuestions.isEmpty());
	}

	@Override
	protected void clearDatas() {
		if (mQuestions != null) {
			mQuestions.clear();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == this.getActivity().RESULT_OK) {
			if (requestCode == REQ_CODE_ADD_ASKS) {
				String json = data.getStringExtra("question");
				if (TextUtils.isEmpty(json)) {
					return;
				}
				Question question = Question.toObj(json);
				mQuestions.add(question);
				mAdapter.notifyDataSetChanged();
			}
		} else {
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void onClickRightBtn(View v) {
		Intent intent = new Intent(getActivity(), AsksAddActivity.class);
		this.startActivityForResult(intent, REQ_CODE_ADD_ASKS);
	}

}
