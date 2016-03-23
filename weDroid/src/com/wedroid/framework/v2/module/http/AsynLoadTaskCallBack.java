package com.wedroid.framework.v2.module.http;

public interface AsynLoadTaskCallBack<E,T> {
	
	/**
	 * 任务执行前，运行与ui线程
	 */
	void beforeTaskExecute();

	/**
	 * 任务执行中，运行与子线程
	 */
	@SuppressWarnings("unchecked")
	T taskExecuting(E... params);
	
	/**
	 * 任务执行后，运行与ui线程
	 */
	void afterTaskExecute(T t);
}