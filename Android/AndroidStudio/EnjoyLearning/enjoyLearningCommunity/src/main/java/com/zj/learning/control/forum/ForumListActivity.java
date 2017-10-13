package com.zj.learning.control.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.LoginActivity;
import com.zj.learning.control.common.base.BaseRefreshListActivity;
import com.zj.learning.model.forum.Posts;
import com.zj.learning.model.forum.Tag;
import com.zj.learning.view.forum.PostsItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

public class ForumListActivity extends BaseRefreshListActivity {
	private Tag tag;
	TextView titlebar_back_tv_title, titlebar_back_tv_right;
	private ArrayList<Posts> mPosts;
	private static final int REQ_CODE_LOGIN = 1;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		this.setContentView(R.layout.forum_list);
		titlebar_back_tv_title = (TextView) findViewById(R.id.titlebar_back_tv_title);
		titlebar_back_tv_right = (TextView) findViewById(R.id.titlebar_back_tv_right);
		titlebar_back_tv_right.setText(getString(R.string.forum_add));
		titlebar_back_tv_right.setVisibility(View.VISIBLE);
		mListView = (PullToRefreshListView) findViewById(R.id.forum_list_listview);
		mPosts = new ArrayList<Posts>();
		mAdapter = new ItemSingleAdapter<PostsItemView, Posts>(this, mPosts);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		super.prepareDatas(savedInstanceState);
		Intent intent = getIntent();
		tag = Tag.toObj(intent.getStringExtra("tag"));
		titlebar_back_tv_title.setText(tag.getName());
		this.showLoading();
		fetchDatas(true);
	}

	@Override
	protected void fetchCacheDatas() {
		// Step 1：先看缓存
		// RequestTag posts = new RequestTag(mReqTag,
		// GlobalConfig.Operator.LIST_FETCH);
		// this.getCacheManager().getCacheByUrl(this, posts,
		// GlobalConfig.URL_FORUM_POSTS_GET);
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		Map<String, String> params = new HashMap<String, String>();
		if (!needCache) {
			params.put("timestamp", this.getTimestamp() + "");
		}
		params.put("max", "" + 8);
		params.put("fid", "" + tag.getFid());
		RequestTag posts = new RequestTag(mReqTag,
				GlobalConfig.Operator.LIST_FETCH);
		this.getHttpApi().request(GlobalConfig.URL_FORUM_POSTS_GET, params,
				this, posts);
	}

	@Override
	public void onResponseCache(RespOut out) {
		super.onResponseCache(out);
	}

	@Override
	public void onResponse(RespOut arg0) {
		super.onResponse(arg0);
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			List<Posts> list = parser.getArray(Posts.class);
			System.out.println("list:" + list);
			if (list != null && list.size() > 0) {
				this.handleSuccessDatas(list);
			}
			this.handleFetchDataSuccess(list);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Posts posts = mPosts.get(arg2);
		Intent intent = new Intent(this, ForumDetailActivity.class);
		intent.putExtra("posts", posts.toJson());
		this.startActivity(intent);
	}

	@Override
	protected void handleSuccessDatas(List<?> result) {
		if (result != null && result.size() > 0) {
			List<Posts> temList = (List<Posts>) result;
			for (int i = 0; i < temList.size(); i++) {
				if (this.getTags() != null && this.getTags().size() > 0) {
					List<Tag> tags = this.getTags();
					for (int j = 0; j < tags.size(); j++) {
						if (temList.get(i).getFid() == tags.get(j).getFid()) {
							temList.get(i).setTagName(tags.get(j).getName());
						} else {
							continue;
						}
					}
				} else {
					return;
				}
			}
			Posts posts = (Posts) result.get(temList.size() - 1);
			this.setTimestamp(posts.getDateline());
			mPosts.addAll(temList);
			mAdapter.notifyDataSetChanged();
		}
		this.hideLoading();
	}

	@Override
	public void onResponseError(RespOut out) {
		super.onResponseError(out);
	}

	@Override
	protected boolean isDataEmpty() {
		return false;
	}

	@Override
	protected void clearDatas() {
		if (mPosts != null) {
			mPosts.clear();
		}
	}

	public void onBtnBackClick(View view) {
		this.finish();
	}

	public void onClickRightBtn(View view) {
		if (this.isLogin()) {
			Intent intent = new Intent(this, ForumPostsAddActivity.class);
			this.startActivity(intent);
		} else {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_CODE_LOGIN);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			Intent intent = new Intent(this, ForumPostsAddActivity.class);
			this.startActivity(intent);
			return;
		}
	}
}
