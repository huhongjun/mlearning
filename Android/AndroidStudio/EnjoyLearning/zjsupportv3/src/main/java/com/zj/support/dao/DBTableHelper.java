package com.zj.support.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 实体反射（用于创建表时）
 *
 */
public class DBTableHelper {

	public static List<String> entityToString(String[] classNames) {
		List<String> createTableStrings = null;
		if (classNames != null) {
			createTableStrings = new ArrayList<String>();
			int length = classNames.length;
			for (int i = 0; i < length; i++) {
				String entityName = classNames[i];
				try {
					Object obj = Class.forName(entityName).newInstance();
					// 获得类名
					String className = entityName.substring(
							entityName.lastIndexOf('.') + 1,
							entityName.length());
					String createSql = createTable(className, obj);
					if (createSql != null && !createSql.equals("")) {
						createTableStrings.add(createSql);
					}
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return createTableStrings;
	}

	private static String createTable(String className, Object entity) {
		StringBuffer buffer = new StringBuffer("CREATE Table ");
		buffer.append(className.toLowerCase()).append("(");
		Class cls = entity.getClass();
		Field[] fields = cls.getDeclaredFields();
		int fieldsNumber = fields.length;
		for (int i = 0; i < fieldsNumber; i++) {
			Object type = fields[i].getType();
			String name = fields[i].getName().toString();
			try {
				if (!name.equals("serialVersionUID")) {
					String fieldType = "";
					if (type instanceof Date
							|| type.toString().equals("class java.util.Date")) {
						fieldType = "DATETIME";
						buffer.append(name).append(" ").append(fieldType)
								.append(",");
					} else if (type instanceof String
							|| type.toString().equals("class java.lang.String")) {
						fieldType = "varchar";
						buffer.append(name).append(" ").append(fieldType)
								.append(",");
					} else if (type instanceof Integer
							|| type.toString()
									.equals("class java.lang.Integer")
							|| type.toString().equals("int")) {
						fieldType = "INTEGER";
						buffer.append(name).append(" ").append(fieldType)
								.append(",");
					} else if (type.toString().equals("long")
							|| type instanceof Long) {
						fieldType = "long";
						buffer.append(name).append(" ").append(fieldType)
								.append(",");
					} else if (type.toString().equals("boolean")) {
						fieldType = "bit";
						buffer.append(name).append(" ").append(fieldType)
								.append(",");
					} else if (type.toString().equals(
							"class java.math.BigDecimal")) {
						fieldType = "bigDecimal";
						buffer.append(name).append(" ").append(fieldType)
								.append(",");
					} else {
						fieldType = "INTEGER";
						buffer.append(name).append(" ").append(fieldType)
								.append(",");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		buffer = buffer.delete(buffer.length() - 1, buffer.length());
		buffer.append(")");
		return buffer.toString();
	}
}
