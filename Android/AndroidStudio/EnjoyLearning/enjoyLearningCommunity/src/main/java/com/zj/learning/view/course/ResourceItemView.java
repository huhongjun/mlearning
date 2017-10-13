package com.zj.learning.view.course;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.model.course.Resource;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

/**
 * 考试、作业列表ItemView
 * 
 * @author XingRongJing
 * 
 */
public class ResourceItemView extends LinearLayout implements IItemView {

	/** 标题、二级标题信息 **/
	private TextView mTvName, mTvInfo;
	/** 开始考试、作业 **/
	private Button mBtnStart;

	public ResourceItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public ResourceItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareView() {
		// TODO Auto-generated method stub
		mTvName = (TextView) this
				.findViewById(R.id.course_common_list_item_tv_name);
		mTvInfo = (TextView) this
				.findViewById(R.id.course_common_list_item_tv_below);
		mBtnStart = (Button) this
				.findViewById(R.id.course_common_list_item_btn_right);
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
		Resource resource = (Resource) item;
		mTvName.setText(resource.getResName());
		mBtnStart.setTag(resource);
		mTvInfo.setText(resource.getTime());

		int type = resource.getResType();
		if (resource.isExamResource(type)) {
			mBtnStart.setText(this.getResources().getString(
					R.string.course_exam_start));
		} else if (resource.isHomeworkResource(type)) {
			mBtnStart.setText(this.getResources().getString(
					R.string.course_homework_start));
		}

	}

}
