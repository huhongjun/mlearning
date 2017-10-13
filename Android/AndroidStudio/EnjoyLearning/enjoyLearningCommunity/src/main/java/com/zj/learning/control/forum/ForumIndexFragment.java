package com.zj.learning.control.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.LoginActivity;
import com.zj.learning.control.common.base.BaseRefreshListFragment;
import com.zj.learning.control.forum.adapter.PostsTypeAdapter;
import com.zj.learning.model.forum.Posts;
import com.zj.learning.model.forum.Tag;
import com.zj.learning.view.forum.PostsItemView;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 论坛
 * 
 * @author wliting
 */
public class ForumIndexFragment extends BaseRefreshListFragment {

	private List<Posts> mPosts;
	private GridView forum_type_gv;
	private ArrayList<Tag> tags;
	private PostsTypeAdapter tagAdapter;
	private static final int REQ_CODE_LOGIN = 1, REQ_CODE_ADD = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		tags = new ArrayList<Tag>();
		mPosts = new ArrayList<Posts>();
		mAdapter = new ItemSingleAdapter<PostsItemView, Posts>(
				this.getActivity(), mPosts);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.forum_list, null);
		ImageView titlebar_back_iv_back = (ImageView) view
				.findViewById(R.id.titlebar_back_iv_back);
		titlebar_back_iv_back.setVisibility(View.GONE);
		TextView titlebar_back_tv_title = (TextView) view
				.findViewById(R.id.titlebar_back_tv_title);
		titlebar_back_tv_title.setText(getString(R.string.forum));
		TextView titlebar_back_tv_right = (TextView) view
				.findViewById(R.id.titlebar_back_tv_right);
		titlebar_back_tv_right.setText(getString(R.string.forum_add));
		titlebar_back_tv_right.setVisibility(View.VISIBLE);
		titlebar_back_tv_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (ForumIndexFragment.this.isLogin()) {
					ForumIndexFragment.this.startActivityForResult(new Intent(
							ForumIndexFragment.this.getActivity(),
							ForumPostsAddActivity.class), REQ_CODE_ADD);
				} else {
					ForumIndexFragment.this.startActivityForResult(new Intent(
							ForumIndexFragment.this.getActivity(),
							LoginActivity.class), REQ_CODE_LOGIN);
				}
			}
		});
		mListView = (PullToRefreshListView) view
				.findViewById(R.id.forum_list_listview);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		tagAdapter = new PostsTypeAdapter(this.getActivity(), tags);
		forum_type_gv = (GridView) view.findViewById(R.id.forum_type_gv);
		forum_type_gv.setVisibility(View.VISIBLE);
		forum_type_gv.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 为GridView设置适配器
		forum_type_gv.setAdapter(tagAdapter);
		// 注册监听事件
		forum_type_gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(ForumIndexFragment.this
						.getActivity(), ForumListActivity.class);
				intent.putExtra("tag", tags.get(position).toJson());
				ForumIndexFragment.this.getActivity().startActivity(intent);
			}
		});
		prepareByView();
		// 如果还未加载过
		if (!this.isLoaded()) {
			if (tags != null) {
				tags.clear();
			}
			fetchCacheTag();
			this.setIsLoaded(true);
		}
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Posts posts = (Posts) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this.getActivity(),
				ForumDetailActivity.class);
		intent.putExtra("posts", posts.toJson());
		this.startActivity(intent);
	}

	private void fetchCacheTag() {
		// Step 1：先看缓存
		RequestTag tag = new RequestTag(mReqTag,
				GlobalConfig.Operator.TAG_FETCH);
		this.getCacheManager().getCacheByUrl(this, tag,
				GlobalConfig.URL_FORUM_GETTAG);
	}

	private void fetchTag() {
		RequestTag tag = new RequestTag(mReqTag,
				GlobalConfig.Operator.TAG_FETCH);
		this.getHttpApi().request(GlobalConfig.URL_FORUM_GETTAG, null, this,
				tag);
	}

	@Override
	protected void fetchCacheDatas() {
		// Step 1：先看缓存
		RequestTag posts = new RequestTag(mReqTag,
				GlobalConfig.Operator.LIST_FETCH);
		this.getCacheManager().getCacheByUrl(this, posts,
				GlobalConfig.URL_FORUM_POSTS_GET);
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		Map<String, String> params = new HashMap<String, String>();
		if (!needCache) {
			params.put("dateline", this.getTimestamp() + "");
		}
		params.put("max", "" + 5);
		RequestTag posts = new RequestTag(mReqTag,
				GlobalConfig.Operator.LIST_FETCH);
		this.getHttpApi().request(GlobalConfig.URL_FORUM_POSTS_GET, params,
				this, posts);
	}

	@Override
	public void onResponse(RespOut out) {
		super.onResponse(out);
		RequestDataParser parser = new RequestDataParser(out.resp);
		if (parser.isSuccess()) {
			if (out.param.operator == GlobalConfig.Operator.TAG_FETCH) {
				ArrayList<Tag> tagList = (ArrayList<Tag>) parser
						.getArray(Tag.class);
				if (tagList != null) {
					tags.addAll(tagList);
					this.setTags(tagList);
					tagAdapter.notifyDataSetChanged();
					fetchCacheDatas();
				}else{
					this.handleFetchDataFail();
				}
			} else if (out.param.operator == GlobalConfig.Operator.LIST_FETCH) {
				List<Posts> list = parser.getArray(Posts.class);
				this.handleFetchDataSuccess(list);
			}
		} else {
			this.handleFetchDataFail();
		}
	}

	@Override
	public void onResponseCache(RespOut out) {
		super.onResponseCache(out);
		if (null == out.param) {
			return;
		}
		if (out.param.operator == GlobalConfig.Operator.TAG_FETCH) {
			ArrayList<Tag> results = (ArrayList<Tag>) out.result;
			if (results != null) {
				tags.addAll(results);
				tagAdapter.notifyDataSetChanged();
				this.setTags(results);
				fetchCacheDatas();
			} else {
				fetchTag();
			}
		} else if (out.param.operator == GlobalConfig.Operator.LIST_FETCH) {
			List<Posts> postsResult = (List<Posts>) out.result;
			this.handleFetchCacheComplete(postsResult);
		}
	}

	@Override
	public void onResponseError(RespOut out) {
		if (out.param.operator == GlobalConfig.Operator.LIST_FETCH) {
			this.handleFetchDataFail();
		}
		// super.onResponseError(out);
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

	@Override
	protected void handleSuccessDatas(List<?> result) {
		if (result != null && result.size() > 0) {
			List<Posts> temList = (List<Posts>) result;
			for (int i = 0; i < temList.size(); i++) {
				if (tags != null && tags.size() > 0) {
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
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQ_CODE_LOGIN:
			if (resultCode == this.getActivity().RESULT_OK) {
				Intent intent = new Intent(this.getActivity(),
						ForumPostsAddActivity.class);
				this.getActivity().startActivity(intent);
			}
			break;
		case REQ_CODE_ADD:
			if (resultCode == this.getActivity().RESULT_OK) {
				if (data != null) {
					Posts posts = Posts.toObj(data.getStringExtra("posts"));
					mPosts.add(posts);
					mAdapter.notifyDataSetChanged();
				}
			}
			break;
		default:
			break;
		}
	}
}
