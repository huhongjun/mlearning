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
import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.api.DomParseUtil;
import com.zhijin.mlearning.app.api.StoreOperation;
import com.zhijin.mlearning.app.control.base.BaseActivity;
import com.zhijin.mlearning.app.model.Course;
import com.zhijin.mlearning.app.view.adapter.ItemSingleAdapter;
import com.zhijin.mlearning.app.view.itemview.CourseItemView;
import com.zhijin.mlearning.support.network.Param;
import com.zhijin.mlearning.support.util.MyToast;

public class CourseChapterActivity extends BaseActivity implements OnItemClickListener
{

	private ItemSingleAdapter<CourseItemView, Course> mAdapter;
	private ListView mListView;
	private List<Course> courseChapterList;
	private Course typeCourse;
	private TextView titleName;

	@Override
	protected void prepareViews()
	{
		setContentView(R.layout.course_type_list);
		mEnableDoubleExit = true;
		mListView = (ListView) findViewById(R.id.course_listView);
		titleName = (TextView) findViewById(R.id.titleName);
		courseChapterList = new ArrayList<Course>();
		mAdapter = new ItemSingleAdapter<CourseItemView, Course>(courseChapterList, this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas()
	{
		DomParseUtil domParseUtil = new DomParseUtil();
		if (domParseUtil.getCourseTypeList() != null && domParseUtil.getCourseTypeList().size() > 0) {
			typeCourse = domParseUtil.getCourseTypeList().get(0);
			if (typeCourse != null) {
				titleName.setText(typeCourse.getName());
				List<Course> allChapterList = domParseUtil.getCourseChapeterList();
				if (allChapterList != null) {
					for (int i = 0; i < allChapterList.size(); i++) {
						Course course = allChapterList.get(i);
						if (course.getFkCourse() != null && course.getFkCourse().getId() == typeCourse.getId()) {
							courseChapterList.add(course);
						}
					}
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

	public void onRecordBtnClick(View view)
	{
		Intent intent = new Intent(this, ProgressListActivity.class);
		startActivity(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Course course = courseChapterList.get(position);
		Intent intent = new Intent(this, CourseSectionActivity.class);
		intent.putExtra("courseChapter", course);
		startActivity(intent);
	}

	/***
	 * 退出时保存用户新的学习记录
	 */
	public void saveProgressList()
	{
		if (CoreApp.getApp().getLearingProgressList() != null) {
			Param param = new Param();
			param.setHashCode(this.hashCode());
			StoreOperation.getInstance().saveLearingProgressList(CoreApp.getApp().getLearingProgressList(), param);
		}
	}

	@Override
	public void onBackPressed()
	{
		if (CoreApp.getApp().getLearingProgressList() != null && CoreApp.getApp().getLearingProgressList().size() > 0) {
			if (CoreApp.getApp().isNetworkConnected()) {
				saveProgressList();
			} else {
				super.onBackPressed();
			}
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void notifySuccessSaveLearningProgress(Param out)
	{
		super.notifySuccessSaveLearningProgress(out);
		if (out.getResult() != null) {
			if (out.getResult().toString().equals("success")) {
				MyToast.showText("学习记录保存成功");
				CoreApp.getApp().exit();
			} else {
				MyToast.showText("学习记录未保存成功");
			}
		}
	}

	@Override
	public void onFails(Param out)
	{
		MyToast.showText("学习记录未保存成功");
		CoreApp.getApp().exit();
		super.onFails(out);
	}

}
