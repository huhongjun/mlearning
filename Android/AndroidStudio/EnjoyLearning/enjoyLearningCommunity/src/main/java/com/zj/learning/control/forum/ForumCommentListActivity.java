package com.zj.learning.control.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseRefreshListActivity;
import com.zj.learning.model.forum.Comment;
import com.zj.learning.model.forum.Posts;
import com.zj.learning.view.forum.CommentItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

public class ForumCommentListActivity extends BaseRefreshListActivity {

	private List<Comment> commentList;
	private TextView titlebar_back_tv_title;
	private Posts posts;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		setContentView(R.layout.forum_comment_list);
		titlebar_back_tv_title = (TextView) findViewById(R.id.titlebar_back_tv_title);
		commentList = new ArrayList<Comment>();
		mAdapter = new ItemSingleAdapter<CommentItemView, Comment>(this,
				commentList);
		mListView = (PullToRefreshListView) this
				.findViewById(R.id.forum_comment_list_lv);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		super.prepareDatas(savedInstanceState);
		posts = Posts.toObj(getIntent().getStringExtra("posts"));
		if (posts != null) {
			titlebar_back_tv_title.setText(posts.getAuthor());
			fetchDatas(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	protected void fetchCacheDatas() {

	}

	@Override
	protected void fetchDatas(boolean needCache) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tid", "" + posts.getTid());
		params.put("max", "" + 15);
		if (!needCache) {
			params.put("timestamp", "" + this.getTimestamp());
		}
		RequestTag tag = new RequestTag(mReqTag,
				GlobalConfig.Operator.LIST_FETCH);
		this.getHttpApi().request(GlobalConfig.URL_FORUM_Comment_GET, params,
				this, tag);
	}

	@Override
	public void onResponse(RespOut arg0) {
		super.onResponse(arg0);
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			List<Comment> list = parser.getArray(Comment.class);
			if (list != null && list.size() > 0) {
				this.handleSuccessDatas(list);
			}
			this.handleFetchDataSuccess(list);
		}
	}

	@Override
	protected void handleSuccessDatas(List<?> result) {
		List<Comment> temList = (List<Comment>) result;
		Comment comment = (Comment) result.get(temList.size() - 1);
		this.setTimestamp(comment.getDateline());
		commentList.addAll(temList);
		mAdapter.notifyDataSetChanged();
	}

	@Override
	public void onResponseError(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseError(out);
	}

	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseCache(out);
	}

	@Override
	public void onBtnBackClick(View view) {
		super.onBtnBackClick(view);
	}

	@Override
	protected boolean isDataEmpty() {
		return false;
	}

	@Override
	protected void clearDatas() {

	}

}
