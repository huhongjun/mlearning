package com.zj.support.widget.item.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.zj.support.widget.item.IItemView;

public abstract class BaseItem {

	/** 操作符（用于同一个项目里一个实体会有多处不同布局的列表展示） **/
	protected int mOperator;

	public int getOperator() {
		return mOperator;
	}

	public void setOperator(int operator) {
		this.mOperator = operator;
	}

	protected IItemView newCellFromXml(Context ctx, int resId, ViewGroup root) {
		return (IItemView) LayoutInflater.from(ctx).inflate(resId, root);
	};
}
