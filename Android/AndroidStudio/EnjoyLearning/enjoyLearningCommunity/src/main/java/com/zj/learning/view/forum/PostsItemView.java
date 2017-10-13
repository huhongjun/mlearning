package com.zj.learning.view.forum;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.CoreApp;
import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.model.forum.Posts;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 帖子列表ItemView
 * 
 * @author wliting
 * 
 */
@SuppressLint("NewApi")
public class PostsItemView extends RelativeLayout implements IItemView {

	private TextView mTvPostsTag, mTvPostsName, mTvUsername, mTvReplyNum,
			mTvLikeNum;

	public PostsItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PostsItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PostsItemView(Context context) {
		this(context, null);
	}

	@Override
	public void prepareView() {
		mTvPostsTag = (TextView) this.findViewById(R.id.posts_tag_tv);
		mTvPostsName = (TextView) this.findViewById(R.id.posts_name_tv);
		mTvUsername = (TextView) this.findViewById(R.id.posts_username_tv);
		mTvReplyNum = (TextView) this.findViewById(R.id.posts_commentnum_tv);
		mTvLikeNum = (TextView) this.findViewById(R.id.posts_likenum_tv);
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		Posts posts = (Posts) item;
		mTvPostsTag.setText(posts.getTagName());
		mTvPostsName.setText("" + posts.getSubject());
		if (posts.getNname() == null || posts.getNname().isEmpty()) {
			mTvUsername.setText("" + posts.getAuthor());
		} else {
			mTvUsername.setText(posts.getNname());
		}
		mTvReplyNum.setText(posts.getReples() + "评论");
		mTvLikeNum.setText(posts.getLoveNum() + "喜欢");
	}

	@Override
	public void prepareItemProperty(Object obj) {

	}

}
