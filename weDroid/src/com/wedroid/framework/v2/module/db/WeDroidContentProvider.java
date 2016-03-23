package com.wedroid.framework.v2.module.db;

import com.wedroid.framework.v2.activity.WeDroidApplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class WeDroidContentProvider extends ContentProvider {
	
	public static final int MANY_RESLUT = 1 << 0;
	public static final int ONE_RESULT = 1 << 1;
	public static final String AUTOHORITY = "cn.wuchuanlong.provider";
	public static final String MANY_RESLUT_MIME_TYPE = "vnd.android.cursor.dir/cn.wuchuanlong.provider";
	public static final String ONE_RESLUT_MIME_TYPE = "vnd.android.cursor.item/cn.wuchuanlong.provider";
	private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private WeDroidSQLiteOpenHelper baseDBHelper;
	private SQLiteDatabase sqLiteDatabase;
	
	public static final int WHERE_TABLE_INDEX = 0;
	
	@Override
	public boolean onCreate() {
		return true;
	}

	public void checkDBHelper(){
		if (baseDBHelper == null){
			baseDBHelper = WeDroidApplication.getDBHelper(getContext());
		}
		if (baseDBHelper != null){
			String[] tables = baseDBHelper.getTables();
			if (tables!=null){
				for(String table:tables){
					mUriMatcher.addURI(AUTOHORITY, table, MANY_RESLUT);
					mUriMatcher.addURI(AUTOHORITY, table + "/#", ONE_RESULT);
				}
			}
		}
	}
	
	@Override
	public Cursor query(Uri uri, String[] columns, String selection,
			String[] selectionArgs, String sortOrder) {
		sqLiteDatabase = getSqliteWritableDatabase();
		String tableName = uri.getPathSegments().get(WHERE_TABLE_INDEX);
		if (TextUtils.isEmpty(sortOrder)) {
			sortOrder = WeDroidSQLiteOpenHelper.DEFAULT_SORT_ORDER;
		}
		switch (mUriMatcher.match(uri)) {
		case MANY_RESLUT:
			return sqLiteDatabase.query(tableName, columns, selection,
					selectionArgs, null, null, sortOrder, WeDroidSQLiteOpenHelper.LIMIT);
		case ONE_RESULT:
			long id = ContentUris.parseId(uri);
			String whereSelection = WeDroidSQLiteOpenHelper.ID + " = " + id;
			if (!TextUtils.isEmpty(selection)) {
				whereSelection += " and " + selection;
			}
			return sqLiteDatabase.query(tableName, columns, whereSelection,
					selectionArgs, null, null, sortOrder, null);
		}
		return null;
	}
	
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		sqLiteDatabase = getSqliteWritableDatabase();
		String tableName = uri.getPathSegments().get(WHERE_TABLE_INDEX);
		long rowId = sqLiteDatabase.insert(tableName, null, values);
		return ContentUris.withAppendedId(uri, rowId);  //返回插入的记录所代表的URI 
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		sqLiteDatabase = getSqliteWritableDatabase();
		String tableName = uri.getPathSegments().get(WHERE_TABLE_INDEX);
		switch (mUriMatcher.match(uri)) {
		case MANY_RESLUT:
			return sqLiteDatabase.delete(tableName, selection, selectionArgs);
		case ONE_RESULT:
			long id = ContentUris.parseId(uri);  
            String whereSelection = WeDroidSQLiteOpenHelper.ID +" = "+id;  
            if(!TextUtils.isEmpty(selection)){
            	whereSelection  += " and "+selection;  
            }  
            return sqLiteDatabase.delete(tableName, whereSelection, selectionArgs);
		}
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String whereClause,
			String[] whereArgs) {
		sqLiteDatabase = getSqliteWritableDatabase();
		String tableName = uri.getPathSegments().get(WHERE_TABLE_INDEX);
		switch (mUriMatcher.match(uri)) {
		case MANY_RESLUT:
			return sqLiteDatabase.update(tableName, values, whereClause, whereArgs);
		case ONE_RESULT:
			long id = ContentUris.parseId(uri);  
            String whereSelection = WeDroidSQLiteOpenHelper.ID +" = "+id;  
            if(!TextUtils.isEmpty(whereClause)){  
            	whereSelection  += " and "+whereClause;  
            }  
            return sqLiteDatabase.update(tableName, values, whereSelection, whereArgs);
		}
		return 0;
	}
	
	public synchronized SQLiteDatabase getSqliteWritableDatabase(){
		if (baseDBHelper == null){
			checkDBHelper();
			baseDBHelper = WeDroidApplication.getDBHelper(getContext());
		}
		return baseDBHelper.getWritableDatabase();
	}
	
	@Override
	public String getType(Uri uri) {
		String mimeType = null;
		switch (mUriMatcher.match(uri)) {
			case MANY_RESLUT:
				mimeType = MANY_RESLUT_MIME_TYPE;
				break;
			case ONE_RESULT:
				mimeType = ONE_RESLUT_MIME_TYPE;
				break;
			default:
				throw new IllegalArgumentException("Unknow Uri:" + uri.toString());
		}
		return mimeType;
	}
}
