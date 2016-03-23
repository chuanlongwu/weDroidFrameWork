package com.wedroid.framework.module.db;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class WeDroidSQLiteOpenHelper extends SQLiteOpenHelper {
	
	// 主键名称
	public static final String ID = "id";
	
	// 默认的按主键排序
	public static final String DEFAULT_SORT_ORDER = ID + " desc ";
	
	// 默认查询前面20条数据
	public static final String LIMIT = "0,20";
	
	
	public WeDroidSQLiteOpenHelper(Context context, String name,
			int version) {
		super(context, name, null, version);
	}
	
	public abstract String[] getTables();

}
