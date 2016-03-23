package com.wedroid.framework.module.business;

import java.util.Map;

import com.wedroid.framework.module.http.WeDroidHttpRequest;
import com.wedroid.framework.module.http.WeDroidRequestCallBack;
import com.wedroid.framework.module.http.WeDroidRequestExecutor;
import com.wedroid.framework.module.http.WeDroidRequestGet;
import com.wedroid.framework.module.http.WeDroidRequestPost;
/**
 * @author 吴传龙
 * @date 2015年8月21日
 * 处理网络请求的业务操作类
 */

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

	protected WeDroidRequestCallBack activityCallBack;
	protected WeDroidRequestCallBack businessCallBack;

	protected Map<String, String> params;

	public WeDroidBusiness(int requestToken, WeDroidRequestCallBack httpRequestCallBack, Map<String, String> params) {
		this.requestToken = requestToken;
		this.activityCallBack = httpRequestCallBack;
		this.params = params;
		this.businessCallBack = this;
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
		WeDroidRequestPost post = new WeDroidRequestPost(requestToken);
		post.sendPost(uri, params, null, businessCallBack);
	}

	protected void postRequest(String uri, Map<String, String> param) {
		WeDroidRequestPost post = new WeDroidRequestPost(requestToken);
		post.sendPost(uri, param, null, businessCallBack);
	}

	/**
	 * 提交一个get请求
	 */
	protected void getRequest(String uri) {
		WeDroidRequestGet get = new WeDroidRequestGet(requestToken);
		get.LoadTextData(uri, businessCallBack);
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
		if (activityCallBack != null){
			activityCallBack.httpRequestSuccessInThread(mResult == null ? result : mResult, requestToken);
			activityCallBack.httpRequestSuccess(mResult == null ? result : mResult, requestToken);
		}
	}
	
	@Override
	public void httpRequestSuccessInThread(Object result, int requestToken) {
		
	}
	
	@Override
	public void httpRequestSuccessNOJson(Object result, int requestToken) {
		if (activityCallBack != null)
			activityCallBack.httpRequestSuccess(result, requestToken);
	}

	@Override
	public void HttpRequestFail(Object errorMessage, int requestToken) {
		if (activityCallBack != null)
			activityCallBack.HttpRequestFail(errorMessage, requestToken);
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
	
	
	@Override
	public Object HttpRequestBeforeNoHttp(int requestToken) {
		return null;
	}
	public Context getContext() {
		if (activityCallBack != null) {
			if (activityCallBack instanceof Activity) {
				return (Activity) activityCallBack;
			} else if (activityCallBack instanceof Fragment) {
				return ((Fragment) activityCallBack).getActivity();
			} else if (activityCallBack instanceof android.support.v4.app.Fragment) {
				return ((android.support.v4.app.Fragment) activityCallBack).getActivity();
			}
		}
		return null;
	}
	
	
	protected AsyncQueryHandler asyncQueryHandler;
	
	/**
	 * db query handler
	 */
	public  AsyncQueryHandler getAsyncQueryHandler(){
		if (asyncQueryHandler == null){
			asyncQueryHandler = new AsyncQueryHandler(getContext().getContentResolver()) {
				@Override
				protected void onQueryComplete(int token, Object cookie,Cursor cursor) {
					queryComplete(token, cookie, cursor);
				}
				@Override
				protected void onInsertComplete(int token, Object cookie,Uri uri) {
					insertComplete(token, cookie, uri);
				}
				@Override
				protected void onDeleteComplete(int token, Object cookie,int result) {
					deleteComplete(token, cookie, result);
				}
				@Override
				protected void onUpdateComplete(int token, Object cookie,int result) {
					updateComplete(token, cookie, result);
				}
			};
		}
		return asyncQueryHandler;
	}
	
	protected void queryComplete(int token, Object cookie,Cursor cursor) {}
	protected void insertComplete(int token, Object cookie,Uri uri) {}
	protected void deleteComplete(int token, Object cookie,int result) {}
	protected void updateComplete(int token, Object cookie,int result) {}

}
