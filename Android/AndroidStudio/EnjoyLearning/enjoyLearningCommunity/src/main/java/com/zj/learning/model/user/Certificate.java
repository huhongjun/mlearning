package com.zj.learning.model.user;

import android.content.Context;
import android.view.ViewGroup;

import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 证书
 * 
 * @author XingRongJing
 * 
 */
public class Certificate extends ItemSingle{

	private int id;
	/** 标题 **/
	private String title;
	/** 条件 **/
	private String condition;
	/** 缩略图 **/
	private String thumb;
	/** 类型：课程证书/社区证书 **/
	private int type = TYPE_COURSE;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public static final int TYPE_COURSE = 0;
	public static final int TYPE_COMMUNITY = 1;

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.newCellFromXml(context, R.layout.certificate_list_item, root);
	}

}
