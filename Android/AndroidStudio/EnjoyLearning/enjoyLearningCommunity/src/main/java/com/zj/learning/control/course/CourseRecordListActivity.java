package com.zj.learning.control.course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.control.common.base.BaseRefreshListActivity;
import com.zj.learning.control.course.video.VideoOfflineActivity;
import com.zj.learning.control.course.video.VideoOnlineActivity;
import com.zj.learning.model.course.VideoRecord;
import com.zj.learning.view.course.CourseRecordItemView;
import com.zj.support.http.model.RequestTag;
import com.zj.support.http.model.RespOut;
import com.zj.support.util.CommonUtil;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;
import com.zj.support.widget.refresh.PullToRefreshListView;

/**
 * 课程学习历史记录
 * 
 * @author XingRongJing
 * 
 */
public class CourseRecordListActivity extends BaseRefreshListActivity {

	private static final int REQ_CODE_VIDEO = 1;
	private List<VideoRecord> mRecords;
	private TextView mTvTitle;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.course_record_list);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mListView = (PullToRefreshListView) this
				.findViewById(R.id.course_record_list_listview);

		mTvTitle.setText(this.getString(R.string.course_record_title));
	}

	@Override
	protected void prepareDatas(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.prepareDatas(savedInstanceState);
		mRecords = new ArrayList<VideoRecord>();
		mAdapter = new ItemSingleAdapter<CourseRecordItemView, VideoRecord>(
				this, mRecords);
		mListView.setAdapter(mAdapter);

		this.onFirstManualRefresh();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		VideoRecord record = (VideoRecord) parent.getAdapter()
				.getItem(position);
		this.startVideo(record);
	}

	@Override
	protected void fetchCacheDatas() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onResponseCache(RespOut out) {
		// TODO Auto-generated method stub
		super.onResponseCache(out);
		if (null == out) {
			return;
		}
		List<VideoRecord> records = (List<VideoRecord>) out.result;
		this.handleFetchDataSuccess(records);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void handleSuccessDatas(List<?> result) {
		// TODO Auto-generated method stub
		if (null == mRecords || null == result || result.isEmpty()) {
			return;
		}
		mRecords.addAll((List<VideoRecord>) result);
		Collections.sort(mRecords, new RecordComparator());
		this.notifyDataChanged();
	}

	@Override
	protected void fetchDatas(boolean needCache) {
		// TODO Auto-generated method stub
		this.getCoreApp().getRecordKeeper()
				.fetchVideoRecord(this, this, new RequestTag(mReqTag));

	}

	@Override
	protected boolean isDataEmpty() {
		// TODO Auto-generated method stub
		return null == mRecords ? true : mRecords.isEmpty();
	}

	@Override
	protected void clearDatas() {
		// TODO Auto-generated method stub
		if (null != mRecords) {
			mRecords.clear();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode || REQ_CODE_VIDEO != requestCode
				&& null != data) {
			return;
		}
		String videoUrl = data.getStringExtra("videoUrl");
		long currPos = data.getLongExtra("currPos", 0);
		long time = data.getLongExtra("stuDate", 0);
		VideoRecord record = this.getRecordByUrl(videoUrl);
		if (null == record) {
			return;
		}
		record.setCurrPos(currPos);
		record.setStuDate(time);
		Collections.sort(mRecords, new RecordComparator());
		this.notifyDataChanged();
	}

	private VideoRecord getRecordByUrl(String videoUrl) {
		if (TextUtils.isEmpty(videoUrl) || this.isDataEmpty()) {
			return null;
		}
		for (VideoRecord record : mRecords) {
			if (videoUrl.equals(record.getVideoUrl())) {
				return record;
			}
		}
		return null;
	}

	public void onClickRight(View view) {
		VideoRecord record = (VideoRecord) view.getTag();
		this.startVideo(record);
	}

	private void startVideo(VideoRecord record) {
		if (null == record) {
			return;
		}
		Intent intent = null;
		if (CommonUtil.isLocalUrl(record.getVideoUrl())) {
			intent = new Intent(this, VideoOfflineActivity.class);
		} else {
			intent = new Intent(this, VideoOnlineActivity.class);
		}
		intent.putExtra("videoRecord", record.toJson());
		intent.putExtra("isRecord", true);
		this.startActivityForResult(intent, REQ_CODE_VIDEO);
	}

	public class RecordComparator implements Comparator<VideoRecord> {

		@Override
		public int compare(VideoRecord lhs, VideoRecord rhs) {
			// TODO Auto-generated method stub
			if (lhs.getStuDate() > rhs.getStuDate()) {
				return -1;
			} else if (lhs.getStuDate() < rhs.getStuDate()) {
				return 1;
			}
			return 0;
		}

	}
}
