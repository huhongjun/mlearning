package com.zj.learning.control.forum.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zj.learning.R;
import com.zj.learning.model.forum.Tag;

public class PostsTypeAdapter extends BaseAdapter {

	// 上下文对象
	private Context context;
	private ArrayList<Tag> typeList;

	public PostsTypeAdapter(Context context, ArrayList<Tag> typeList) {
		this.typeList = typeList;
		this.context = context;
	}

	public int getCount() {
		return typeList.size();
	}

	public Object getItem(int item) {
		return item;
	}

	public long getItemId(int id) {
		return id;
	}

	// 创建View方法
	@SuppressLint("ResourceAsColor")
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView textView;
		if (convertView == null) {
			textView = new TextView(context);
			textView.setId(position);
			textView.setBackgroundResource(R.drawable.posts_tags_bg_selector);
			textView.setTextColor(R.color.posts_tag_textcolor);
			textView.setGravity(Gravity.CENTER_HORIZONTAL);
			textView.setPadding(2, 4, 2, 4);// 设置间距
		} else {
			textView = (TextView) convertView;
		}
		textView.setText(typeList.get(position).getName());
		return textView;
	}

}
