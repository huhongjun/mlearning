package com.zhijin.mlearning.app.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhijin.mlearning.app.view.item.ItemSingle;
import com.zhijin.mlearning.app.view.item.ItemView;

/**
 * 单一View的Adapter
 * 
 * @author XingRongJing
 * 
 * @param <V>
 *            ListView的子View
 * @param <I>
 *            ListView的数据模型
 */
public class ItemSingleAdapter<V extends ItemView, I extends ItemSingle> extends BaseAdapter
{

	protected List<I> mItems;
	protected Context mCtx;

	public ItemSingleAdapter(List<I> items, Context context)
	{
		this.mItems = items;
		this.mCtx = context;
	}

	@Override
	public int getCount()
	{
		return mItems != null ? mItems.size() : 0;
	}

	@Override
	public Object getItem(int position)
	{
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// TODO Auto-generated method stub
		ViewHolder<V> holder = null;
		I item = (I) getItem(position);
		if (null == convertView) {
			holder = new ViewHolder<V>();
			convertView = (View) item.newCell(mCtx, null);
			((ItemView) convertView).prepareViews();
			holder.itemview = (V) convertView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder<V>) convertView.getTag();
		}
		holder.itemview.setItem(item, position);
		return convertView;
	}

	private static class ViewHolder<V extends ItemView>
	{
		V itemview;
	}

}
