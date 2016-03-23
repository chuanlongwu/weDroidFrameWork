/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wedroid.framework.v2.module.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.wedroid.framework.v2.common.WedroidLog;

/**
 * A helper class to help make handling asynchronous {@link ContentResolver}
 * queries easier.
 */
public abstract class DBAsyncQueryHandler<T> extends Handler {
    private static final String TAG = "AsyncQuery";
    private static final boolean localLOGV = false;

    private static final int EVENT_ARG_QUERY = 1;
    private static final int EVENT_ARG_INSERT = 2;
    private static final int EVENT_ARG_UPDATE = 3;
    private static final int EVENT_ARG_DELETE = 4;

    /* package */ final WeakReference<ContentResolver> mResolver;

    private static Looper sLooper = null;

    private Handler mWorkerThreadHandler;

    protected static final class WorkerArgs {
        public Uri uri;
        public Handler handler;
        public String[] projection;
        public String selection;
        public String[] selectionArgs;
        public String orderBy;
        public Object result;
        public Object cookie;
        public ContentValues values;
        public List<?> queryList;
    }
    
    public Map<String, String> getFieldNameMapping(Class<?> genricType){
		Map<String, String> fieldNameMapping = new HashMap<String, String>();
		Field[] declaredFields = genricType.getDeclaredFields();
		for (int i = 0; i < declaredFields.length; i++) {
			Field field = declaredFields[0];
			DBField annotation = field.getAnnotation(DBField.class);
			if (annotation!=null){
				fieldNameMapping.put(annotation.name(), field.getName());
			}else{
				fieldNameMapping.put(field.getName(), field.getName());
			}
		}
		return fieldNameMapping;
	}
    
    public List<T> processCursor(Cursor cursor){
    	@SuppressWarnings("unchecked")
		Class<T> clazz = (Class<T>) getSuperClassGenricType(getClass(), 0);
		Map<String, String> fieldNameMapping = getFieldNameMapping(clazz);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("[");
		int columnCount = cursor.getColumnCount();
		while(cursor.moveToNext()){
			stringBuffer.append("{");
			for(int index=0;index<columnCount;index++){
				String columnName = cursor.getColumnName(index);
				String columnValue = cursor.getString(index);
				String fieldName = fieldNameMapping.get(columnName);
				stringBuffer.append(fieldName+":"+columnValue+",");
			}
			stringBuffer = stringBuffer.deleteCharAt(stringBuffer.length()-1);
			stringBuffer.append("}");
		}
		stringBuffer.append("]");
		return JSONArray.parseArray(stringBuffer.toString(), clazz);
    }
    
    /**
     * 通过反射, 获得定义Class时声明的父类的泛型参数的类型. 如无法找到, 返回Object.class.
     */
    @SuppressWarnings("unchecked")
    public static Class<Object> getSuperClassGenricType(final Class<?> clazz, final int index) {
    	//返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
           return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
              return Object.class;
        }
        return (Class<Object>) params[index];
    }
    
    protected class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            final ContentResolver resolver = mResolver.get();
            if (resolver == null) return;

            WorkerArgs args = (WorkerArgs) msg.obj;

            int token = msg.what;
            int event = msg.arg1;

            switch (event) {
                case EVENT_ARG_QUERY:
                    Cursor cursor;
                    try {
                        cursor = resolver.query(args.uri, args.projection,
                                args.selection, args.selectionArgs,
                                args.orderBy);
                        // Calling getCount() causes the cursor window to be filled,
                        // which will make the first access on the main thread a lot faster.
                        if (cursor != null) {
                            cursor.getCount();
                            args.queryList = processCursor(cursor);
                        }
                    } catch (Exception e) {
                        WedroidLog.w(TAG, "Exception thrown during handling EVENT_ARG_QUERY");
                        cursor = null;
                    }
                    args.result = cursor;
                    break;

                case EVENT_ARG_INSERT:
                    args.result = resolver.insert(args.uri, args.values);
                    break;

                case EVENT_ARG_UPDATE:
                    args.result = resolver.update(args.uri, args.values, args.selection,
                            args.selectionArgs);
                    break;

                case EVENT_ARG_DELETE:
                    args.result = resolver.delete(args.uri, args.selection, args.selectionArgs);
                    break;
            }

            // passing the original token value back to the caller
            // on top of the event values in arg1.
            Message reply = args.handler.obtainMessage(token);
            reply.obj = args;
            reply.arg1 = msg.arg1;

            if (localLOGV) {
                WedroidLog.d(TAG, "WorkerHandler.handleMsg: msg.arg1=" + msg.arg1
                        + ", reply.what=" + reply.what);
            }

            reply.sendToTarget();
        }
    }

    public DBAsyncQueryHandler(ContentResolver cr) {
        super();
        mResolver = new WeakReference<ContentResolver>(cr);
        synchronized (DBAsyncQueryHandler.class) {
            if (sLooper == null) {
                HandlerThread thread = new HandlerThread("AsyncQueryWorker");
                thread.start();

                sLooper = thread.getLooper();
            }
        }
        mWorkerThreadHandler = createHandler(sLooper);
    }

    protected Handler createHandler(Looper looper) {
        return new WorkerHandler(looper);
    }

    /**
     * This method begins an asynchronous query. When the query is done
     * {@link #onQueryComplete} is called.
     *
     * @param token A token passed into {@link #onQueryComplete} to identify
     *  the query.
     * @param cookie An object that gets passed into {@link #onQueryComplete}
     * @param uri The URI, using the content:// scheme, for the content to
     *         retrieve.
     * @param projection A list of which columns to return. Passing null will
     *         return all columns, which is discouraged to prevent reading data
     *         from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *         SQL WHERE clause (excluding the WHERE itself). Passing null will
     *         return all rows for the given URI.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in the order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY
     *         clause (excluding the ORDER BY itself). Passing null will use the
     *         default sort order, which may be unordered.
     */
    public void startQuery(int token, Object cookie, Uri uri,
            String[] projection, String selection, String[] selectionArgs,
            String orderBy) {
        // Use the token as what so cancelOperations works properly
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_QUERY;

        WorkerArgs args = new WorkerArgs();
        args.handler = this;
        args.uri = uri;
        args.projection = projection;
        args.selection = selection;
        args.selectionArgs = selectionArgs;
        args.orderBy = orderBy;
        args.cookie = cookie;
        msg.obj = args;

        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * Attempts to cancel operation that has not already started. Note that
     * there is no guarantee that the operation will be canceled. They still may
     * result in a call to on[Query/Insert/Update/Delete]Complete after this
     * call has completed.
     *
     * @param token The token representing the operation to be canceled.
     *  If multiple operations have the same token they will all be canceled.
     */
    public final void cancelOperation(int token) {
        mWorkerThreadHandler.removeMessages(token);
    }

    /**
     * This method begins an asynchronous insert. When the insert operation is
     * done {@link #onInsertComplete} is called.
     *
     * @param token A token passed into {@link #onInsertComplete} to identify
     *  the insert operation.
     * @param cookie An object that gets passed into {@link #onInsertComplete}
     * @param uri the Uri passed to the insert operation.
     * @param initialValues the ContentValues parameter passed to the insert operation.
     */
    public final void startInsert(int token, Object cookie, Uri uri,
            ContentValues initialValues) {
        // Use the token as what so cancelOperations works properly
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_INSERT;

        WorkerArgs args = new WorkerArgs();
        args.handler = this;
        args.uri = uri;
        args.cookie = cookie;
        args.values = initialValues;
        msg.obj = args;

        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * This method begins an asynchronous update. When the update operation is
     * done {@link #onUpdateComplete} is called.
     *
     * @param token A token passed into {@link #onUpdateComplete} to identify
     *  the update operation.
     * @param cookie An object that gets passed into {@link #onUpdateComplete}
     * @param uri the Uri passed to the update operation.
     * @param values the ContentValues parameter passed to the update operation.
     */
    public final void startUpdate(int token, Object cookie, Uri uri,
            ContentValues values, String selection, String[] selectionArgs) {
        // Use the token as what so cancelOperations works properly
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_UPDATE;

        WorkerArgs args = new WorkerArgs();
        args.handler = this;
        args.uri = uri;
        args.cookie = cookie;
        args.values = values;
        args.selection = selection;
        args.selectionArgs = selectionArgs;
        msg.obj = args;

        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * This method begins an asynchronous delete. When the delete operation is
     * done {@link #onDeleteComplete} is called.
     *
     * @param token A token passed into {@link #onDeleteComplete} to identify
     *  the delete operation.
     * @param cookie An object that gets passed into {@link #onDeleteComplete}
     * @param uri the Uri passed to the delete operation.
     * @param selection the where clause.
     */
    public final void startDelete(int token, Object cookie, Uri uri,
            String selection, String[] selectionArgs) {
        // Use the token as what so cancelOperations works properly
        Message msg = mWorkerThreadHandler.obtainMessage(token);
        msg.arg1 = EVENT_ARG_DELETE;

        WorkerArgs args = new WorkerArgs();
        args.handler = this;
        args.uri = uri;
        args.cookie = cookie;
        args.selection = selection;
        args.selectionArgs = selectionArgs;
        msg.obj = args;

        mWorkerThreadHandler.sendMessage(msg);
    }

    /**
     * Called when an asynchronous query is completed.
     *
     * @param token the token to identify the query, passed in from
     *            {@link #startQuery}.
     * @param cookie the cookie object passed in from {@link #startQuery}.
     * @param queryList The cursor holding the results from the query.
     */
    protected void onQueryComplete(int token, Object cookie, List<?> queryList) {
        // Empty
    }
    
    /**
     * Called when an asynchronous insert is completed.
     *
     * @param token the token to identify the query, passed in from
     *        {@link #startInsert}.
     * @param cookie the cookie object that's passed in from
     *        {@link #startInsert}.
     * @param uri the uri returned from the insert operation.
     */
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        // Empty
    }

    /**
     * Called when an asynchronous update is completed.
     *
     * @param token the token to identify the query, passed in from
     *        {@link #startUpdate}.
     * @param cookie the cookie object that's passed in from
     *        {@link #startUpdate}.
     * @param result the result returned from the update operation
     */
    protected void onUpdateComplete(int token, Object cookie, int result) {
        // Empty
    }

    /**
     * Called when an asynchronous delete is completed.
     *
     * @param token the token to identify the query, passed in from
     *        {@link #startDelete}.
     * @param cookie the cookie object that's passed in from
     *        {@link #startDelete}.
     * @param result the result returned from the delete operation
     */
    protected void onDeleteComplete(int token, Object cookie, int result) {
        // Empty
    }

    @Override
    public void handleMessage(Message msg) {
        WorkerArgs args = (WorkerArgs) msg.obj;

        if (localLOGV) {
            WedroidLog.d(TAG, "AsyncQueryHandler.handleMessage: msg.what=" + msg.what
                    + ", msg.arg1=" + msg.arg1);
        }

        int token = msg.what;
        int event = msg.arg1;

        // pass token back to caller on each callback.
        switch (event) {
            case EVENT_ARG_QUERY:
            	onQueryComplete(token, args.cookie, args.queryList);
                break;

            case EVENT_ARG_INSERT:
                onInsertComplete(token, args.cookie, (Uri) args.result);
                break;

            case EVENT_ARG_UPDATE:
                onUpdateComplete(token, args.cookie, (Integer) args.result);
                break;

            case EVENT_ARG_DELETE:
                onDeleteComplete(token, args.cookie, (Integer) args.result);
                break;
        }
    }
}
