package com.wedroid.framework.module.http;

/**
 */
public interface  WeDroidRequestCallBack {

	public  void httpRequestSuccess(Object result, int requestToken);
	public  void httpRequestSuccessInThread(Object result, int requestToken);

	public  void HttpRequestFail(Object errorMessage, int requestToken);
	
	public  Object  HttpRequestBefore(int requestToken);

	public  Object requestSucessFinished(Object result, int requestToken);
	
	
	/**
	 * 主要用于离线的操作
	 */
	public  Object HttpRequestBeforeNoHttp(int requestToken);
	public void httpRequestSuccessNOJson(Object result, int requestToken);
}
