package com.zhijin.mlearning.support.sqlite;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zhijin.mlearning.app.CoreApp;
import com.zhijin.mlearning.app.model.LearningProgress;
import com.zhijin.mlearning.support.util.ClassType;
import com.zhijin.mlearning.support.util.DateFormatMethod;
import com.zhijin.mlearning.support.util.MyToast;
import com.zhijin.mlearning.support.validate.ValidateUtil;

public class SqliteUtil
{

	private static SqliteUtil mInstance;
	DbHelper db;
	SQLiteDatabase sqlite;
	ObjectOptionUtil objOptionUtil;
	Object idType = null;

	// StoreOperation storeOperation;

	private SqliteUtil()
	{
		db = DbHelper.getInstance();
		objOptionUtil = new ObjectOptionUtil();
		// storeOperation = StoreOperation.getInstance();
	}

	/***
	 * 得到当前用户本地的学习记录
	 * 
	 * @return 学习进度列表
	 */
	public List<LearningProgress> getLocalProgressList()
	{
		String memberId = CoreApp.getApp().getMemberId();
		String sql = "from com.zhijin.mlearning.app.model.LearningProgress where memberId = " + "'" + memberId + "'";
		List<Object> list = selectDomain(sql, LearningProgress.class, false);
		if (list != null) {
			return objctListToItemSingleList(list);
		} else {
			return null;
		}
	}

	/***
	 * 将泛型为Object的List的泛型变成泛型为LearningProgress的List
	 * 
	 * @param list
	 *            泛型为Object的List
	 * @return List<LearningProgress>
	 */
	public List<LearningProgress> objctListToItemSingleList(List<Object> list)
	{
		List<LearningProgress> learningProgressList = new ArrayList<LearningProgress>();
		for (Iterator<Object> i = list.iterator(); i.hasNext();) {
			learningProgressList.add((LearningProgress) i.next());
		}
		return learningProgressList;

	}

	public synchronized static SqliteUtil getInstance()
	{
		if (mInstance == null) {
			mInstance = new SqliteUtil();
		}
		return mInstance;
	}

	public synchronized boolean saveOrUpdateDomain(Object entity)
	{
		Object value = objOptionUtil.getFieldVlaue(entity, "id");
		if (value != null && !value.equals(0)) {
			return updateDomain(entity, Integer.valueOf(value.toString()));
		} else {
			return saveDomain(entity, false);
		}
	}

	/**
	 * 保存某个对象
	 * 
	 * @param entity
	 *            保存的对象
	 * @return 返回是否保存成功
	 */
	public synchronized boolean saveDomain(Object entity, boolean isUpload)
	{
		boolean validateResult = ValidateUtil.validateObj(entity);
		Log.i("info", "validateResult:" + validateResult);
		if (!validateResult) {
			Log.i("info", "entity:" + entity);
			MyToast.showToast(entity);
			return validateResult;
		}
		int id = -1;
		sqlite = db.getWritableDatabase();
		sqlite.beginTransaction();
		ContentValues values = new ContentValues();
		Class cls = entity.getClass();
		String entityName = entity.getClass().getName();
		// 获得类名
		String className = entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
		Field[] fields = cls.getDeclaredFields();
		int fieldsNumber = fields.length;
		for (int i = 0; i < fieldsNumber; i++) {
			Field field = fields[i];
			Object type = field.getType();
			try {
				String name = fields[i].getName().toString();
				Log.i("info", "name:" + name);
				if (ClassType.isBasyType(type)) {
					if (!name.equals("id")) {
						if (name.equals("serialVersionUID")) {
							continue;
						} else {
							Object value = null;
							try {
								value = objOptionUtil.getFieldVlaue(entity, field.getName());
								// value = field.get(entity);
							} catch (Exception e) {
								Log.e("error", "value:" + value + ",field:" + field);
							}
							if (type instanceof Date || type.toString().equals("class java.util.Date")) {
								DateFormatMethod dateForMat = new DateFormatMethod();
								String dateValue = dateForMat.formatDate((Date) value);
								values.put(name, value == null ? "" : dateValue);
								System.out.println("Date==" + values.get(name));
							} else if (type instanceof String || type.toString().equals("class java.lang.String")) {
								values.put(name, value == null ? "" : value.toString());
							} else if (type instanceof Integer || type.toString().equals("class java.lang.Integer")
									|| type.toString().equals("int")) {
								if (value == null) {
									value = 0;
								} else {
									values.put(name, Integer.valueOf(value.toString()));
								}
							} else if (type.toString().equals("long") || type instanceof Long) {
								values.put(name, Long.valueOf(value.toString()));
							} else if (type.toString().equals("interface java.util.Map")) {
								Log.d("info", "map:" + type + ",value：" + value);
								// values.put(name, value.toString());
							} else if (type.toString().equals("boolean")) {
								if (value != null) {
									boolean valueFlag = Boolean.valueOf(value.toString());
									if (valueFlag) {
										value = 1;
									} else {
										value = 0;
									}
									values.put(name, Integer.valueOf(value.toString()));
								}
							} else if (type.toString().equals("class java.math.BigDecimal")) {
								values.put(name, value.toString());
							} else {
								// System.out.println("type==" + type);
							}
						}
					} else {
						idType = type;
						Object value = objOptionUtil.getFieldVlaue(entity, field.getName());
						if (value != null && Integer.valueOf(value.toString()) > 0) {
							values.put(name, Integer.valueOf(value.toString()));
							id = Integer.valueOf(value.toString());
						} else {
							UUID uuid = UUID.randomUUID();
							id = uuid.hashCode();
							if (id < 0) {
								id = Math.abs(id);
							}
							values.put(name, id);
						}
					}
				} else {
					Object value = objOptionUtil.getFieldVlaue(entity, field.getName());
					if (value != null) {
						String sFkId = objOptionUtil.getFieldVlaue(value, "id").toString();
						if (sFkId != null) {
							int iFkId = Integer.valueOf(sFkId);
							try {
								values.put(name, iFkId);
							} catch (Exception e) {
								Log.e("error", "fkId:" + e.toString());
							}
						}
					}
				}
			} catch (IllegalArgumentException e) {
				Log.i("info", "IllegalArgumentException:" + e.toString());
			}
		}
		long result = sqlite.insert(className.toLowerCase(), "", values);
		Log.d("info", "result:" + result);
		long logResult = -1;
		if (isUpload) {
			ContentValues uplodValues = new ContentValues();
			uplodValues.put("id", id);
			uplodValues.put("objName", entityName);
			uplodValues.put("errorTime", 0);
			logResult = sqlite.insert("uploadlog", "", uplodValues);
			if (logResult > 0 && result > 0) {
				sqlite.setTransactionSuccessful();
				sqlite.endTransaction();
			} else {
				sqlite.endTransaction();
				return false;
			}
		}
		if (result > 0) {
			if (!isUpload) {
				sqlite.setTransactionSuccessful();
				sqlite.endTransaction();
			}
			// set方法
			Method fkMethod = null;
			try {
				fkMethod = cls.getDeclaredMethod("setId", int.class);
			} catch (NoSuchMethodException e) {
				try {
					fkMethod = cls.getDeclaredMethod("setId", Integer.class);
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				}
				Log.e("error", "NoSuchMethodException:" + e.toString());
			}
			try {
				fkMethod.invoke(entity, id);
				sqlite.close();
				return true;
			} catch (IllegalArgumentException e) {
				Log.e("error", "IllegalArgumentException:" + e.toString());
			} catch (IllegalAccessException e) {
				Log.e("error", "IllegalAccessException:" + e.toString());
			} catch (InvocationTargetException e) {
				Log.e("error", "InvocationTargetException:" + e.toString());
			}
		}

		sqlite.close();
		return false;
	}

	private synchronized boolean saveDomainList(List list)
	{
		sqlite = db.getWritableDatabase();
		// 开始事务处理
		sqlite.beginTransaction();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			boolean result = this.saveDomain(obj, sqlite);// 单个保存
			if (!result) {
				sqlite.endTransaction();
				sqlite.close();
				return false;
			}
		}
		if (sqlite.isOpen()) {
			Log.d("info", "sqlite is open");
			sqlite.setTransactionSuccessful();
			sqlite.endTransaction();
			sqlite.close();
			return true;
		} else {
			Log.d("info", "sqlite not open");
			sqlite.endTransaction();
			sqlite.close();
			return false;
		}
	}

	/**
	 * 保存某个对象
	 * 
	 * @param entity
	 *            保存的对象
	 * @param sqlite
	 *            打开的数据库对象
	 * @return 返回是否保存成功
	 */
	private synchronized boolean saveDomain(Object entity, SQLiteDatabase sqlite)
	{
		boolean validateResult = ValidateUtil.validateObj(entity);
		Log.i("info", "validateResult:" + validateResult);
		if (!validateResult) {
			Log.i("info", "entity:" + entity);
			MyToast.showToast(entity);
			return validateResult;
		}
		int id = -1;
		ContentValues values = new ContentValues();
		Class cls = entity.getClass();
		String entityName = entity.getClass().getName();
		// 获得类名
		String className = entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
		Field[] fields = cls.getDeclaredFields();
		int fieldsNumber = fields.length;
		for (int i = 0; i < fieldsNumber; i++) {
			Field field = fields[i];
			Object type = field.getType();
			try {
				String name = fields[i].getName().toString();
				if (ClassType.isBasyType(type)) {
					if (!name.equals("id")) {
						if (name.equals("serialVersionUID")) {
							continue;
						} else {
							Object value = objOptionUtil.getFieldVlaue(entity, field.getName());
							if (type instanceof Date || type.toString().equals("class java.util.Date")) {
								values.put(name, value == null ? "" : value.toString());
								// System.out.println("Date=="
								// + values.get("name"));
							} else if (type instanceof String || type.toString().equals("class java.lang.String")) {
								values.put(name, value == null ? "" : value.toString());
							} else if (type instanceof Integer || type.toString().equals("class java.lang.Integer")
									|| type.toString().equals("int")) {
								if (value == null) {
									value = 0;
								} else {
									values.put(name, Integer.valueOf(value.toString()));
								}
							} else if (type.toString().equals("long") || type instanceof Long) {
								values.put(name, Long.valueOf(value.toString()));
							} else if (type.toString().equals("boolean")) {
								if (value != null) {
									boolean valueFlag = Boolean.valueOf(value.toString());
									if (valueFlag) {
										value = 1;
									} else {
										value = 0;
									}
									values.put(name, Integer.valueOf(value.toString()));
								}
							} else if (type.toString().equals("class java.math.BigDecimal")) {
								values.put(name, value.toString());
							} else {
								// System.out.println("type==" + type);
							}
						}
					} else {
						idType = type;
						Object value = objOptionUtil.getFieldVlaue(entity, field.getName());
						if (value != null && Integer.valueOf(value.toString()) > 0) {
							values.put(name, Integer.valueOf(value.toString()));
							id = Integer.valueOf(value.toString());
						} else {
							UUID uuid = UUID.randomUUID();
							id = uuid.hashCode();
							if (id < 0) {
								id = Math.abs(id);
							}
							values.put(name, id);
							// System.out.println("id==" + id);
						}
					}
				} else {
					Object value = objOptionUtil.getFieldVlaue(entity, field.getName());
					if (value != null) {
						String sFkId = objOptionUtil.getFieldVlaue(value, "id").toString();
						if (sFkId != null) {
							int iFkId = Integer.valueOf(sFkId);
							try {
								values.put(name, iFkId);
							} catch (Exception e) {
								Log.e("error", "fkId:" + e.toString());
							}
						}
					}
				}
			} catch (IllegalArgumentException e) {
				Log.e("error", "IllegalArgumentException:" + e.toString());
			}
		}
		long result = sqlite.insert(className.toLowerCase(), "", values);
		ContentValues uplodValues = new ContentValues();
		uplodValues.put("id", id);
		uplodValues.put("objName", entityName);
		uplodValues.put("errorTime", 0);
		long logResult = sqlite.insert("uploadlog", "", uplodValues);
		if (result > -1) {
			// set方法
			Method fkMethod = null;
			try {
				// System.out.println("cls==" + cls + ",idType=" + idType);
				fkMethod = cls.getDeclaredMethod("setId", int.class);
			} catch (NoSuchMethodException e) {
				try {
					fkMethod = cls.getDeclaredMethod("setId", Integer.class);
				} catch (SecurityException e1) {
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					e1.printStackTrace();
				}
				Log.e("error", "NoSuchMethodException:" + e.toString());
			}
			try {
				fkMethod.invoke(entity, id);
				return true;
			} catch (IllegalArgumentException e) {
				Log.e("error", "IllegalArgumentException:" + e.toString());
			} catch (IllegalAccessException e) {
				Log.e("error", "IllegalAccessException:" + e.toString());
			} catch (InvocationTargetException e) {
				Log.e("error", "InvocationTargetException:" + e.toString());
			}
		}
		if (logResult > -1 && result > -1) {
			return true;
		} else {
			return false;
		}
	}

	/***
	 * 根据某个对象的id删除记录
	 * 
	 * @param entity
	 *            对象实例
	 * @param id
	 *            id
	 * @return 返回删除的对象id
	 */
	public synchronized int deleteDomain(Object entity, int id)
	{
		sqlite = db.getWritableDatabase();
		Class cls = entity.getClass();
		String entityName = entity.getClass().getName();
		// 获得类名
		String className = entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
		String sql = "DELETE FROM " + className.toLowerCase() + " where id = " + id;
		sqlite.execSQL(sql);
		sqlite.close();
		return id;
	}

	/**
	 * 删除某类的缓存
	 * 
	 * @param classname
	 *            最好是全类名
	 */
	public void deleteByClassName(String classname)
	{
		String sql = "DELETE FROM cacheurlinfo where url LIKE '%" + classname + "%'";
		this.deleteSql(sql);
	}

	/***
	 * 删除数据库
	 * 
	 */
	public synchronized void deleteAll(Context context)
	{
		Properties props = new Properties();
		try {
			props.load(context.getAssets().open("classes.properties"));
			String strValue = props.getProperty("className");
			if (!strValue.equals("")) {
				String[] classNames = strValue.split(",");
				for (int i = 0; i < classNames.length; i++) {
					String entityName = classNames[i];
					// 获得类名
					String className = entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
					if (className.equals("MiAttachmentFile")) {
						continue;
					} else {
						System.out.println("className:" + className);
						deleteTable(className);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void deleteTable(String tableName)
	{
		if (tabbleIsExist(tableName.toLowerCase())) {
			String sql = "delete from " + tableName.toLowerCase();
			deleteSql(sql);
		}
	}

	/**
	 * 判断某张表是否存在
	 * 
	 * @param tabName
	 *            表名
	 * @return
	 */
	public boolean tabbleIsExist(String tableName)
	{
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		Cursor cursor = null;
		try {
			sqlite = db.getWritableDatabase();
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
			cursor = sqlite.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
			sqlite.close();
		} catch (Exception e) {
		}
		return result;
	}

	/***
	 * 执行某个删除sql
	 * 
	 */
	public synchronized void deleteSql(String sql)
	{
		sqlite = db.getWritableDatabase();
		sqlite.execSQL(sql);
		sqlite.close();
	}

	/**
	 * 更新数据库数据
	 * 
	 * @param entity
	 *            需要更新的entity
	 * @return 更新完之后的id，即原始id
	 */
	public synchronized boolean updateDomain(Object entity, int id)
	{
		boolean validateResult = ValidateUtil.validateObj(entity);
		Log.i("info", "validateResult:" + validateResult);
		if (!validateResult) {
			Log.i("info", "entity:" + entity);
			MyToast.showToast(entity);
			return validateResult;
		}
		sqlite = db.getWritableDatabase();
		ContentValues values = new ContentValues();
		Class cls = entity.getClass();
		String entityName = entity.getClass().getName();
		// 获得类名
		String className = entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
		Field[] fields = cls.getDeclaredFields();
		int fieldsNumber = fields.length;
		for (int i = 0; i < fieldsNumber; i++) {
			Field field = fields[i];
			Object type = field.getType();
			try {
				String name = fields[i].getName().toString();
				if (ClassType.isBasyType(type)) {
					if (!name.equals("id")) {
						if (name.equals("serialVersionUID")) {
							continue;
						} else {
							Object value = objOptionUtil.getFieldVlaue(entity, field.getName());
							if (type instanceof Date || type.toString().equals("class java.util.Date")) {
								values.put(name, value == null ? "" : value.toString());
								// System.out.println("Date=="
								// + values.get("name"));
							} else if (type instanceof String || type.toString().equals("class java.lang.String")) {
								values.put(name, value == null ? "" : value.toString());
							} else if (type instanceof Integer || type.toString().equals("class java.lang.Integer")
									|| type.toString().equals("int")) {
								Log.d("info", "IntegerValue:" + value);
								if (value == null) {
									value = 0;
								} else {
									values.put(name, Integer.valueOf(value.toString()));
								}
							} else if (type.toString().equals("long") || type instanceof Long) {
								values.put(name, Long.valueOf(value.toString()));
							} else if (type.toString().equals("boolean")) {
								if (value != null) {
									boolean valueFlag = Boolean.valueOf(value.toString());
									if (valueFlag) {
										value = 1;
									} else {
										value = 0;
									}
									values.put(name, Integer.valueOf(value.toString()));
								}
							} else if (type.toString().equals("class java.math.BigDecimal")) {
								values.put(name, value.toString());
							} else if (type.toString().equals("interface java.util.Map")) {
								Log.d("info", "map:" + type);
								// values.put(name, value.toString());
							} else {
								if (value != null) {
									String sFkId = objOptionUtil.getFieldVlaue(value, "id").toString();
									if (sFkId != null) {
										int iFkId = Integer.valueOf(sFkId);
										try {
											values.put(name, iFkId);
										} catch (Exception e) {
											Log.e("error", "fkId:" + e.toString());
										}
									}
								}

							}
						}
					} else {
						idType = type;
						Object value = objOptionUtil.getFieldVlaue(entity, field.getName());
						if (value != null && Integer.valueOf(value.toString()) > 0) {
							System.out.println("Id有值");
							values.put(name, Integer.valueOf(value.toString()));
							// id = Integer.valueOf(value.toString());
						} else {
							System.out.println("Id没值");
							UUID uuid = UUID.randomUUID();
							id = uuid.hashCode();
							if (id < 0) {
								id = Math.abs(id);
							}
							values.put(name, id);
						}
					}
				} else {
					Object value = objOptionUtil.getFieldVlaue(entity, field.getName());
					if (value != null) {
						String sFkId = objOptionUtil.getFieldVlaue(value, "id").toString();
						if (sFkId != null) {
							int iFkId = Integer.valueOf(sFkId);
							try {
								values.put(name, iFkId);
							} catch (Exception e) {
								Log.e("error", "fkId:" + e.toString());
							}
						}
					}
				}
			} catch (IllegalArgumentException e) {
				Log.e("error", "IllegalArgumentException:" + e.toString());
			}
		}
		String wheres = "id" + "=?";
		String whereId = String.valueOf(id);
		String[] whereArgs = new String[] { whereId };
		int result = sqlite.update(className.toLowerCase(), values, wheres, whereArgs);
		System.out.println("values:" + values);
		Log.d("info", "updateResult:" + result);
		if (result > 0) {
			sqlite.close();
			return true;
		}
		sqlite.close();
		return false;
	}

	/**
	 * 对某个对象的查询（isUpload=true,需要在子线程）
	 * 
	 * @param sql
	 *            查询语句
	 * @param cls
	 *            查询的对象
	 * @return 查询得到的对象列表
	 */
	public synchronized List<Object> selectDomain(String sql, Class cls, boolean isUpload)
	{
		String entityName = cls.getName();
		// 获得类名
		String className = entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
		Field[] fields = cls.getDeclaredFields();
		List<Object> resultList = new ArrayList<Object>();
		// System.out.println("sql==" + sql);
		StringBuffer selectSql = new StringBuffer("select * from ");
		selectSql.append(className.toLowerCase());
		if (sql.contains("where")) {
			int index = sql.indexOf("where");
			sql = sql.substring(index);
			selectSql.append(" ");
			selectSql.append(sql);
		}
		if (db == null) {
			db = DbHelper.getInstance();
		}
		sqlite = db.getWritableDatabase();
		Cursor cursor = sqlite.rawQuery(selectSql.toString(), null);
		if (cursor != null) {
			// 遍历cursor，并将其数据添加至Map中，并返回。
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				try {
					Object obj = cls.newInstance();
					Map<String, Object> map = new IdentityHashMap<String, Object>();
					for (int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						Object type = field.getType();
						String name = field.getName();
						Log.i("info", "fieldName:" + name);
						if (name.equals("serialVersionUID")
								|| type.toString().equals("interface org.aspectj.lang.JoinPoint$StaticPart")) {
							continue;
						}
						String fieldName = name.substring(0, 1).toUpperCase() + name.substring(1);
						// set方法
						Method method = cls.getDeclaredMethod("set" + fieldName, field.getType());
						int number = cursor.getColumnIndex(name);
						if (number >= 0) {
							if (type instanceof Date || type.toString().equals("class java.util.Date")) {
								DateFormatMethod dateForMat = new DateFormatMethod();
								String value = cursor.getString(number);
								Date date = dateForMat.getDateByStrToYMD(value);
								method.invoke(obj, date);
							} else if (type.toString().equals("class java.lang.String") || type instanceof String) {
								String value = cursor.getString(number);
								// System.out.println("valuettttt===" + value);
								method.invoke(obj, value);
							} else if (type instanceof Integer || type.toString().equals("class java.lang.Integer")
									|| type.toString().equals("int")) {
								Integer value = cursor.getInt(number);
								method.invoke(obj, value);
							} else if (type.toString().equals("long") || type instanceof Long) {
								Long value = cursor.getLong(number);
								method.invoke(obj, value);
							} else if (type.toString().equals("boolean")) {
								int value = cursor.getInt(number);
								if (value == 0) {
									method.invoke(obj, false);
								} else if (value == 1) {
									method.invoke(obj, true);
								}
							} else if (type.toString().equals("class java.math.BigDecimal")) {
								Double doubleValue = cursor.getDouble(number);
								BigDecimal value = BigDecimal.valueOf(doubleValue);
								method.invoke(obj, value);
							} else if (type.toString().equals("interface java.util.Map")) {
								Log.d("info", "map:" + type);
							} else {
								Object fkObj = Class.forName(field.getType().getName()).newInstance();
								objOptionUtil.setFieldVlaue(cursor.getInt(number), fkObj, "id");
								method.invoke(obj, fkObj);
							}
						}
					}
					resultList.add(obj);
				} catch (Exception e) {
					Log.e("error", "error:" + e.toString());
				}
			}
		} else {
			// storeOperation.showDomain(entityName, id, isLoad, isUpdate)
		}
		cursor.close();
		sqlite.close();
		return resultList;
	}

	/**
	 * 对某个对象的查询（isUpload=true,需要在子线程）
	 * 
	 * @param sql
	 *            查询语句
	 * @param cls
	 *            查询的对象
	 * @return 查询得到的对象列表
	 */
	public synchronized Object showDomain(Class cls, int id)
	{
		String entityName = cls.getName();
		// 获得类名
		String className = entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
		Field[] fields = cls.getDeclaredFields();
		StringBuffer selectSql = new StringBuffer("select * from ");
		selectSql.append(className.toLowerCase());
		selectSql.append(" where id = ");
		selectSql.append(id);
		sqlite = db.getWritableDatabase();
		Cursor cursor = sqlite.rawQuery(selectSql.toString(), null);
		if (cursor != null) {
			// 遍历cursor，并将其数据添加至Map中，并返回。
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
				try {
					Object obj = cls.newInstance();
					Map<String, Object> map = new IdentityHashMap<String, Object>();
					for (int i = 0; i < fields.length; i++) {
						Field field = fields[i];
						Object type = field.getType();
						String name = field.getName();
						Log.i("info", "fieldName:" + name);
						if (name.equals("serialVersionUID")) {
							continue;
						}
						String fieldName = name.substring(0, 1).toUpperCase() + name.substring(1);
						// set方法
						Method method = cls.getDeclaredMethod("set" + fieldName, field.getType());
						int number = cursor.getColumnIndex(name);
						if (number >= 0) {
							if (type instanceof Date || type.toString().equals("class java.util.Date")) {
								DateFormatMethod dateForMat = new DateFormatMethod();
								String value = cursor.getString(number);
								Date date = dateForMat.getDateByStrToYMD(value);
								method.invoke(obj, date);
							} else if (type.toString().equals("class java.lang.String") || type instanceof String) {
								String value = cursor.getString(number);
								// System.out.println("valuettttt===" + value);
								method.invoke(obj, value);
							} else if (type instanceof Integer || type.toString().equals("class java.lang.Integer")
									|| type.toString().equals("int")) {
								Integer value = cursor.getInt(number);
								method.invoke(obj, value);
							} else if (type.toString().equals("long") || type instanceof Long) {
								Long value = cursor.getLong(number);
								method.invoke(obj, value);
							} else if (type.toString().equals("boolean")) {
								int value = cursor.getInt(number);
								if (value == 0) {
									method.invoke(obj, false);
								} else if (value == 1) {
									method.invoke(obj, true);
								}
							} else if (type.toString().equals("class java.math.BigDecimal")) {
								Double doubleValue = cursor.getDouble(number);
								BigDecimal value = BigDecimal.valueOf(doubleValue);
								method.invoke(obj, value);
							} else if (type.toString().equals("interface java.util.Map")) {
								Log.d("info", "map:" + type);
							} else {
								Object fkObj = Class.forName(field.getType().getName()).newInstance();
								objOptionUtil.setFieldVlaue(cursor.getInt(number), fkObj, "id");
								method.invoke(obj, fkObj);
							}
						}
					}
					return obj;
				} catch (Exception e) {
					Log.e("error", "error:" + e.toString());
				}
			}
		} else {
			// Object obj = storeOperation.showDomain(entityName, id, true,
			// false);
			// if (obj != null) {
			// saveDomain(obj, false);
			// return obj;
			// }
		}
		cursor.close();
		sqlite.close();
		return null;
	}

	/**
	 * 对属性的查询
	 * 
	 * @param sql
	 *            查询语句
	 * @param cls
	 *            查询的对象
	 * @return 属性集合的列表
	 */
	public List executeSelect(String sql, Class cls)
	{
		List list = new ArrayList();
		int from = sql.indexOf("from");
		int where = sql.indexOf("where");
		String fromSql = sql.substring(0, from);
		String whereSql = sql.substring(where, sql.length());
		StringBuffer buffer = new StringBuffer(fromSql);
		String entityName = cls.getName();
		// 获得类名
		String className = entityName.substring(entityName.lastIndexOf('.') + 1, entityName.length());
		buffer.append(" from ").append(className.toLowerCase()).append(" ").append(whereSql);
		String selectSql = buffer.toString();
		// System.out.println("selectSql==" + selectSql);
		sqlite = db.getWritableDatabase();
		Cursor cursor = sqlite.rawQuery(selectSql.toString(), null);
		Field[] fields = cls.getDeclaredFields();
		// 遍历cursor，并将其数据添加至Map中，并返回。
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			try {
				Object obj = cls.newInstance();
				Map<String, Object> map = new IdentityHashMap<String, Object>();
				for (int i = 0; i < fields.length; i++) {
					Field field = fields[i];
					Object type = field.getType();
					String name = field.getName();
					if (name.equals("serialVersionUID")) {
						continue;
					}
					String fieldName = name.substring(0, 1).toUpperCase() + name.substring(1);
					// set方法
					Method method = cls.getDeclaredMethod("set" + fieldName, field.getType());
					int number = cursor.getColumnIndex(name);
					if (number >= 0) {
						// System.out.println("name==" + name);
						if (type instanceof Date || type.toString().equals("class java.util.Date")) {
							DateFormatMethod dateForMat = new DateFormatMethod();
							String value = cursor.getString(number);
							Date date = dateForMat.getDateByStrToYMD(value);
							map.put(name, date);
						} else if (type.toString().equals("class java.lang.String") || type instanceof String) {
							String value = cursor.getString(number);
							map.put(name, value);
						} else if (type instanceof Integer || type.toString().equals("class java.lang.Integer")
								|| type.toString().equals("int")) {
							Integer value = cursor.getInt(number);
							map.put(name, value);
						} else if (type.toString().equals("long") || type instanceof Long) {
							Long value = cursor.getLong(number);
							map.put(name, value);
						} else if (type.toString().equals("boolean")) {
							int value = cursor.getInt(number);
							if (value == 0) {
								map.put(name, false);
							} else if (value == 1) {
								map.put(name, true);
							}
						} else if (type.toString().equals("class java.math.BigDecimal")) {
							Double doubleValue = cursor.getDouble(number);
							BigDecimal value = BigDecimal.valueOf(doubleValue);
							map.put(name, value);
						} else if (type.toString().equals("interface java.util.Map")) {
							Log.d("info", "map:" + type);
							// values.put(name, value.toString());
						} else {
							// set方法
							Method fkMethod = cls.getDeclaredMethod("setId", Integer.class);
							Integer objId = cursor.getInt(number);
							fkMethod.invoke(type, objId);
							map.put(name, type);
							// System.out.println("type==" + type);
						}
					}
				}
				list.add(map);
			} catch (Exception e) {
				Log.e("error", "error:" + e.toString());
			}
		}
		cursor.close();
		sqlite.close();
		return list;
	}

	// public synchronized List<Object> selectUploadDomainList() {
	// List<Object> list = selectDomain(
	// "from com.andrnes.entity.sqlitebean.UploadLog",
	// UploadLog.class, true);
	// if (list != null && list.size() > 0) {
	// return list;
	// } else {
	// return null;
	// }
	// }

	// public synchronized List<Object> selectUpLoadDomain(UploadLog uploadLog)
	// {
	// List<Object> uploadDomainList = new ArrayList<Object>();
	// int objId = uploadLog.getId();
	// String tableName = uploadLog.getObjName();
	// // 获得类名
	// String className = tableName.substring(tableName.lastIndexOf('.') + 1,
	// tableName.length());
	// try {
	// Class cls = Class.forName(tableName);
	// List<Object> domainList;
	// try {
	// domainList = selectDomain("from " + tableName + " where id = "
	// + objId, cls.newInstance().getClass(), true);
	// if (domainList != null && domainList.size() > 0) {
	// for (int j = 0; j < domainList.size(); j++) {
	// uploadDomainList.add(domainList.get(j));
	// }
	// return uploadDomainList;
	// } else {
	// return null;
	// }
	// } catch (InstantiationException e) {
	// Log.e("error", "InstantiationException:" + e.toString());
	// } catch (IllegalAccessException e) {
	// Log.e("error", "IllegalAccessException:" + e.toString());
	// }
	// } catch (ClassNotFoundException e) {
	// Log.e("error", "ClassNotFoundException:" + e.toString());
	// }
	// return null;
	// }
}
