package com.zj.learning.control.forum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.MainActivity;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.control.forum.adapter.PostsTypeAdapter;
import com.zj.learning.model.forum.Posts;
import com.zj.learning.model.forum.Tag;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.TimeUtil;

@SuppressLint("NewApi")
public class ForumPostsAddActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private GridView forum_type_gv;
	private int fid;// 标签id
	private TextView titlebar_back_tv_right, forum_posts_title_et,
			forum_posts_content_et;
	private RadioButton forum_posts_isname_rb, forum_posts_name_rb;
	private RadioGroup anonymous_select_gr;
	private boolean isAnonymous;
	private ArrayList<Tag> tags;
	private PostsTypeAdapter tagAdapter;
	private Posts mPosts;
	private static final int REQ_CODE_ADD = 2;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		setContentView(R.layout.forum_posts_add);
		tags = new ArrayList<Tag>();
		titlebar_back_tv_right = (TextView) findViewById(R.id.titlebar_back_tv_right);
		titlebar_back_tv_right.setText(getString(R.string.posts_send));
		titlebar_back_tv_right.setVisibility(View.VISIBLE);
		forum_type_gv = (GridView) findViewById(R.id.forum_type_gv);
		tagAdapter = new PostsTypeAdapter(this, tags);
		// 为GridView设置适配器
		forum_type_gv.setAdapter(tagAdapter);
		// 注册监听事件
		forum_type_gv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				fid = tags.get(position).getFid();
				v.setBackgroundColor(Color.RED);
				tagAdapter.notifyDataSetChanged();
			}
		});
		forum_posts_title_et = (TextView) findViewById(R.id.forum_posts_title_et);
		forum_posts_content_et = (TextView) findViewById(R.id.forum_posts_content_et);
		anonymous_select_gr = (RadioGroup) findViewById(R.id.anonymous_select_gr);
		anonymous_select_gr.setOnCheckedChangeListener(this);
		forum_posts_isname_rb = (RadioButton) findViewById(R.id.forum_posts_isname_rb);
		forum_posts_name_rb = (RadioButton) findViewById(R.id.forum_posts_name_rb);

	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		super.prepareDatas(savedInstanceState);
		if (this.getTags() != null && this.getTags().size() > 0) {
			tags.addAll(this.getTags());
		} else {
			fetchCacheTag();
		}
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

	public void onBtnBackClick(View view) {
		this.finish();
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
			} else {
				fetchTag();
			}
		}
	}

	@Override
	public void onResponse(RespOut arg0) {
		super.onResponse(arg0);
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (parser.isSuccess()) {
			if (arg0.param.operator == GlobalConfig.Operator.TAG_FETCH) {
				ArrayList<Tag> tagList = (ArrayList<Tag>) parser
						.getArray(Tag.class);
				tags.addAll(tagList);
				this.setTags(tagList);
				tagAdapter.notifyDataSetChanged();
			} else if (arg0.param.operator == GlobalConfig.Operator.OBJECT_ADD) {
				String result = parser.getValue("result").toString();
				System.out.println("result:" + result);
				Intent intent = new Intent(this, MainActivity.class);
				intent.putExtra("posts", mPosts.toJson());
				this.setResult(RESULT_OK, intent);
			}
		}
	}

	public void onClickRightBtn(View view) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("loginName", "test");
		params.put("fid", "" + fid);
		System.out.println("fid:" + fid);
		if (isAnonymous) {
			params.put("nname", this.getString(R.string.anonymous_name));
		}
		String subject = forum_posts_title_et.getText().toString().trim();
		if (!subject.isEmpty()) {
			params.put("subject", subject);
		} else {
			return;
		}
		String message = forum_posts_content_et.getText().toString().trim();
		if (!message.isEmpty()) {
			params.put("message", message);
		} else {
			return;
		}
		mPosts = new Posts();
		mPosts.setAuthor("test");
		mPosts.setDateline(TimeUtil.getCurrentTimeBySeconds());
		mPosts.setFid(fid);
		mPosts.setMessage(message);
		if (isAnonymous) {
			mPosts.setNname(this.getString(R.string.anonymous_name));
		}
		mPosts.setSubject(subject);
		RequestTag tag = new RequestTag(mReqTag,
				GlobalConfig.Operator.OBJECT_ADD);
		this.getHttpApi().request(GlobalConfig.URL_FORUM_POSTS_ADD, params,
				this, tag);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == forum_posts_isname_rb.getId()) {
			isAnonymous = true;
		} else if (checkedId == forum_posts_name_rb.getId()) {
			isAnonymous = false;
		}
	}
}
