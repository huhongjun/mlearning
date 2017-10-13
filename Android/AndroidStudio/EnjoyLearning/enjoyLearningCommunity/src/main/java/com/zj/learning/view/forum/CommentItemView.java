package com.zj.learning.view.forum;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.model.forum.Comment;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 帖子评论列表ItemView
 * 
 * @author wliting
 * 
 */
public class CommentItemView extends RelativeLayout implements IItemView {

	// private TextView comment_username_tv, comment_userlevel_tv,
	// comment_time_tv, comment_content_tv;
	// private ZjImageView comment_user_iv_thumb;
	// private AsksHelper mAskHelper;
	private TextView comment_username_tv, comment_content_tv;

	public CommentItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CommentItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CommentItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		comment_username_tv = (TextView) this
				.findViewById(R.id.user_show_tv_name);
		// comment_userlevel_tv = (TextView) this
		// .findViewById(R.id.comment_userlevel_tv);
		// comment_time_tv = (TextView) this.findViewById(R.id.comment_time_tv);
		comment_content_tv = (TextView) this
				.findViewById(R.id.comment_content_tv);
		// comment_user_iv_thumb = (ZjImageView) this
		// .findViewById(R.id.comment_user_iv_thumb);
		// comment_user_iv_thumb.setErrorImageResId(R.drawable.avator_default);
		// comment_user_iv_thumb.setDefaultImageResId(R.drawable.avator_default);
	}

	@Override
	public void prepareItemProperty(Object obj) {// listview
		// @SuppressWarnings("unchecked")
		// ItemSingleAdapter<CommentItemView, Comment> adapter =
		// (ItemSingleAdapter<CommentItemView, Comment>) obj;
		// if (null == mAskHelper) {
		// mAskHelper = (AsksHelper) adapter.getParams("asksHelper");
		// }
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// Comment comment = (Comment) item;
		// comment_content_tv.setText(comment.getContent());
		// if (comment.getIsName()) {
		// comment_username_tv.setText("匿名小金");
		// } else {
		// comment_username_tv.setText(comment.getUserName());
		// }
	}
}
