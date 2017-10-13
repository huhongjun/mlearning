package com.zj.support.dao;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.zj.support.util.ClassTypeUtil;
import com.zj.support.util.EntityInflectUtil;
import com.zj.support.util.TimeUtil;

/**
 * 本地数据库操作Api（完全面向对象）--增删改查
 * 
 * @author XingRongJing
 * 
 */
public class SqliteApi {

	private static final String TAG = SqliteApi.class.getName();
	/** 对象id域名 **/
	private static final String FIELD_ID = "id";
	/** 对象序列化uid域名 **/
	private static final String FIELD_SVUID = "serialVersionUID";

	private static SqliteApi mSqlApi;
	private DBHelper mDBHelper;

	private SqliteApi(Context ctx) {
		mDBHelper = DBHelper.getDBHelper(ctx.getApplicationContext());
	}

	public static SqliteApi getSqliteApi(Context ctx) {
		if (null == mSqlApi) {
			mSqlApi = new SqliteApi(ctx);
		}
		return mSqlApi;
	}

	public synchronized boolean saveOrUpdateEntity(Object entity) {
		if (null == entity) {
			return false;
		}
		// 获取实体的id值，存在为更新，不存在为插入
		Object value = EntityInflectUtil.getFieldVlaue(entity, FIELD_ID);
		if (null == value || value != null
				&& Integer.valueOf(value.toString()) == 0) {
			return this.saveEntity(entity);
		} else {
			return this.updateEntity(entity, Integer.valueOf(value.toString()));
		}
	}

	@SuppressWarnings("unchecked")
	private boolean saveEntity(Object entity) {
		SQLiteDatabase sqlite = mDBHelper.getWritableDatabase();
		try {
			sqlite.beginTransaction();
			ContentValues values = SqliteApi.getValuesByEntity(entity);
			Class<?> cls = entity.getClass();
			String entityName = entity.getClass().getName();
			String className = entityName.substring(
					entityName.lastIndexOf('.') + 1, entityName.length());
			long result = sqlite.insert(className.toLowerCase(), "", values);
			try {
				// 设置实体id
				Method fkMethod = cls.getDeclaredMethod("setId", int.class);
				int id = values.getAsInteger(FIELD_ID);
				fkMethod.invoke(entity, id);
				sqlite.setTransactionSuccessful();
				if (result > 0) {
					return true;
				}
			} catch (Exception e) {
				Method fkMethod = cls.getDeclaredMethod("setId", Integer.class);
				int id = values.getAsInteger(FIELD_ID);
				fkMethod.invoke(entity, id);
				sqlite.setTransactionSuccessful();
				if (result > 0) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlite.endTransaction();
			sqlite.close();
		}
		return false;
	}

	private boolean updateEntity(Object entity, int id) {
		SQLiteDatabase sqlite = mDBHelper.getWritableDatabase();
		try {
			sqlite.beginTransaction();
			ContentValues values = SqliteApi.getValuesByEntity(entity);
			Class<?> cls = entity.getClass();
			String entityName = entity.getClass().getName();
			String className = entityName.substring(
					entityName.lastIndexOf('.') + 1, entityName.length());
			String wheres = "id" + "=?";
			String whereId = String.valueOf(id);
			String[] whereArgs = new String[] { whereId };
			int result = sqlite.update(className.toLowerCase(), values, wheres,
					whereArgs);
			sqlite.setTransactionSuccessful();
			if (result > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sqlite.endTransaction();
			sqlite.close();
		}
		return false;
	}

	public synchronized boolean deleteEntity(Object entity) {
		if (null == entity) {
			return false;
		}
		SQLiteDatabase sqlite = mDBHelper.getWritableDatabase();
		try {
			// 获取实体的id值
			Object value = EntityInflectUtil.getFieldVlaue(entity, FIELD_ID);
			String entityName = entity.getClass().getName();
			// 获得类名
			String className = entityName.substring(
					entityName.lastIndexOf('.') + 1, entityName.length());
			String sql = "DELETE FROM " + className.toLowerCase()
					+ " where id = " + Integer.valueOf(value.toString());
			sqlite.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != sqlite) {
				sqlite.close();
			}
		}
		return false;
	}

	public synchronized List<Object> findEntities(String sql, Class<?> cls) {
		List<Object> entities = new ArrayList<Object>();
		SQLiteDatabase sqlite = mDBHelper.getReadableDatabase();
		Cursor cursor = null;
		try {
			String entityName = cls.getName();
			// 获得类名
			String className = entityName.substring(
					entityName.lastIndexOf('.') + 1, entityName.length());
			StringBuffer selectSql = new StringBuffer("select * from ");
			selectSql.append(className.toLowerCase());
			if (sql.contains("where")) {
				int index = sql.indexOf("where");
				sql = sql.substring(index);
				selectSql.append(" ");
				selectSql.append(sql);
			}
			cursor = sqlite.rawQuery(selectSql.toString(), null);
			if (null == cursor) {
				return entities;
			}
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				Object entity = cls.newInstance();
				this.setEntity(cls, entity, cursor);
				entities.add(entity);
			}
		} catch (Exception e) {
			Log.e(TAG, "shan-->" + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (null != cursor) {
					cursor.close();
				}
				if (null != sqlite) {
					sqlite.close();
				}
			} catch (Exception e) {
				Log.e(TAG, "shan-->" + e.getMessage());
				e.printStackTrace();
			}
		}
		return entities;
	}

	public boolean executeSql(String sql) {
		SQLiteDatabase sqlite = mDBHelper.getWritableDatabase();
		try {
			sqlite.execSQL(sql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != sqlite) {
				sqlite.close();
			}
		}
		return false;
	}

	/**
	 * 通过反射将一个实体的域名和值转为对应的键值对
	 * 
	 * @param entity
	 * @return
	 */
	private static ContentValues getValuesByEntity(Object entity) {
		ContentValues values = new ContentValues();
		Class<?> cls = entity.getClass();
		Field[] fields = cls.getDeclaredFields();
		int fieldsNumber = fields.length;
		int id = -1;
		for (int i = 0; i < fieldsNumber; i++) {
			Field field = fields[i];
			Object fieldType = field.getType();
			String fieldName = field.getName();
			try {
				// Switch 1：域为基本类型
				if (ClassTypeUtil.isBaseType(fieldType)) {
					// Switch 1.1：filed为id
					if (FIELD_ID.equals(fieldName)) {
						Object value = EntityInflectUtil.getFieldVlaue(entity,
								fieldName);
						if (value != null
								&& Integer.valueOf(value.toString()) > 0) {
							values.put(fieldName,
									Integer.valueOf(value.toString()));
							id = Integer.valueOf(value.toString());
						} else {
							UUID uuid = UUID.randomUUID();
							id = uuid.hashCode();
							if (id < 0) {
								id = Math.abs(id);
							}
							values.put(fieldName, id);
						}
						continue;
					}
					// Switch 1.2：filed为非id
					if (FIELD_SVUID.equals(fieldName)) {
						continue;
					}
					Object value = null;
					try {
						value = EntityInflectUtil.getFieldVlaue(entity,
								field.getName());
						// value = field.get(entity);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (fieldType instanceof Date
							|| fieldType.toString().equals(
									"class java.util.Date")) {
						String dateValue = TimeUtil.formatDate((Date) value);
						values.put(fieldName, value == null ? "" : dateValue);
					} else if (fieldType instanceof String
							|| fieldType.toString().equals(
									"class java.lang.String")) {
						values.put(fieldName,
								value == null ? "" : value.toString());
					} else if (fieldType instanceof Integer
							|| fieldType.toString().equals(
									"class java.lang.Integer")
							|| fieldType.toString().equals("int")) {
						if (value == null) {
							value = 0;
						} else {
							values.put(fieldName,
									Integer.valueOf(value.toString()));
						}
					} else if (fieldType.toString().equals("long")
							|| fieldType instanceof Long) {
						values.put(fieldName, Long.valueOf(value.toString()));
					} else if (fieldType.toString().equals(
							"interface java.util.Map")) {
						// values.put(name, value.toString());
					} else if (fieldType.toString().equals("boolean")) {
						if (value != null) {
							boolean valueFlag = Boolean.valueOf(value
									.toString());
							if (valueFlag) {
								value = 1;
							} else {
								value = 0;
							}
							values.put(fieldName,
									Integer.valueOf(value.toString()));
						}
					} else if (fieldType.toString().equals(
							"class java.math.BigDecimal")) {
						values.put(fieldName, value.toString());
					} else {

					}
					continue;
				}
			} catch (Exception e) {
				Log.e(TAG, "shan-->saveEntity(S1): " + e.getMessage());
			}
			// Switch 2：域为非基本类型
			Object value = EntityInflectUtil.getFieldVlaue(entity, fieldName);
			if (null == value) {
				continue;
			}
			// String sFkId = EntityInflectUtil.getFieldVlaue(value, FIELD_ID)
			// .toString();
			Object fkValue = EntityInflectUtil.getFieldVlaue(value, FIELD_ID);
			if (null == fkValue) {
				continue;
			}
			String sFkId = fkValue.toString();
			if (TextUtils.isEmpty(sFkId)) {
				continue;
			}
			try {
				int iFkId = Integer.valueOf(sFkId);
				values.put(fieldName, iFkId);
			} catch (Exception e) {
				Log.e(TAG, "shan-->saveEntity(S2): " + e.getMessage());
			}
		}
		return values;
	}

	private void setEntity(Class<?> cls, Object entity, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, InstantiationException,
			ClassNotFoundException {
		Field[] fields = cls.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Object type = field.getType();
			String name = field.getName();
			if (name.equals("serialVersionUID")
					|| type.toString().equals(
							"interface org.aspectj.lang.JoinPoint$StaticPart")) {
				continue;
			}
			String fieldName = name.substring(0, 1).toUpperCase()
					+ name.substring(1);
			// set方法
			Method method = null;
			try {
				method = cls.getDeclaredMethod("set" + fieldName,
						field.getType());
			} catch (NoSuchMethodException e) {
				// Log.e(TAG, "shan-->"+e.getMessage());
			}
			if (null == method) {
				continue;
			}
			int number = cursor.getColumnIndex(name);
			if (number >= 0) {
				if (type instanceof Date
						|| type.toString().equals("class java.util.Date")) {
					String value = cursor.getString(number);
					Date date = TimeUtil.getDateByStrToYMD(value);
					method.invoke(entity, date);
				} else if (type.toString().equals("class java.lang.String")
						|| type instanceof String) {
					String value = cursor.getString(number);
					method.invoke(entity, value);
				} else if (type instanceof Integer
						|| type.toString().equals("class java.lang.Integer")
						|| type.toString().equals("int")) {
					Integer value = cursor.getInt(number);
					method.invoke(entity, value);
				} else if (type.toString().equals("long")
						|| type instanceof Long) {
					Long value = cursor.getLong(number);
					method.invoke(entity, value);
				} else if (type.toString().equals("boolean")) {
					int value = cursor.getInt(number);
					if (value == 0) {
						method.invoke(entity, false);
					} else if (value == 1) {
						method.invoke(entity, true);
					}
				} else if (type.toString().equals("class java.math.BigDecimal")) {
					Double doubleValue = cursor.getDouble(number);
					BigDecimal value = BigDecimal.valueOf(doubleValue);
					method.invoke(entity, value);
				} else if (type.toString().equals("interface java.util.Map")) {
				} else {
					// try{
					// Object fkObj = Class.forName(field.getType().getName())
					// .newInstance();
					// EntityInflectUtil.setFieldVlaue(cursor.getInt(number),
					// fkObj, FIELD_ID);
					// method.invoke(entity, fkObj);
					// }catch(Exception e){
					// Log.e(TAG, "shan-->"+e.getMessage());
					// }
				}
			}
		}
	}
}
