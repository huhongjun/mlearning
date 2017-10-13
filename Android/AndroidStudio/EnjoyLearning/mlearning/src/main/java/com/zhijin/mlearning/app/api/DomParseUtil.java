package com.zhijin.mlearning.app.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.constant.GlobalConfig;
import com.zhijin.mlearning.app.model.Course;

public class DomParseUtil
{

	private List<Course> courseList, courseTypeList, courseChapeterList, courseSectionList;

	public DomParseUtil()
	{
		// 创建对象时自动读取本地文件获取课程数据
		if (courseList == null) {
			courseList = getCourses();
		}
	}

	private List<Course> getCourses()
	{
		InputStream inputStream;
		courseList = new ArrayList<Course>();
		courseTypeList = new ArrayList<Course>();
		courseChapeterList = new ArrayList<Course>();
		courseSectionList = new ArrayList<Course>();
		try {
			String filePath = CoreApp.getApp().getOfflineFileExist(GlobalConfig.XML_URL);

			if (filePath != null) {
				File file = new File(filePath);
				inputStream = new FileInputStream(file);
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(inputStream);
				Element element = (Element) document.getDocumentElement();
				NodeList firstNodes = ((org.w3c.dom.Element) element).getElementsByTagName("course");
				for (int i = 0; i < firstNodes.getLength(); i++) {
					Element bookElement = (Element) firstNodes.item(i);
					Course course1 = new Course();
					int id = Integer.parseInt(((org.w3c.dom.Element) bookElement).getAttribute("id"));
					String name = ((org.w3c.dom.Element) bookElement).getAttribute("name");
					course1.setId(id);
					course1.setName(name);
					courseList.add(course1);
					courseTypeList.add(course1);
					NodeList secondNodes = bookElement.getElementsByTagName("chapter");
					// System.out.println("*****"+childNodes.getLength());
					for (int j = 0; j < secondNodes.getLength(); j++) {
						Element secondElement = (Element) secondNodes.item(j);
						Course course2 = new Course();
						int id2 = Integer.parseInt(((org.w3c.dom.Element) secondElement).getAttribute("id"));
						String name2 = ((org.w3c.dom.Element) secondElement).getAttribute("name");
						course2.setId(id2);
						course2.setName(name2);
						course2.setFkCourse(course1);
						courseChapeterList.add(course2);
						courseList.add(course2);
						NodeList thirdChildNodes = secondElement.getElementsByTagName("section");
						for (int f = 0; f < thirdChildNodes.getLength(); f++) {
							Course course3 = new Course();
							Element thirdElement = (Element) thirdChildNodes.item(f);
							NodeList fourChildNodes = thirdChildNodes.item(f).getChildNodes();
							for (int m = 0; m < fourChildNodes.getLength(); m++) {
								if (fourChildNodes.item(m).getNodeName().toString().equals("name")) {
									String name3 = fourChildNodes.item(m).getTextContent();
									course3.setName(name3);
								} else if (fourChildNodes.item(m).getNodeName().toString().equals("url")) {
									String url = fourChildNodes.item(m).getTextContent();
									course3.setUrl(url);
								} else if (fourChildNodes.item(m).getNodeName().toString().equals("desc")) {
									String desc = fourChildNodes.item(m).getTextContent();
									course3.setDesc(desc);
								} else if (fourChildNodes.item(m).getNodeName().toString().equals("length")) {
									String length = fourChildNodes.item(m).getTextContent();
									course3.setLength(length);
								}
							}
							int id3 = Integer.parseInt(((org.w3c.dom.Element) thirdElement).getAttribute("id"));
							course3.setId(id3);
							course3.setFkCourse(course2);
							courseSectionList.add(course3);
							courseList.add(course3);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return courseList;
	}

	public List<Course> getCourseList()
	{
		return courseList;
	}

	public void setCourseList(List<Course> courseList)
	{
		this.courseList = courseList;
	}

	public List<Course> getCourseTypeList()
	{
		return courseTypeList;
	}

	public void setCourseTypeList(List<Course> courseTypeList)
	{
		this.courseTypeList = courseTypeList;
	}

	public List<Course> getCourseChapeterList()
	{
		return courseChapeterList;
	}

	public void setCourseChapeterList(List<Course> courseChapeterList)
	{
		this.courseChapeterList = courseChapeterList;
	}

	public List<Course> getCourseSectionList()
	{
		return courseSectionList;
	}

	public void setCourseSectionList(List<Course> courseSectionList)
	{
		this.courseSectionList = courseSectionList;
	}

}
