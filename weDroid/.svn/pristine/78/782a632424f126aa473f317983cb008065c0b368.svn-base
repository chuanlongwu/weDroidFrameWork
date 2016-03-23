package com.wedroid.framework.module.db;

import java.util.List;
import android.content.ContentResolver;
import android.net.Uri;

public class AsyncDBHandler<T> extends DBAsyncQueryHandler<T> {
	CallBack callBack;

	public interface CallBack {
		<T> void queryResult(List<T> result); 
	}

	public AsyncDBHandler(ContentResolver cr, CallBack back) {
		super(cr);
	}
	
	
	@Override
	protected void onQueryComplete(int token, Object cookie, List<?> resultList) {
		if (resultList!=null){
			System.out.println("--");
		}
	}



	@Override
	protected void onInsertComplete(int token, Object cookie, Uri uri) {
	}

	@Override
	protected void onUpdateComplete(int token, Object cookie, int result) {
	}

	@Override
	protected void onDeleteComplete(int token, Object cookie, int result) {
	}

	
}
