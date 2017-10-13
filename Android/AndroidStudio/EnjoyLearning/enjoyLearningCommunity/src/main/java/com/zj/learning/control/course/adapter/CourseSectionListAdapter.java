package com.zj.learning.control.course.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.model.course.CourseChapter;
import com.zj.learning.model.course.CourseSection;

/**
 * 章节列表Adapter
 * 
 * @author XingRongJing
 * 
 */
public class CourseSectionListAdapter extends BaseExpandableListAdapter {

	private List<CourseChapter> mParents;
	private LayoutInflater mInflater;

	public CourseSectionListAdapter(Context ctx, List<CourseChapter> parents) {
		mInflater = LayoutInflater.from(ctx);
		this.mParents = parents;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return (null == mParents ? 0 : mParents.size());
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		// CourseSection parent = mParents.get(groupPosition);
		// List<CourseSection> childs =
		// mSectionHelper.getChildsByParent(parent);
		// return (null == childs ? 0 : childs.size());
		CourseChapter parent = mParents.get(groupPosition);
		List<CourseSection> childs = parent.getSections();
		return (null == childs ? 0 : childs.size());
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return (null == mParents ? null : mParents.get(groupPosition));
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		CourseChapter parent = mParents.get(groupPosition);
		List<CourseSection> childs = parent.getSections();
		return (null == childs ? null : childs.get(childPosition));
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupViewHolder holder = null;
		if (null == convertView) {
			holder = new GroupViewHolder();
			convertView = mInflater.inflate(
					R.layout.course_section_list_item_parent, null);
			holder.mTvName = (TextView) convertView
					.findViewById(R.id.course_section_list_item_parent_tv_name);
			holder.mTvCount = (TextView) convertView
					.findViewById(R.id.course_section_list_item_parent_tv_count);
			holder.mIvIcon = (ImageView) convertView
					.findViewById(R.id.common_list_item_iv_icon);
			holder.mIvIcon
					.setBackgroundResource(R.drawable.course_chapter_left_icon_selector);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		// CourseSection section = (CourseSection) this.getGroup(groupPosition);
		CourseChapter chapter = (CourseChapter) this.getGroup(groupPosition);
		holder.mTvName.setText(chapter.getChapterName());
		holder.mTvCount.setText(this.getChildrenCount(groupPosition) + "小节");
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChildViewHolder holder = null;
		if (null == convertView) {
			holder = new ChildViewHolder();
			convertView = mInflater.inflate(R.layout.course_common_list_item,
					null);
			holder.mTvName = (TextView) convertView
					.findViewById(R.id.course_common_list_item_tv_name);
			holder.mTvInfo = (TextView) convertView
					.findViewById(R.id.course_common_list_item_tv_below);
			holder.mBtnLearn = (Button) convertView
					.findViewById(R.id.course_common_list_item_btn_right);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}
		CourseSection section = (CourseSection) this.getChild(groupPosition,
				childPosition);
		holder.mTvName.setText(section.getName());
		holder.mTvInfo.setText(section.getLength());
		holder.mBtnLearn.setText(holder.mBtnLearn.getContext().getResources()
				.getString(R.string.course_learn_start));
		holder.mBtnLearn.setTag(section);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	private static class ChildViewHolder {
		private TextView mTvName, mTvInfo;
		private Button mBtnLearn;
	}

	private static class GroupViewHolder {
		private TextView mTvName;
		private TextView mTvCount;
		private ImageView mIvIcon;
	}

}
