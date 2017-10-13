package com.zhijin.mlearning.app.view.itemview;

import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhijin.mlearning.app.model.LearningProgress;
import com.zhijin.mlearning.app.view.item.BaseItem;
import com.zhijin.mlearning.app.view.item.ItemView;

public class ProgressListItemView extends RelativeLayout implements ItemView
{

	private TextView courseName, Course_Length;
	protected StringBuilder mFormatBuilder;
	protected Formatter mFormatter;

	public ProgressListItemView(Context context)
	{
		this(context, null);

	}

	public ProgressListItemView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		mFormatBuilder = new StringBuilder();
		mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
	}

	public ProgressListItemView(Context context, AttributeSet attrs)
	{
		this(context, attrs, 0);
	}

	@Override
	public void prepareViews()
	{
		courseName = (TextView) this.findViewById(com.zhijin.mlearning.R.id.Course_Name);
		Course_Length = (TextView) this.findViewById(com.zhijin.mlearning.R.id.Course_Length);

	}

	@Override
	public void prepareItemProperty(Object obj)
	{

	}

	@Override
	public void setItem(BaseItem item, int pos)
	{
		LearningProgress course = (LearningProgress) item;
		long progress = course.getSectionProgress();
		if (course.getSectionName() != null) {
			courseName.setText(course.getSectionName());
		}
		Course_Length.setText("已观看至" + stringForTime((int) progress));
	}

	private String stringForTime(int timeMs)
	{
		int totalSeconds = timeMs / 1000;

		int seconds = totalSeconds % 60;
		int minutes = (totalSeconds / 60) % 60;
		int hours = totalSeconds / 3600;

		mFormatBuilder.setLength(0);
		if (hours > 0) {
			return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
		} else {
			return mFormatter.format("%02d:%02d", minutes, seconds).toString();
		}
	}

}
