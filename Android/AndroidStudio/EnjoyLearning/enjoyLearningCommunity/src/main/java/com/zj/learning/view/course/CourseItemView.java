package com.zj.learning.view.course;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.model.course.Course;
import com.zj.support.util.CommonUtil;
import com.zj.support.widget.ZjImageView;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 课程列表ItemView
 * 
 * @author XingRongJing
 * 
 */
public class CourseItemView extends RelativeLayout implements IItemView {

	private ZjImageView mIvThumb;
	private TextView mTvName, mTvCoast, mTvScore, mTvLearnCount, mTvFocusCount;

	public CourseItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CourseItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CourseItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mIvThumb = (ZjImageView) this
				.findViewById(R.id.course_list_item_iv_show);
		mTvName = (TextView) this.findViewById(R.id.course_list_item_tv_name);
		mTvCoast = (TextView) this.findViewById(R.id.course_list_item_tv_coast);
		mTvScore = (TextView) this.findViewById(R.id.course_list_item_tv_score);
		mTvLearnCount = (TextView) this
				.findViewById(R.id.course_list_item_tv_learn);
		mTvFocusCount = (TextView) this
				.findViewById(R.id.course_list_item_tv_focus);
		mIvThumb.setErrorImageResId(R.drawable.list_default);
		mIvThumb.setDefaultImageResId(R.drawable.list_default);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		Course course = (Course) item;
		mTvName.setText(course.getResName());
		mTvScore.setText("评分："
				+ CommonUtil.floatToString(course.getAvgScore(), 1));
		mTvLearnCount.setText("学过（" + course.getLearnedNum() + "）");
		mTvFocusCount.setText("关注（" + course.getFocusNum() + "）");
		if (course.isFree()) {
			mTvCoast.setText(this.getResources()
					.getString(R.string.course_free));
		} else {
			mTvCoast.setText(course.getPrice() + "");
		}
		String imageUrl = GlobalConfig.URL_PIC_COURSE_THUMB
				+ GlobalConfig.SEPERATOR + course.getCourseId();
		mIvThumb.setImageUrl(imageUrl);
	}

}
