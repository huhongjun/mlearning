package com.zj.learning.control.asks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zj.learning.R;
import com.zj.learning.control.common.base.BaseTabFragment;
import com.zj.learning.control.course.CourseOfflineListFragment;
import com.zj.learning.model.asks.Question;

/**
 * 问答Tab的首页
 * 
 * 
 */
public class AsksIndexFragment extends BaseTabFragment implements
		OnClickListener {
	private static int PAGE_ASKS_ALL = 1;

	@Override
	protected void prepareFragments() {
		Fragment ask_all_fragment = this
				.newAsksListFragment(Question.OPERATOR_ALL);
		Fragment ask_common_fragment = this
				.newAsksListFragment(Question.OPERATOR_COMMON);
		this.adddFragment(ask_common_fragment);
		this.adddFragment(ask_all_fragment);
	}

	@Override
	protected void prepareViewsChanged() {
		super.prepareViewsChanged();
		this.setTitleRBtnText("常见问题", "全部问题");
		setTitleIcon(R.drawable.search, R.drawable.asks_add_icon);
	}

	@Override
	public void onPageSelected(int arg0) {
		this.setCurrPage(arg0);
		if (PAGE_ASKS_ALL == arg0) {
			this.setRightCheck();
		} else {
			this.setLeftCheck();
		}
		AsksListFragment fragment = (AsksListFragment) this
				.getFragmentByIndex(arg0);
		if (null != fragment) {
			fragment.onSelected();
		}
	}

	private Fragment newAsksListFragment(int operatorType) {
		AsksListFragment fragment = new AsksListFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("operatorType", operatorType);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onTitleRightClick(View view) {
		AsksListFragment fragment = (AsksListFragment) getFragmentByIndex(this
				.getCurrPage());
		fragment.onClickRightBtn(view);
	}

	@Override
	protected void onTitleLeftClick(View view) {
		super.onTitleLeftClick(view);
		Toast.makeText(getActivity(), "搜索", Toast.LENGTH_SHORT).show();
		Intent findIntent = new Intent(getActivity(), AsksFindActivity.class);
		getActivity().startActivity(findIntent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
