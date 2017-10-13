package com.zhijin.mlearning.app.view.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhijin.mlearning.app.model.Course;
import com.zhijin.mlearning.app.view.item.BaseItem;
import com.zhijin.mlearning.app.view.item.ItemView;

public class CourseSectionItemView extends RelativeLayout implements ItemView
{

	private TextView courseName, courseLength, courseDesc;

	public CourseSectionItemView(Context context)
	{
		this(context, null);

	}

	public CourseSectionItemView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public CourseSectionItemView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	@Override
	public void prepareViews()
	{
		courseName = (TextView) this.findViewById(com.zhijin.mlearning.R.id.Course_Name);
		courseLength = (TextView) this.findViewById(com.zhijin.mlearning.R.id.Course_Time);
		courseDesc = (TextView) this.findViewById(com.zhijin.mlearning.R.id.Course_desc);
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
		if (course.getLength() != null) {
			courseLength.setText(course.getLength());
		}
		if (course.getDesc() != null) {
			courseDesc.setText(course.getDesc());
		}
	}

}
