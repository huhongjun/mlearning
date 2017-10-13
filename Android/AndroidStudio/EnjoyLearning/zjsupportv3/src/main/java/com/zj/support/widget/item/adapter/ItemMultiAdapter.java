package com.zj.support.widget.item.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemMulti;

/**
 * 两种不同View的Adapter
 * 
 * @author XingRongJing
 * 
 * @param <V>
 *            ListView的ItemView
 * @param <I>
 *            ListView的数据模型
 */
public class ItemMultiAdapter<V extends IItemView, I extends ItemMulti> extends
		BaseItemAdapter<IItemView, I> {

	/** ListView的子view的数量 **/
	private static final int TYPE_COUNT = 2;

	public ItemMultiAdapter(Context ctx, List<I> datas) {
		super(ctx, datas);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public int getItemViewType(int position) {
		// TODO Auto-generated method stub
		I item = (I) this.getItem(position);
		return ((ItemMulti) item).getItemType();
	}

	@Override
	public int getViewTypeCount() {
		// TODO Auto-generated method stub
		return TYPE_COUNT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder<V> holder = null;
		I item = (I) getItem(position);
		if (null == item) {
			return null;
		}
		if (null == convertView) {
			holder = new ViewHolder<V>();
			convertView = (View) item.createCellByType(mCtx,
					this.getItemViewType(position), null);
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
