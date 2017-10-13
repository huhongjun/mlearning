package com.zj.support.widget.item.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.BaseItem;

public abstract class BaseItemAdapter<V extends IItemView, I extends BaseItem>
		extends BaseAdapter {

	protected List<I> mDatas;
	protected Context mCtx;
	private Map<String, Object> mParams;
	/** Item是否可点击，默认可以 **/
	private boolean mEnableClick = true;

	public BaseItemAdapter(Context ctx, List<I> datas) {
		this.mCtx = ctx;
		this.mDatas = datas;
		mParams = new HashMap<String, Object>();
	}

	public void addParams(String key, Object obj) {
		if (null == mParams) {
			return;
		}
		mParams.put(key, obj);
	}

	public Object getParams(String key) {
		if (null == mParams) {
			return null;
		}
		return mParams.get(key);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == mDatas ? 0 : mDatas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null == mDatas ? 0 : mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return this.mEnableClick;
	}

	public void setEnableClick(boolean enableClick) {
		this.mEnableClick = enableClick;
	}

	protected static class ViewHolder<V extends IItemView> {
		V itemView;
	}
}
