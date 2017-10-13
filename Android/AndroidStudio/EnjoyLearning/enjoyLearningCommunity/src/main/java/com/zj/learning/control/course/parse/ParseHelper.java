package com.zj.learning.control.course.parse;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.text.TextUtils;
import android.util.Xml;

import com.zj.learning.model.course.Course;
import com.zj.learning.model.course.CourseChapter;
import com.zj.learning.model.course.CourseSection;
import com.zj.learning.model.course.Question;
import com.zj.learning.model.course.Resource;

public class ParseHelper {

	public List<?> getResults(InputStream is, int type)
			throws XmlPullParserException, IOException {
		return this.getQuestions(is);
	}

	/**
	 * 获取资源-试题、作业
	 * 
	 * @param is
	 * @param type
	 * @param ids
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public List<Resource> getResources(InputStream is, int type,
			List<Integer> ids) throws XmlPullParserException, IOException {
		List<Resource> resources = new ArrayList<Resource>();
		XmlPullParser parser = this.getXmlPullParser(is);
		int eventType = parser.getEventType();
		Resource resource = null;
		while (XmlPullParser.END_DOCUMENT != eventType) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if (TAG_RES.equalsIgnoreCase(name)) {// <res>始
					String resTypeStr = parser.getAttributeValue(null,
							TAG_RES_TYPE);
					int resType = -1;
					if (!TextUtils.isEmpty(resTypeStr)) {
						resType = Integer.valueOf(resTypeStr);
					}
					int id = this.getId(parser);
					// 考试、作业
					if (type == resType && null != ids && ids.contains(id)) {
						resource = new Resource();
						resource.setId(id);
						resource.setResType(resType);
					}
				} else if (TAG_RES_NAME.equalsIgnoreCase(name)) {// 资源名称
					if (null != resource) {
						resource.setResName(parser.nextText());
					}
				} else if (TAG_RES_DESC.equalsIgnoreCase(name)) {// 资源描述
					if (null != resource) {
						resource.setResDesc(parser.nextText());
					}
				} else if (TAG_RES_FILENAME.equalsIgnoreCase(name)) {// 资源文件名
					if (null != resource) {
						resource.setResFileName(parser.nextText());
					}
				} else if (TAG_RES_IMAGE.equalsIgnoreCase(name)) {// 资源封面图
					if (null != resource) {
						resource.setResImage(parser.nextText());
					}
				}else if(TAG_TIME.equalsIgnoreCase(name)){
					if(null!=resource){
						resource.setTime(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				String nameTemp = parser.getName();
				if (TAG_RES.equals(nameTemp) && null != resource) {// <res>止
					resources.add(resource);
					resource = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return resources;
	}

	public List<Course> getCourses(InputStream is)
			throws XmlPullParserException, IOException {
		List<Course> courses = new ArrayList<Course>();
		XmlPullParser parser = this.getXmlPullParser(is);
		int eventType = parser.getEventType();
		Course course = null;
		while (XmlPullParser.END_DOCUMENT != eventType) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if (TAG_RES.equalsIgnoreCase(name)) {// <res>始
					String resTypeStr = parser.getAttributeValue(null,
							TAG_RES_TYPE);
					int resType = -1;
					if (!TextUtils.isEmpty(resTypeStr)) {
						resType = Integer.valueOf(resTypeStr);
					}
					// 课程资源
					if (Resource.TYPE_COURSE == resType) {
						course = new Course();
						course.setCourseId(this.getId(parser));
					} else {// 考试、作业
						// continue;
					}
				} else if (TAG_RES_NAME.equalsIgnoreCase(name)) {// 资源名称
					if (null != course) {
						course.setResName(parser.nextText());
					}
				} else if (TAG_RES_DESC.equalsIgnoreCase(name)) {// 资源描述
					if (null != course) {
						course.setDesc(parser.nextText());
					}
				} else if (TAG_RES_FILENAME.equalsIgnoreCase(name)) {// 资源文件名
					if (null != course) {
						course.setResFileName(parser.nextText());
					}
				} else if (TAG_RES_IMAGE.equalsIgnoreCase(name)) {// 资源封面图
					if (null != course) {
						course.setResImage(parser.nextText());
					}
				} else if (TAG_RES_PRICE.equalsIgnoreCase(name)) {// 课程资源价格
					if (null != course) {
						String text = parser.nextText();
						if (TextUtils.isEmpty(text)) {
							continue;
						}
						float price = Float.valueOf(text);
						course.setPrice(price);
					}
				} else if (TAG_RES_QUESTION_IDS.equalsIgnoreCase(name)) {// 课程资源的考试ids
					if (null != course) {
						String ids = parser.nextText();
						course.setQuestionId(ids);
					}
				} else if (TAG_RES_TASK_IDS.equalsIgnoreCase(name)) {// 课程资源的作业ids
					if (null != course) {
						String ids = parser.nextText();
						course.setTaskId(ids);
					}
				}
				break;
			case XmlPullParser.END_TAG:
				String nameTemp = parser.getName();
				if (TAG_RES.equals(nameTemp) && null != course) {// <res>止
					courses.add(course);
					course = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return courses;
	}

	// /**
	// * 获取课程章节列表
	// *
	// * @param is
	// * @return
	// * @throws XmlPullParserException
	// * @throws IOException
	// */
	// public List<CourseSection> getCourseSections(InputStream is)
	// throws XmlPullParserException, IOException {
	// List<CourseSection> temps = new ArrayList<CourseSection>();
	// XmlPullParser parser = this.getXmlPullParser(is);
	// int eventType = parser.getEventType();
	// CourseSection chapter = null;
	// CourseSection section = null;
	// while (XmlPullParser.END_DOCUMENT != eventType) {
	// switch (eventType) {
	// case XmlPullParser.START_TAG:
	// String name = parser.getName();
	// if (TAG_CHATPTER.equalsIgnoreCase(name)) {// 章
	// chapter = new CourseSection();
	// String chapterName = parser.getAttributeValue(null,
	// TAG_CHATPTER_NAME);
	// chapter.setId(this.getId(parser));
	// chapter.setName(chapterName);
	// } else if (TAG_SECTION.equalsIgnoreCase(name)) {// 节
	// section = new CourseSection();
	// section.setId(this.getId(parser));
	// } else if (TAG_NAME.equalsIgnoreCase(name)) {// 节名
	// if (null != section) {
	// section.setName(parser.nextText());
	// }
	// } else if (TAG_URL.equalsIgnoreCase(name)) {// 节url
	// if (null != section) {
	// section.setVideoUrl(parser.nextText());
	// }
	// } else if (TAG_RES_DESC.equalsIgnoreCase(name)) {// 节描述
	// if (null != section) {
	// section.setDesc(parser.nextText());
	// }
	// } else if (TAG_LENGTH.equalsIgnoreCase(name)) {// 节时长
	// if (null != section) {
	// section.setLength(parser.nextText());
	// }
	// }
	// break;
	// case XmlPullParser.END_TAG:
	// String tempName = parser.getName();
	// if (TAG_SECTION.equalsIgnoreCase(tempName)) {
	// if (null != section) {
	// section.setParent(chapter);
	// temps.add(section);
	// section = null;
	// }
	// } else if (TAG_CHATPTER.equalsIgnoreCase(tempName)) {
	// if (null != chapter) {
	// temps.add(chapter);
	// chapter = null;
	// }
	// }
	// break;
	// }
	// eventType = parser.next();
	// }
	// return temps;
	// }

	/**
	 * 获取课程章节列表
	 * 
	 * @param is
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public List<CourseChapter> getCourseChapters(InputStream is)
			throws XmlPullParserException, IOException {
		List<CourseChapter> temps = new ArrayList<CourseChapter>();
		XmlPullParser parser = this.getXmlPullParser(is);
		int eventType = parser.getEventType();
		CourseChapter chapter = null;
		CourseSection section = null;
		while (XmlPullParser.END_DOCUMENT != eventType) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if (TAG_CHATPTER.equalsIgnoreCase(name)) {// 章
					chapter = new CourseChapter();
					String chapterName = parser.getAttributeValue(null,
							TAG_CHATPTER_NAME);
					chapter.setChapterId(this.getId(parser));
					chapter.setChapterName(chapterName);
					chapter.setSections(new ArrayList<CourseSection>());
				} else if (TAG_SECTION.equalsIgnoreCase(name)) {// 节
					section = new CourseSection();
					section.setSectionId(this.getId(parser));
				} else if (TAG_NAME.equalsIgnoreCase(name)) {// 节名
					if (null != section) {
						section.setName(parser.nextText());
					}
				} else if (TAG_URL.equalsIgnoreCase(name)) {// 节url
					if (null != section) {
						section.setUrl(parser.nextText());
					}
				} else if (TAG_RES_DESC.equalsIgnoreCase(name)) {// 节描述
					if (null != section) {
						section.setDesc(parser.nextText());
					}
				} else if (TAG_LENGTH.equalsIgnoreCase(name)) {// 节时长
					if (null != section) {
						section.setLength(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				String tempName = parser.getName();
				if (TAG_SECTION.equalsIgnoreCase(tempName)) {
					if (null != section) {
						List<CourseSection> tempSections = chapter
								.getSections();
						tempSections.add(section);
						section = null;
					}
				} else if (TAG_CHATPTER.equalsIgnoreCase(tempName)) {
					if (null != chapter) {
						temps.add(chapter);
						chapter = null;
					}
				}
				break;
			}
			eventType = parser.next();
		}
		return temps;
	}

	/**
	 * 获取考试、作业试题
	 * 
	 * @param is
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public List<Question> getQuestions(InputStream is)
			throws XmlPullParserException, IOException {
		List<Question> temps = new ArrayList<Question>();
		XmlPullParser parser = this.getXmlPullParser(is);
		int eventType = parser.getEventType();
		Question question = null;
		while (XmlPullParser.END_DOCUMENT != eventType) {
			switch (eventType) {
			case XmlPullParser.START_TAG:
				String name = parser.getName();
				if (TAG_QUESTION.equalsIgnoreCase(name)) {// 一道题
					question = new Question();
				} else if (TAG_TYPE.equalsIgnoreCase(name)) {// 题目类型
					String typeStr = parser.nextText();
					int type = -1;
					if (!TextUtils.isEmpty(typeStr)) {
						type = Integer.valueOf(typeStr);
					}
					if (null != question) {
						question.setType(type);
					}
				} else if (TAG_TITLE.equalsIgnoreCase(name)) {// 题目标题
					if (null != question) {
						question.setTitle(parser.nextText());
					}
				} else if (TAG_OPTIONS.equalsIgnoreCase(name)) {// 选项集（可空）
					if (null != question) {
						List<String> options = new ArrayList<String>();
						question.setOptions(options);
					}
				} else if (TAG_OPTION.equalsIgnoreCase(name)) {// 每个选项
					if (null != question) {
						question.addOption(parser.nextText());
					}
				} else if (TAG_ANSWERS.equalsIgnoreCase(name)) {// 答案集
					if (null != question) {
						List<String> answers = new ArrayList<String>();
						question.setAnswers(answers);
					}
				} else if (TAG_ANSWER.equalsIgnoreCase(name)) {// 每个答案
					if (null != question) {
						question.addAnswer(parser.nextText());
					}
				} else if (TAG_ANALYZE.equalsIgnoreCase(name)) {// 解析
					if (null != question) {
						question.setAnalyze(parser.nextText());
					}
				}
				break;
			case XmlPullParser.END_TAG:
				String tempName = parser.getName();
				if (TAG_QUESTION.equalsIgnoreCase(tempName) && null != question) {
					temps.add(question);
					question = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return temps;
	}

	private XmlPullParser getXmlPullParser(InputStream is)
			throws XmlPullParserException {
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, ENCODING);
		return parser;
	}

	private List<Integer> getIdsByString(String idsStr) {
		List<Integer> ids = new ArrayList<Integer>();
		String[] temps = idsStr.split(ID_SEPERATOR);
		if (null != temps) {
			for (String temp : temps) {
				try {
					int id = Integer.valueOf(temp);
					ids.add(id);
				} catch (Exception e) {
				}
			}
		}
		return ids;
	}

	private int getId(XmlPullParser parser) {
		String temp = parser.getAttributeValue(null, TAG_ID);
		int id = -1;
		if (!TextUtils.isEmpty(temp)) {
			id = Integer.valueOf(temp);
		}
		return id;
	}

	private static final String ENCODING = "UTF-8";
	private static final String ID_SEPERATOR = ",";
	private static final String TAG_RES = "res";
	private static final String TAG_ID = "id";
	private static final String TAG_RES_TYPE = "resType";
	private static final String TAG_RES_NAME = "resName";
	private static final String TAG_RES_IMAGE = "resImage";
	private static final String TAG_RES_FILENAME = "resFileName";
	private static final String TAG_RES_QUESTION_IDS = "questionId";
	private static final String TAG_RES_TASK_IDS = "taskId";
	private static final String TAG_RES_DESC = "desc";
	private static final String TAG_RES_PRICE = "price";
	private static final String TAG_TIME = "time";

	private static final String TAG_CHATPTER = "chapter";
	private static final String TAG_CHATPTER_NAME = "chapterName";
	private static final String TAG_SECTION = "section";
	private static final String TAG_NAME = "name";
	private static final String TAG_URL = "url";
	private static final String TAG_LENGTH = "length";

	private static final String TAG_QUESTION = "question";
	private static final String TAG_TYPE = "type";
	private static final String TAG_TITLE = "title";
	private static final String TAG_OPTIONS = "options";
	private static final String TAG_OPTION = "option";
	private static final String TAG_ANSWERS = "answers";
	private static final String TAG_ANSWER = "answer";
	private static final String TAG_ANALYZE = "analyze";

}
