package com.zhijin.mlearning.app.model;

import java.io.Serializable;

import android.content.Context;
import android.view.ViewGroup;

import com.zhijin.mlearning.R;
import com.zhijin.mlearning.app.view.item.ItemSingle;
import com.zhijin.mlearning.app.view.item.ItemView;

public class Course extends ItemSingle implements Serializable
{

	private static final long serialVersionUID = 1L;

	private int id;// 唯一标识
	private String name;// 课程名称
	private String length;// 课程时长
	private String url;// 课程相对路径
	private String desc; // 课程简介
	private Course fkCourse;// 属于哪一章

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getLength()
	{
		return length;
	}

	public void setLength(String length)
	{
		this.length = length;
	}

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getDesc()
	{
		return desc;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public Course getFkCourse()
	{
		return fkCourse;
	}

	public void setFkCourse(Course fkCourse)
	{
		this.fkCourse = fkCourse;
	}

	@Override
	public String toString()
	{
		return "Course [id=" + id + ", name=" + name + ", length=" + length + ", url=" + url + ", desc=" + desc + ", fkCourse="
				+ fkCourse + "]";
	}

	@Override
	public ItemView newCell(Context context, ViewGroup root)
	{
		if (url != null) {
			return createCellFromXml(context, R.layout.coursesection_list_item, root);
		} else {
			return createCellFromXml(context, R.layout.course_list_item, root);
		}
	}

}
