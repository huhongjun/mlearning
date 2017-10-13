package com.zhijin.mlearning.app.control;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.api.DomParseUtil;
import com.zhijin.mlearning.app.api.StoreOperation;
import com.zhijin.mlearning.app.constant.GlobalConfig;
import com.zhijin.mlearning.app.control.base.BaseActivity;
import com.zhijin.mlearning.app.control.media.MVideoPlayActivity;
import com.zhijin.mlearning.app.model.Course;
import com.zhijin.mlearning.app.model.LearningProgress;
import com.zhijin.mlearning.app.view.adapter.ItemSingleAdapter;
import com.zhijin.mlearning.app.view.itemview.ProgressListItemView;
import com.zhijin.mlearning.support.network.Param;
import com.zhijin.mlearning.support.sqlite.SqliteUtil;
import com.zhijin.mlearning.support.util.MyToast;

public class ProgressListActivity extends BaseActivity implements OnItemClickListener
{

	private ItemSingleAdapter<ProgressListItemView, LearningProgress> mAdapter;
	private ListView mListView;
	private List<LearningProgress> learningProgressList;;
	private LearningProgress learningProgress;
	private Param param;

	@Override
	protected void prepareViews()
	{
		setContentView(R.layout.progress_list);
		mListView = (ListView) findViewById(R.id.progress_listView);
		learningProgressList = new ArrayList<LearningProgress>();
		mAdapter = new ItemSingleAdapter<ProgressListItemView, LearningProgress>(learningProgressList, this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(this);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas()
	{
		param = new Param();
		param.setHashCode(this.hashCode());
		synchMemberLearningProgress();
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
		learningProgress = learningProgressList.get(position);
		Course course = getCourse(learningProgress.getSectionId());
		if (course != null) {
			String filePath = CoreApp.getApp().getOfflineFileExist(course.getUrl());
			if (filePath != null) {
				Intent intent = new Intent(this, MVideoPlayActivity.class);
				intent.putExtra(GlobalConfig.KEY_COURSE, course);
				intent.putExtra(GlobalConfig.KEY_COURSEPATH, filePath);
				startActivity(intent);
			} else {
				MyToast.showText("视频文件不存在");
			}
		}
	}

	/***
	 * 获得某一记录对应的小节
	 * 
	 * @param id
	 *            学习记录中的课程Id
	 * @return 对应的Course
	 */
	private Course getCourse(int id)
	{
		DomParseUtil dom = new DomParseUtil();
		List<Course> allsectionList = dom.getCourseSectionList();
		for (int i = 0; i < allsectionList.size(); i++) {
			if (allsectionList.get(i).getId() == id) {
				return allsectionList.get(i);
			} else {
				continue;
			}
		}
		return null;
	}

	public void synchMemberLearningProgress()
	{
		if (CoreApp.getApp().isNetworkConnected()) {
			if (CoreApp.getApp().getLearingProgressList() == null) {
				StoreOperation.getInstance().getLearingProgress(param);
			} else {
				learningProgressList.addAll(CoreApp.getApp().getLearingProgressList());
			}
		} else {
			learningProgressList.addAll(CoreApp.getApp().getLearingProgressList());
		}
	}

	/**
	 * 保存学习记录的处理
	 * 
	 * @see 1.将本地有改动标识的学习记录上传至服务器。成功后，将本地是否更新的标识改为false
	 * @see 2.将用户新的学习记录上传至服务器。成功后不做处理。
	 */
	// @Override
	// public void notifySuccessSaveLearningProgress(Param out) {
	// if (out.getMethod() == GlobalConfig.Method.M_SAVEUPLOADPROGRESSLIST) {
	// if (uploadLearningProgressList != null
	// && uploadLearningProgressList.size() > 0) {
	// for (int i = 0; i < uploadLearningProgressList.size(); i++) {
	// LearningProgress learningProgress = (LearningProgress)
	// uploadLearningProgressList
	// .get(i);
	// learningProgress.setIsUpload(false);
	// SqliteUtil.getInstance().saveOrUpdateDomain(
	// learningProgress);
	// }
	// learningProgressList
	// .addAll(objctListToItemSingleList(getLocalProgressList()));
	// notifyDataChanged();
	//
	// }
	// } else if (out.getMethod() == GlobalConfig.Method.M_SAVEPROGRESSLIST) {
	//
	// }
	// }

	/**
	 * 获取学习记录成功(成功后 将得到的同步到本地。同时将内存中的记录更新)
	 */
	@Override
	public void notifySuccessFindLearningProgress(Param out)
	{
		if (out.getResult() != null) {
			List<LearningProgress> learningProgressListResult = (List<LearningProgress>) out.getResult();
			for (int i = 0; i < learningProgressListResult.size(); i++) {
				LearningProgress learningProgress = (LearningProgress) learningProgressListResult.get(i);
				SqliteUtil.getInstance().saveOrUpdateDomain(learningProgress);
			}
			learningProgressList.addAll(learningProgressListResult);
			notifyDataChanged();
			CoreApp.getApp().setLearingProgressList(learningProgressListResult);
		}
	}

	/**
	 * 网络操作失败(无论什么操作，失败后将本地学习记录，放到内存中)
	 */
	@Override
	public void onFails(Param out)
	{
		notifyDataChanged();
	}

}
