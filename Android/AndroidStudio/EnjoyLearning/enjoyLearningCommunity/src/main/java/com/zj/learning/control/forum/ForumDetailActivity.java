package com.zj.learning.control.forum;

import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.control.common.base.BaseActivity;
import com.zj.learning.model.forum.Comment;
import com.zj.learning.model.forum.Posts;
import com.zj.support.http.api.RequestDataParser;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;

@SuppressLint("NewApi")
public class ForumDetailActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private Posts posts;
	private TextView postsName_tv, postsContent_tv, posts_comment_num_tv,
			titlebar_back_tv_title, titlebar_back_tv_right;
	private CheckBox anonymous_btn;
	private Comment comment;
	private TextView new_comment_content, posts_comment_content;

	@Override
	protected void prepareViews() {
		super.prepareViews();
		setContentView(R.layout.forum_posts_detail);
		postsName_tv = (TextView) findViewById(R.id.posts_name_tv);
		postsContent_tv = (TextView) findViewById(R.id.posts_content_tv);
		posts_comment_num_tv = (TextView) findViewById(R.id.posts_comment_num_tv);
		new_comment_content = (TextView) findViewById(R.id.comment_content_tv);
		posts_comment_content = (TextView) findViewById(R.id.posts_comment_content);
		anonymous_btn = (CheckBox) findViewById(R.id.anonymous_btn);
		anonymous_btn.setOnCheckedChangeListener(this);
		titlebar_back_tv_title = (TextView) findViewById(R.id.titlebar_back_tv_title);
		titlebar_back_tv_right = (TextView) findViewById(R.id.titlebar_back_tv_right);
		titlebar_back_tv_right
				.setBackgroundResource(R.drawable.course_focus_count_icon);
		titlebar_back_tv_right.setVisibility(View.VISIBLE);
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		super.prepareDatas(savedInstanceState);
		posts = Posts.toObj(getIntent().getStringExtra("posts"));
		if (posts != null) {
			if (posts.getNname() != null) {
				if (posts.getNname().isEmpty()) {
					titlebar_back_tv_title.setText(posts.getAuthor() + "的帖子");
				} else {
					titlebar_back_tv_title.setText(posts.getNname() + "的帖子");
				}
			}
			postsName_tv.setText(posts.getSubject());
			postsContent_tv.setText(posts.getMessage());
			posts_comment_num_tv.setText("" + posts.getReples());
			fetchNewComment();
		}
	}

	private void fetchNewComment() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tid", "" + posts.getTid());
		RequestTag tag = new RequestTag(mReqTag,
				GlobalConfig.Operator.OBJECT_FETCH);
		this.getHttpApi().request(GlobalConfig.URL_FORUM_POSTS_COMMENT_NEW,
				params, this, tag);
	}

	public void onPostsCommentAdd(View view) {
		String message = posts_comment_content.getText().toString().trim();
		if (message != null && !message.isEmpty()) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("tid", "" + posts.getTid());
			params.put("author", "test");
			params.put("message", message);
			params.put("userId", "" + 7);
			RequestTag tag = new RequestTag(mReqTag,
					GlobalConfig.Operator.OBJECT_ADD);
			this.getHttpApi().request(GlobalConfig.URL_FORUM_Comment_Add,
					params, this, tag);
		}
	}

	@Override
	public void onResponse(RespOut arg0) {
		super.onResponse(arg0);
		RequestDataParser parser = new RequestDataParser(arg0.resp);
		if (arg0.isSuccess) {
			if (arg0.param.operator == GlobalConfig.Operator.OBJECT_FETCH) {
				comment = parser.getOne(Comment.class);
				refreshView(comment);
			} else if (arg0.param.operator == GlobalConfig.Operator.OBJECT_ADD) {
			} else if (arg0.param.operator == GlobalConfig.Operator.GOOD_ADD) {
				Toast.makeText(this, "点赞成功", Toast.LENGTH_SHORT).show();
			}
		}

	}

	private void refreshView(Comment comment) {
		new_comment_content.setText(comment.getMessage());
	}

	@Override
	public void onResponseCache(RespOut out) {
		super.onResponseCache(out);
	}

	@Override
	public void onResponseError(RespOut out) {
		super.onResponseError(out);
	}

	public void onClickRightBtn(View view) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("tid", "" + posts.getTid());
		RequestTag tag = new RequestTag(mReqTag, GlobalConfig.Operator.GOOD_ADD);
		this.getHttpApi().request(GlobalConfig.URL_FORUM_POSTS_LOVE, params,
				this, tag);
	}

	public void onBtnBackClick(View view) {
		this.finish();
	}

	public void onMoreCommentClick(View view) {
		Intent intent = new Intent(this, ForumCommentListActivity.class);
		intent.putExtra("posts", posts.toJson());
		startActivity(intent);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	}
}