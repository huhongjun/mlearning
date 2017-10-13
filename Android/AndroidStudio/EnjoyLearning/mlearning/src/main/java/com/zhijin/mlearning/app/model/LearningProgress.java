package com.zhijin.mlearning.app.model;

import java.io.Serializable;

import android.content.Context;
import android.view.ViewGroup;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.view.item.ItemSingle;
import com.zhijin.mlearning.app.view.item.ItemView;

public class LearningProgress extends ItemSingle implements Serializable
{

	private static final long serialVersionUID = 1L;

	private int id;
	private String memberId; // 会员唯一标识
	private String sectionName; // 课程视频名称
	private int sectionId;// 课程Id
	private long sectionProgress; // 记录值

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getMemberId()
	{
		return memberId;
	}

	public void setMemberId(String memberId)
	{
		this.memberId = memberId;
	}

	public String getSectionName()
	{
		return sectionName;
	}

	public void setSectionName(String sectionName)
	{
		this.sectionName = sectionName;
	}

	public int getSectionId()
	{
		return sectionId;
	}

	public void setSectionId(int sectionId)
	{
		this.sectionId = sectionId;
	}

	public long getSectionProgress()
	{
		return sectionProgress;
	}

	public void setSectionProgress(long sectionProgress)
	{
		this.sectionProgress = sectionProgress;
	}

	@Override
	public String toString()
	{
		return "LearningProgress [memberId=" + memberId + ", sectionName=" + sectionName + ", sectionId=" + sectionId
				+ ", sectionProgress=" + sectionProgress + "]";
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root)
	{
		return createCellFromXml(context, R.layout.progress_list_item, root);
	}

}
