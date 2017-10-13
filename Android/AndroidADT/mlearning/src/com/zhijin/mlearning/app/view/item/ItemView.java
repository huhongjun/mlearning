package com.zhijin.mlearning.app.view.item;

/**
 * ListView的子View接口，所有子View必实现
 * 
 * @author XingRongJing
 * 
 */
public interface ItemView
{

	public void prepareViews();

	public void prepareItemProperty(Object obj);

	public void setItem(BaseItem item, int pos);
}
