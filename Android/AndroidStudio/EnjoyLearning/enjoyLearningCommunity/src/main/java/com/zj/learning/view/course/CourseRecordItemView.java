package com.zj.learning.view.course;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.constant.GlobalConfig;
import com.zj.learning.model.course.VideoRecord;
import com.zj.support.util.TimeUtil;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 观看记录列表ItemView
 * 
 * @author XingRongJing
 * 
 */
public class CourseRecordItemView extends LinearLayout implements IItemView {

	/** 标题、二级标题信息 **/
	private TextView mTvSectionName, mTvCourseName, mTvVideoTime;
	/** 重看或续播 **/
	private Button mBtnVideo;

	public CourseRecordItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CourseRecordItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mTvSectionName = (TextView) this
				.findViewById(R.id.course_record_list_item_tv_section_name);
		mTvCourseName = (TextView) this
				.findViewById(R.id.course_record_list_item_tv_course_name);
		mTvVideoTime = (TextView) this
				.findViewById(R.id.course_record_list_item_tv_video_time);
		mBtnVideo = (Button) this
				.findViewById(R.id.course_record_list_item_btn_right);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub
		if (null == obj) {
			return;
		}
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		VideoRecord record = (VideoRecord) item;
		mTvSectionName.setText(record.getSectionName());
		mTvCourseName.setText(record.getCourseName());

		boolean isFinish = record.isVideoFinish();
		mTvVideoTime.setText(this.buildVideoTimeStr(isFinish,
				record.getCurrPos(), record.getTotalPos()));
		if (isFinish) {
			mBtnVideo.setText(this.getResources().getString(
					R.string.course_record_repeat));
		} else {
			mBtnVideo.setText(this.getResources().getString(
					R.string.course_record_going));
		}
		mBtnVideo.setTag(record);
	}

	private String buildVideoTimeStr(boolean isFinish, long cusPos,
			long totalPos) {
		StringBuilder sb = new StringBuilder();
		if (isFinish) {
			sb.append(this.getResources().getString(
					R.string.course_record_have_video_finish));
			sb.append(GlobalConfig.SEPERATOR);
			sb.append(TimeUtil.formatMillTime(totalPos));
		} else {
			sb.append(this.getResources().getString(
					R.string.course_record_have_video));
			sb.append(TimeUtil.formatMillTime(cusPos));
			sb.append(GlobalConfig.SEPERATOR);
			sb.append(TimeUtil.formatMillTime(totalPos));
		}
		return sb.toString();
	}

}
