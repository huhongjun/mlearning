package com.zj.learning.control.user;

import android.view.View;

import com.zj.learning.R;
import com.zj.learning.control.common.base.BaseTabActivity;

/**
 * 证书专区
 * 
 * @author XingRongJing
 * 
 */
public class CertificateAreaActivity extends BaseTabActivity {

	private static final int PAGE_CERTIFICATE_COMMUNITY = 1;

	@Override
	protected void prepareViewsChanged() {
		// TODO Auto-generated method stub
		this.setTitleRBtnText(this.getString(R.string.certificate_course),
				this.getString(R.string.certificate_community));
		this.setRightVisible(this.getString(R.string.certificate_rule));
	}

	@Override
	protected void prepareFragments() {
		// TODO Auto-generated method stub
		this.adddFragment(new CourseCertificateListFragment());
		this.adddFragment(new CommunityCertificateListFragment());
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		if (PAGE_CERTIFICATE_COMMUNITY == arg0) {
			this.setRightCheck();
			CommunityCertificateListFragment myCourse = (CommunityCertificateListFragment) this
					.getFragmentByIndex(arg0);
			if (null != myCourse) {
				myCourse.onSelected();
			}
		} else {
			this.setLeftCheck();
		}
	}

	@Override
	public void onClickRightBtn(View view) {// 规则
		// TODO Auto-generated method stub
		super.onClickRightBtn(view);
	}

}
