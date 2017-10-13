package com.zj.support.dao;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

/**
 * Sqlite数据库助手
 * 
 * @author XingRongJing
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String TAG = DBHelper.class.getName();
	private static final String DB_NAME = "sqlite.db";
	private static final int DB_VERSION = 1;
	private static final String PROPERTIES_FILENAME = "classes.properties";
	private static final String KEY_CLASSNAME = "className";
	private static final String PROPERTIES_SEPERATOR = ",";
	private static DBHelper mHelper;
	private Context mCtx;

	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		this.mCtx = context;
	}

	public static synchronized DBHelper getDBHelper(Context ctx) {
		if (null == mHelper) {
			mHelper = new DBHelper(ctx, DB_NAME, null, DB_VERSION);
		}
		return mHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		Properties props = new Properties();
		try {
			props.load(mCtx.getAssets().open(PROPERTIES_FILENAME));
			String value = props.getProperty(KEY_CLASSNAME);
			if (!TextUtils.isEmpty(value)) {
				String[] classNames = value.split(PROPERTIES_SEPERATOR);
				List<String> sqls = DBTableHelper.entityToString(classNames);
				if (null != sqls && !sqls.toString().equals("[]")) {
					for (int i = 0; i < sqls.size(); i++) {
						db.execSQL(sqls.get(i));
					}
				}
			}
		} catch (IOException e) {
			Log.e(TAG, "shan-->onCreate tables：" + e.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.e(TAG, "shan-->onUpgrade oldVersion = " + oldVersion
				+ " newVersion = " + newVersion);
	}

}
