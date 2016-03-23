package com.wedroid.framework.v2.module.business;

import java.util.Map;

import com.wedroid.framework.v2.activity.WeDroidApplication;
/**
 * @author 吴传龙
 * @date 2015年8月21日
 * 处理网络请求的业务操作类
 */
import com.wedroid.framework.v2.cache.HttpBusinessMappingContainer;
import com.wedroid.framework.v2.module.http.WeDroidHttpRequest;
import com.wedroid.framework.v2.module.http.WeDroidRequestCallBack;
import com.wedroid.framework.v2.module.http.WeDroidRequestExecutor;
import com.wedroid.framework.v2.module.http.WeDroidRequestGet;
import com.wedroid.framework.v2.module.http.WeDroidRequestPost;

import android.app.Activity;
import android.app.Fragment;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
/**
 * @author 吴传龙
 */
public abstract class WeDroidBusiness implements Runnable, WeDroidRequestCallBack {

	protected int requestToken = WeDroidHttpRequest.REQUEST_INVALID_CODE;
	
	protected Map<String, String> params;
	
	public WeDroidBusiness(int requestToken, Map<String, String> params) {
		this.requestToken = requestToken;
		this.params = params;
	}

	public int getRequestToken() {
		return requestToken;
	}

	public void setRequestToken(int requestToken) {
		this.requestToken = requestToken;
	}

	/**
	 * 提交一个post请求
	 */
	protected void postRequest(String uri) {
		postRequest(uri, params);
	}

	protected void postRequest(String uri, Map<String, String> param) {
		WeDroidRequestPost post = new WeDroidRequestPost(requestToken);
		HttpBusinessMappingContainer.putMapping(post, this);
		post.sendPost(uri, param, null);
	}

	/**
	 * 提交一个get请求
	 */
	protected void getRequest(String uri) {
		WeDroidRequestGet get = new WeDroidRequestGet(requestToken);
		HttpBusinessMappingContainer.putMapping(get, this);
		get.LoadTextData(uri);
	}

	/**
	 * 开始执行http请求
	 */
	public void execute() {
		WeDroidRequestExecutor.execute(this);
	}
	
	/**
	 * 开始执行http请求
	 */
	public void execute(int prioity) {
		WeDroidRequestExecutor.execute(this,prioity);
	}

	/**
	 * 请求成功之后会调用此方法，用于将返回的数据在子线程中处理，然后在交给ui线程更新界面数据
	 * 具体的业务子类可以根据是否需要在子线程中处理数据重写此方法
	 */
	public Object requestSucessFinished(Object result, int requestToken) {
		return null;
	}

	@Override
	public void httpRequestSuccess(Object result, int requestToken) {
		Object mResult = requestSucessFinished(result, requestToken);
		WeDroidRequestCallBack activityCallBack = getBusinessCallBack();
		if (activityCallBack != null){
			activityCallBack.httpRequestSuccessInThread(mResult == null ? result : mResult, requestToken);
			activityCallBack.httpRequestSuccess(mResult == null ? result : mResult, requestToken);
		}
		HttpBusinessMappingContainer.removeMapping(this);
	}
	
	@Override
	public void httpRequestSuccessNOJson(Object result, int requestToken) {
		WeDroidRequestCallBack activityCallBack = getBusinessCallBack();
		if (activityCallBack != null)
			activityCallBack.httpRequestSuccess(result, requestToken);
		HttpBusinessMappingContainer.removeMapping(this);
	}
	
	@Override
	public void httpRequestSuccessInThread(Object result, int requestToken) {
	}

	@Override
	public void HttpRequestFail(Object errorMessage, int requestToken) {
		WeDroidRequestCallBack activityCallBack = getBusinessCallBack();
		if (activityCallBack != null)
			activityCallBack.HttpRequestFail(errorMessage, requestToken);
		HttpBusinessMappingContainer.removeMapping(this);
	}

	/**
	 * Handle some of the tasks to be processed before HTTP ,starts running In Thread 
	 */
	@Override
	public Object HttpRequestBefore(int requestToken) {
//		if (activityCallBack != null){
//			Object object = activityCallBack.HttpRequestBefore(requestToken);
//			if(object!=null){
//				httpRequestSuccess(object, requestToken);
//			}
//		}
		return null;
	}
	
	public Context getContext(){
		return WeDroidApplication.mContext;
	}
	@Override
	public Object HttpRequestBeforeNoHttp(int requestToken) {
		return null;
	}
	
	
	public WeDroidRequestCallBack getBusinessCallBack(){
		return (WeDroidRequestCallBack) HttpBusinessMappingContainer.getMapping(this);
	}

}
