package com.zhijin.mlearning.support.sqlite;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.zhijin.mlearning.app.CoreApp;

public class DbHelper extends SQLiteOpenHelper
{

	private static Context mContext = CoreApp.getApp().getApplicationContext();
	private final static String sql = "schema.sql";
	private static DbHelper mInstance;
	private static String name;
	static int version = 1;

	private DbHelper(String name, CursorFactory factory, int version)
	{
		super(mContext, name, factory, version);
	}

	public synchronized static DbHelper getInstance()
	{
		if (mInstance == null) {
			name = mContext.getPackageName() + ".db";
			mInstance = new DbHelper(name, null, version);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		executeSchema(db, sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		if (newVersion <= oldVersion) {
			return;
		}
		int changeCnt = newVersion - oldVersion;
		for (int i = 0; i < changeCnt; i++) {
			String schemaName = "update" + (oldVersion + i + 1) + "_" + 0 + ".sql";
			executeSchema(db, schemaName);
		}
	}

	// 执行创建
	private void executeSchema(SQLiteDatabase db, String schemaName)
	{
		if (schemaName.equals(sql)) {
			Properties props = new Properties();
			try {
				props.load(mContext.getAssets().open("classes.properties"));
				String strValue = props.getProperty("className");
				if (!strValue.equals("")) {
					String[] classNames = strValue.split(",");
					List<String> list = EntityInflect.entityToString(classNames);
					if (list != null && !list.toString().equals("[]")) {
						for (int i = 0; i < list.size(); i++) {
							db.execSQL(list.get(i));
						}
					}
				}
			} catch (FileNotFoundException e) {
				Log.e("info", "config.properties Not Found Exception", e);
			} catch (IOException e) {
				Log.e("info", "config.properties IO Exception", e);
			}
		} else {
			BufferedReader in = null;
			try {
				in = new BufferedReader(new InputStreamReader(mContext.getAssets().open("schema" + "/" + schemaName)));
				String line;
				String buffer = "";
				while ((line = in.readLine()) != null) {
					buffer += line;
					if (line.trim().endsWith(";")) {
						db.execSQL(buffer);
						buffer = "";
					}
				}
				in.close();
			} catch (IOException e) {
				System.out.println("error====" + e.toString());
			} finally {
				try {
					if (in != null)
						in.close();
				} catch (IOException e) {
					System.out.println("error===" + e.toString());
				}
			}
		}
	}

	public boolean tabbleIsExist(String tableName)
	{
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = this.getReadableDatabase();
			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='" + tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}
		} catch (Exception e) {
		}
		return result;
	}
}
