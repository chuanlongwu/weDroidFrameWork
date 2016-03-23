package com.wedroid.framework.module.http;

import android.os.AsyncTask;

/**
 * @author 吴传龙
 */
public class WeDroidAsynLoadTask<E,T> extends AsyncTask<E, Void, T> {

	private MyAsynTaskCallBack<T> taskCallback;
	
	@SuppressWarnings("hiding")
	public abstract class MyAsynTaskCallBack<T> implements AsynLoadTaskCallBack<E,T>{
		@Override
		public void beforeTaskExecute() {}
		@Override
		public void afterTaskExecute(T t) {}
	}
	
	public WeDroidAsynLoadTask(MyAsynTaskCallBack<T> taskCallback){
		this.taskCallback = taskCallback;
	}
	
	@Override
	protected void onPreExecute() {
		if (taskCallback!=null){
			taskCallback.beforeTaskExecute();
		}
		super.onPreExecute();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected T doInBackground(E... params) {
		if (taskCallback!=null){
			return (T)taskCallback.taskExecuting(params);
		}
		return (T) null;
	}
	
	@Override
	protected void onPostExecute(T result) {
		if (taskCallback!=null){
			taskCallback.afterTaskExecute(result);
		}
		super.onPostExecute(result);
	}
	
	
	
	
}

	
