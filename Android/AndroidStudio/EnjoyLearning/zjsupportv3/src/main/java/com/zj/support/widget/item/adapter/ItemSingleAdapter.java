package com.zj.support.widget.item.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 单一视图Adapter
 * 
 * @author XingRongJing
 * 
 * @param <V>
 *            ListView的ItemView
 * @param <I>
 *            ListView的数据模型
 */
public class ItemSingleAdapter<V extends IItemView, I extends ItemSingle>
		extends BaseItemAdapter<IItemView, I> {

	public ItemSingleAdapter(Context ctx, List<I> datas) {
		super(ctx, datas);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder<V> holder = null;
		I item = (I) this.getItem(position);
		if (null == item) {
			return null;
		}
		if (null == convertView) {
			holder = new ViewHolder<V>();
			convertView = (View) item.createCell(mCtx, null);
			((V) convertView).prepareView();
			((V) convertView).prepareItemProperty(this);
			holder.itemView = (V) convertView;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder<V>) convertView.getTag();
		}
		holder.itemView.setItem(item, position);
		return convertView;
	}

}
