package com.zj.learning.view.course;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.zj.learning.CourseReviewHelper;
import com.zj.learning.model.course.CourseReview;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 课程评价列表ItemView
 * 
 * @author XingRongJing
 * 
 */
public class CourseReviewItemView extends RelativeLayout implements IItemView {

	private CourseReviewHelper mHelper;

	public CourseReviewItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public CourseReviewItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CourseReviewItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mHelper = new CourseReviewHelper(this);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		CourseReview review = (CourseReview) item;
		mHelper.bindItemValue(review);
	}

}
