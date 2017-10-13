package com.zj.support.widget.item.model;

import android.content.Context;
import android.view.ViewGroup;

import com.zj.support.widget.item.IItemView;

/**
 * ListView单一View的抽象基类
 * 
 * @author XingRongJing
 * 
 */
public abstract class ItemSingle extends BaseItem {

	public abstract IItemView createCell(Context context, ViewGroup root);

}
