package com.zj.learning.control.course.exam;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.control.common.base.BaseFragment;
import com.zj.learning.model.course.AnswerCardItem;
import com.zj.learning.view.course.AnswerCardItemView;
import com.zj.support.widget.item.adapter.ItemSingleAdapter;

/**
 * 答题卡
 * 
 * @author XingRongJing
 * 
 */
public class AnswerFragment extends BaseFragment {

	private GridView mGridView;
	private TextView mTvTitle;
	private BaseAdapter mAdapter;
	private List<AnswerCardItem> mCountList;
	private String mResName;

	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		if (null != args) {
			mResName = args.getString("examTitle");
			mCountList = args.getParcelableArrayList("answerCounts");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.course_answer_card, null);
		mTvTitle = (TextView) view.findViewById(R.id.course_answer_tv_title);
		mGridView = (GridView) view.findViewById(R.id.course_answer_gridview);
		mAdapter = new ItemSingleAdapter<AnswerCardItemView, AnswerCardItem>(
				this.getActivity(), mCountList);
		mGridView.setAdapter(mAdapter);
		mTvTitle.setText(mResName);
		return view;
	}

	public void notifyDataChanged() {
		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}
	}

}
