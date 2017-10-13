package com.zj.learning.model.course;

import java.util.List;

import com.google.gson.Gson;

/**
 * 试题Item，分多种试题类型
 * 
 * @author XingRongJing
 * 
 */
public class Question {

	private int id;
	/**
	 * 试题类型：0-单选 1-多选 2-判断 3-问答 4-完形填空
	 */
	private int type = -1;
	/** 问题标题 **/
	private String title;
	/** 回答选项 **/
	private List<String> options;
	/** 正确答案 **/
	private List<String> answers;
	/** 解析 **/
	private String analyze;
	/** 本题分值 **/
	private float score;

	public Question() {
	}

	public Question(int type, String title) {
		this.type = type;
		this.title = title;
	}

	public Question(int type, String question, List<String> options) {
		this(type, question);
		this.options = options;
	}

	public Question(int type, String question, List<String> options,
			List<String> answers, String anaylze) {
		this(type, question, options);
		this.answers = answers;
		this.analyze = anaylze;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public float getScore() {
		return score;
	}

	public void setScore(float score) {
		this.score = score;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getAnalyze() {
		return analyze;
	}

	public void setAnalyze(String analyze) {
		this.analyze = analyze;
	}

	public void addOption(String option) {
		if (null != this.options) {
			options.add(option);
		}
	}

	public void addAnswer(String answer) {
		if (null != this.answers) {
			answers.add(answer);
		}
	}

	public int getAnswersCount() {
		return (null == answers) ? 0 : answers.size();
	}

	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public static Question toObj(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, Question.class);
	}

	/** 单选 **/
	public static final int TYPE_SINGLE_CHOICE = 0;
	/** 多选 **/
	public static final int TYPE_MULTIPLE_CHOICE = 1;
	/** 判断 **/
	public static final int TYPE_JUDGE = 2;
	/** 简答 **/
	public static final int TYPE_SHORT_ANSWER = 3;
	/** 完形填空 **/
	public static final int TYPE_CLOSE = 4;
}
