package com.zhijin.mlearning.app.control;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.api.DomParseUtil;
import com.zhijin.mlearning.app.control.base.BaseActivity;
import com.zhijin.mlearning.app.model.Course;
import com.zhijin.mlearning.app.view.adapter.ItemSingleAdapter;
import com.zhijin.mlearning.app.view.itemview.CourseSectionItemView;

public class CourseSectionActivity extends BaseActivity implements OnItemClickListener
{

	private ItemSingleAdapter<CourseSectionItemView, Course> mAdapter;
	private ListView mListView;
	private List<Course> courseSectionList;
	private Course chapterCourse;
	private TextView titleName;

	@Override
	protected void prepareViews()
	{
		setContentView(R.layout.course_section_list);
		Intent intent = getIntent();
		titleName = (TextView) findViewById(R.id.titleName);
		chapterCourse = (Course) intent.getSerializableExtra("courseChapter");
		if (chapterCourse != null) {
			titleName.setText(chapterCourse.getName());
		}
		mListView = (ListView) findViewById(R.id.course_listView);
		courseSectionList = new ArrayList<Course>();
		mAdapter = new ItemSingleAdapter<CourseSectionItemView, Course>(courseSectionList, this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas()
	{
		DomParseUtil domParseUtil = new DomParseUtil();
		List<Course> allSectionList = domParseUtil.getCourseSectionList();
		if (chapterCourse != null && allSectionList != null) {
			for (int i = 0; i < allSectionList.size(); i++) {
				Course course = allSectionList.get(i);
				if (course.getFkCourse() != null && course.getFkCourse().getId() == chapterCourse.getId()) {
					courseSectionList.add(course);
				}
			}
		}
		super.prepareDatas();
	}

	protected void notifyDataChanged()
	{
		if (null == mAdapter) {
			return;
		}
		mAdapter.notifyDataSetChanged();
	}

	@Override
	protected void prepareDatasChanged()
	{
		notifyDataChanged();
		super.prepareDatasChanged();
	}

	public void onBackBtnClick(View view)
	{
		this.finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Course course = courseSectionList.get(position);
		Intent intent = new Intent(this, CourseDetailsActivity.class);
		intent.putExtra("courseSection", course);
		startActivity(intent);
	}

}
