package com.zhijin.mlearning.app.control;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.constant.GlobalConfig;
import com.zhijin.mlearning.app.control.base.BaseActivity;
import com.zhijin.mlearning.app.control.media.MVideoPlayActivity;
import com.zhijin.mlearning.app.model.Course;
import com.zhijin.mlearning.support.util.MyToast;

public class CourseDetailsActivity extends BaseActivity
{

	private Course course;
	private TextView courseName, courseLength, courseDesc;

	@Override
	protected void prepareViews()
	{
		setContentView(R.layout.course_details_layout);
		courseName = (TextView) findViewById(R.id.Course_Name);
		courseLength = (TextView) findViewById(R.id.Course_Time);
		courseDesc = (TextView) findViewById(R.id.Course_desc);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas()
	{
		Intent intent = getIntent();
		course = (Course) intent.getSerializableExtra("courseSection");
		if (course != null) {
			if (course.getName() != null) {
				courseName.setText(course.getName());
			}
			if (course.getDesc() != null) {
				courseDesc.setText(course.getDesc());
			}
			if (course.getLength() != null) {
				courseLength.setText(course.getLength());
			}
		}
		super.prepareDatas();
	}

	public void onOfflineBtnClick(View view)
	{
		String courseUrl = course.getUrl();
		if (courseUrl != null) {
			String filePath = CoreApp.getApp().getOfflineFileExist(courseUrl);

			if (filePath != null) {
				// Intent intent = new Intent(this, VideoPlayActivity.class);
				Intent intent = new Intent(this, MVideoPlayActivity.class);
				intent.putExtra(GlobalConfig.KEY_COURSE, course);
				intent.putExtra(GlobalConfig.KEY_COURSEPATH, filePath);
				startActivity(intent);

			} else {
				MyToast.showText(getString(R.string.video_no_exsit));
			}
		} else {
			MyToast.showText(getString(R.string.video_no_exsit));
		}
	}

	public void onBackBtnClick(View view)
	{
		this.finish();
	}
}
