package com.zhijin.mlearning.app.view.item;

import android.content.Context;
import android.view.ViewGroup;

/**
 * ListView单一View的抽象基类
 * 
 * @author XingRongJing
 * 
 */
public abstract class ItemSingle extends BaseItem
{

	protected int itemType;

	public void setItemType(int type)
	{
		this.itemType = type;
	}

	public int getItemType()
	{
		return this.itemType;
	};

	public abstract ItemView newCell(Context context, ViewGroup root);
}
