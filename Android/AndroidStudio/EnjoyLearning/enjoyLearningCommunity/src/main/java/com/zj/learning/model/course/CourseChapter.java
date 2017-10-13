package com.zj.learning.model.course;

import java.util.List;

public class CourseChapter {

	private int chapterId;
	private String chapterName;
	private List<CourseSection> sections;
	public int getChapterId() {
		return chapterId;
	}
	public void setChapterId(int chapterId) {
		this.chapterId = chapterId;
	}
	public String getChapterName() {
		return chapterName;
	}
	public void setChapterName(String chapterName) {
		this.chapterName = chapterName;
	}
	public List<CourseSection> getSections() {
		return sections;
	}
	public void setSections(List<CourseSection> sections) {
		this.sections = sections;
	}
	
	
}
