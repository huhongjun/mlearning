package com.zhijin.mlearning.app.view.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhijin.mlearning.app.model.Course;
import com.zhijin.mlearning.app.view.item.BaseItem;
import com.zhijin.mlearning.app.view.item.ItemView;

public class CourseItemView extends RelativeLayout implements ItemView
{

	private TextView courseName;

	public CourseItemView(Context context)
	{
		this(context, null);

	}

	public CourseItemView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public CourseItemView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	@Override
	public void prepareViews()
	{
		courseName = (TextView) this.findViewById(com.zhijin.mlearning.R.id.Course_Name);

	}

	@Override
	public void prepareItemProperty(Object obj)
	{

	}

	@Override
	public void setItem(BaseItem item, int pos)
	{
		Course course = (Course) item;
		if (course.getName() != null) {
			courseName.setText(course.getName());
		}
	}

}
