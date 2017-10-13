package com.zj.learning.model.dynamic;

import android.content.Context;
import android.view.ViewGroup;

import com.zj.learning.R;
import com.zj.support.widget.item.IItemView;
import com.zj.support.widget.item.model.ItemSingle;

/**
 * 动态
 * 
 * @author XingRongJing
 * 
 */
public class Dynamic extends ItemSingle{

	private int id;
	/** 用户id-动态产生者id **/
	private int userId;
	private String username;
	/** 动态相关者id-如回答别人的问题时，此处是提问者，可为空 **/
	private int relativeUserId;
	/** 动态类型-如课程、问答、论坛、用户、证书等 **/
	private int type;
	/** 事件类型-动态类型下的子类型，如加入课程、关注课程、回答问题、评论帖子等 **/
	private int eventType;
	/** 事件id-事件源id，如提问时，是问题id，发帖时是帖子id **/
	private int eventId;
	/** 事件源标题-如帖子标题、课程名称、问题标题等 **/
	private String eventTitle;
	/** 动态时间 **/
	private long time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRelativeUserId() {
		return relativeUserId;
	}

	public void setRelativeUserId(int relativeUserId) {
		this.relativeUserId = relativeUserId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getEventType() {
		return eventType;
	}

	public void setEventType(int eventType) {
		this.eventType = eventType;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public static final int OPERATOR_DYNAMIC_ME = 1;
	public static final int OPERATOR_DYNAMIC_ALL = 2;

	public static final int TYPE_COURSE = 0;
	public static final int TYPE_ASKS = 1;
	public static final int TYPE_FORUM = 2;
	public static final int TYPE_USER = 3;
	public static final int TYPE_CERTIFICATE = 4;
	public static final int TYPE_SYSTEM = 5;

	/** 课程-关注课程 **/
	public static final int EVENT_TYPE_COURSE_FOCUS = 0;
	/** 课程-学习课程 **/
	public static final int EVENT_TYPE_COURSE_LEARN = 1;
	/** 课程-学完课程 **/
	public static final int EVENT_TYPE_COURSE_LEARN_COMPLETE = 2;
	/** 课程-评论课程 **/
	public static final int EVENT_TYPE_COURSE_REVIEW = 3;

	/** 问答-发布问题 **/
	public static final int EVENT_TYPE_ASKS_ADD = 0;
	/** 问答-回答问题 **/
	public static final int EVENT_TYPE_ASKS_ANSWER = 1;
	/** 问答-评论 **/
	public static final int EVENT_TYPE_ASKS_ANSWER_REVIEW = 2;

	/** 论坛-发帖 **/
	public static final int EVENT_TYPE_FORUM_ADD = 0;
	/** 论坛-评论帖子 **/
	public static final int EVENT_TYPE_FORUM_REVIEW = 1;
	/** 论坛-帖子点赞 **/
	public static final int EVENT_TYPE_FORUM_GOOD = 2;

	/** 用户-加入app **/
	public static final int EVENT_TYPE_USER_ADD = 0;
	/** 用户-修改信息 **/
	public static final int EVENT_TYPE_USER_UPDATE = 1;

	/** 证书-获得证书 **/
	public static final int EVENT_TYPE_CERTIFICATE_GOT = 0;

	/** 系统-加入新课程 **/
	public static final int EVENT_TYPE_SYSTEM_NEW_COURSE = 0;
	/** 系统-App升级 **/
	public static final int EVENT_TYPE_SYSTEM_APP_UPDATE = 1;
	/** 系统-大事记，如用户过10万等 **/
	public static final int EVENT_TYPE_SYSTEM_HISTORY = 2;

	@Override
	public IItemView createCell(Context context, ViewGroup root) {
		// TODO Auto-generated method stub
		return this.newCellFromXml(context, R.layout.dynamic_list_item, root);
	}
}
