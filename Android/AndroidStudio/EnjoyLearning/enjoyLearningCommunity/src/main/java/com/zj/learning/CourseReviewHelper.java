package com.zj.learning;

import android.content.Context;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.model.course.CourseReview;
import com.zj.support.util.TimeUtil;
import com.zj.support.widget.ZjImageView;

/**
 * 课程评论助手
 * 
 * @author XingRongJing
 * 
 */
public class CourseReviewHelper {

	private ZjImageView mIvThumb;
	private TextView mTvName, mTvStatus, mTvTime, mTvContent;
	private RatingBar mRBarScore;

	/**
	 * 初始化评论助手
	 * 
	 * @param itemview
	 */
	public CourseReviewHelper(View itemview) {
		if (null == itemview) {
			throw new IllegalArgumentException(
					"CourseReviewItemView cannot be null");
		}
		mIvThumb = (ZjImageView) itemview
				.findViewById(R.id.user_show_course_review_iv_avator);
		mTvName = (TextView) itemview
				.findViewById(R.id.user_show_course_review_tv_name);
		mTvStatus = (TextView) itemview
				.findViewById(R.id.user_show_course_review_tv_status);
		mTvTime = (TextView) itemview
				.findViewById(R.id.user_show_course_review_tv_time);
		mTvContent = (TextView) itemview
				.findViewById(R.id.course_review_list_item_tv_desc);
		mRBarScore = (RatingBar) itemview
				.findViewById(R.id.user_show_course_review_ratingbar);
	}

	public void bindItemValue(CourseReview review) {
		if (null == review) {
			return;
		}
		if (null != mTvName) {
			mTvName.setText(review.getUserName());
		}
		if (null != mTvStatus) {
			mTvStatus.setText(this.getStatusText(mTvStatus.getContext(),
					review.getStuState()));
		}
		if (null != mTvContent) {
			mTvContent.setText(review.getContent());
		}
		if (null != mTvTime) {
			mTvTime.setText(TimeUtil.getTimeTips(mTvTime.getContext(),
					review.getCommentTime()));
		}

		if (null != mRBarScore) {
			int progress = ((int) (review.getFlag0() + review.getFlag1() + review
					.getFlag2()) / 3);
			mRBarScore.setProgress(progress/2);
		}
	}

	private String getStatusText(Context ctx, int status) {
		String result = null;
		switch (status) {
		case CourseReview.STATUS_LEARN_ING:
			result = ctx.getString(R.string.course_review_status_ing);
			break;
		case CourseReview.STATUS_LEARN_COMPLETE:
			result = ctx.getString(R.string.course_review_status_complete);
			break;
		case CourseReview.STATUS_LEARN_GIVEUP:
			result = ctx.getString(R.string.course_review_status_giveup);
			break;
		}
		return result;
	}
}
