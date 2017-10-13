package com.zj.support.widget.item.model;

import android.content.Context;
import android.view.ViewGroup;

import com.zj.support.widget.item.IItemView;

/**
 * ListView多种View的抽象基类（必须根据type区分）
 * 
 * @author XingRongJing
 * 
 */
public abstract class ItemMulti extends BaseItem {

	/** 视图类别，如聊天ListView里发送和接收的ItemView不同 **/
	protected int mItemType;

	public int getItemType() {
		return mItemType;
	}

	public void setItemType(int mItemType) {
		this.mItemType = mItemType;
	}

	public abstract IItemView createCellByType(Context context, int type,
			ViewGroup root);
}
