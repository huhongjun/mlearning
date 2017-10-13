package com.zj.support.widget.item;

import com.zj.support.widget.item.model.BaseItem;

public interface IItemView {

	public void prepareView();

	public void prepareItemProperty(Object obj);

	public void setItem(BaseItem item, int pos);
}
