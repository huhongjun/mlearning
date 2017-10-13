package com.zj.learning.control.asks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListActivity;
import com.zj.learning.model.asks.Question;
import com.zj.learning.view.asks.QuestionItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

@SuppressLint("NewApi")
public class AsksFindActivity extends BaseRefreshListActivity {

	private EditText titlebar_find_et;
	private List<Question> mQuestions;
	private AsksHelper mAsksHelper;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		setContentView(R.layout.asks_list);
		mQuestions = new ArrayList<Question>();
		mAdapter = new ItemSingleAdapter<QuestionItemView, Question>(this,
				mQuestions);
		mAsksHelper = new AsksHelper();
		RelativeLayout asks_list_find_l = (RelativeLayout) findViewById(R.id.asks_list_find_l);
		asks_list_find_l.setVisibility(View.VISIBLE);
		titlebar_find_et = (EditText) findViewById(R.id.titlebar_search_et);
		mListView = (PullToRefreshListView) findViewById(R.id.asks_list_listview);
		mListView.setAdapter(mAdapter);
		((ItemSingleAdapter<QuestionItemView, Question>) mAdapter).addParams(
				"asksHelper", mAsksHelper);
		mListView.setOnItemClickListener(this);
	}

	public void onClicksearchBtn(View view) {
		if (!titlebar_find_et.getText().toString().trim().isEmpty()) {
			if (mQuestions != null) {
				clearDatas();
			}
			fetchDatas(false);
		} else {
			Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Question question = (Question) mQuestions.get(arg2 - 1);
		Intent intent = new Intent(this, AsksDetailActivity.class);
		intent.putExtra("asks", question.toJson());
		this.startActivity(intent);

	}

	@Override
	protected void fetchDatas(boolean needCache) {
		RequestTag posts = new RequestTag(mReqTag,
				GlobalConfig.Operator.LIST_FETCH);
		Map<String, String> params = new HashMap<String, String>();
		if (!needCache) {
			params.put("addTime", this.getTimestamp() + "");
		}
		params.put("max", "" + 8);
		params.put("questionContent", titlebar_find_et.getText().toString()
				.trim());
		this.getHttpApi().request(GlobalConfig.URL_QUESTION_LIST, params, this,
				posts);
	}

	@Override
	public void onResponse(RespOut arg0) {
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			if (arg0.param.operator == GlobalConfig.Operator.LIST_FETCH) {
				ArrayList<Question> questionList = (ArrayList<Question>) parser
						.getArray(Question.class);
				if (questionList != null) {
					this.handleFetchDataSuccess(questionList);
				}
			}
		} else {
			this.handleFetchDataFail();
		}
		super.onResponse(arg0);
	}

	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseError(out);
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
	protected boolean isDataEmpty() {
		return false;
	}

	@Override
	protected void clearDatas() {
		if (mQuestions != null) {
			mQuestions.clear();
		}
	}

	@Override
	protected void fetchCacheDatas() {
		// TODO Auto-generated method stub

	}

}
