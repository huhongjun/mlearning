package com.zhijin.mlearning.app.view.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * 所有ListView的子View的基类
 * 
 * @author XingRongJing
 * @see ItemSingle
 * @see ItemMulti
 */
public abstract class BaseItem
{

	protected ItemView createCellFromXml(Context context, int resource, ViewGroup root)
	{
		return (ItemView) LayoutInflater.from(context).inflate(resource, root, false);
	}
}
