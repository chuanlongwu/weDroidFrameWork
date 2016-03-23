package com.wedroid.framework.activity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wedroid.framework.common.ToastUtil;
import com.wedroid.framework.module.db.AsyncDBHandler;
import com.wedroid.framework.module.http.WeDroidHttpRequest;
import com.wedroid.framework.module.http.WeDroidRequestCallBack;
import com.wedroid.framework.module.imageLoader.WeDroidImageLoaderConfig;
import com.wedroid.framework.module.imageLoader.WeDroidImageLoader;

import android.app.Activity;
import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * @author 吴传龙
 * 
 */
@SuppressWarnings("unused")
public abstract class WeDroidActivity extends Activity implements WeDroidRequestCallBack{

	protected Context mContext;
	protected AsyncQueryHandler asyncQueryHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getApplicationContext();
	}
	
	public void showToast(String text){
//		Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
		ToastUtil.showToast(text);
	}
	
	public View $(int resId){
		return findViewById(resId);
	}
	public String getStringRes(int resId){
		return mContext.getResources().getString(resId);
	}
	
	public int getColorRes(int resId){
		return mContext.getResources().getColor(resId);
	}
	public String getEditText(EditText et){
		return et.getText().toString().trim();
	}
	
	public void showView(View view) {
		if(view!=null){
			if (view.getVisibility() == View.GONE ||
					view.getVisibility() == View.INVISIBLE) {
				view.setVisibility(View.VISIBLE);
			}
		}
	}

	public void hideView(View view) {
		if (view!=null){
			if (view.getVisibility() == View.VISIBLE) {
				view.setVisibility(View.GONE);
			}
		}
	}
	
	/********************************* 文件操作操作 ******************/
	
	
	/********************************* 数据库的CRUD操作 ******************/
	public  AsyncQueryHandler getAsyncQueryHandler(){
		if (asyncQueryHandler == null){
			asyncQueryHandler = new AsyncQueryHandler(getContentResolver()) {
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
	
	/*************通过自己开启http线程的方式回调***************************/
	
	/** 
	 * http请求成功，运行与ui线程
	*/
	protected void requestSuccess(Object result,int requestToken) {}
	/** 
	 * http请求成功，运行与子线程
	*/
	@Override
	public void httpRequestSuccessInThread(Object result, int requestToken) {
		requestSuccessInThread(result, requestToken);
	}
	
	/** http请求成功，运行与子線程线程 */
	public void requestSuccessInThread(Object result, int requestToken) {
	}

	/**
	 * http请求失败，运行与ui线程
	 */
	protected void requestFail(Object errorMessage,int requestToken) {}
	
	/**
	 * http请求之前，运行与ui线程
	 */
	protected void requestBefore(int requestToken) {}
	
	/**
	 * http请求成功之后，在子线程中处理数据
	 * result:不为空
	 */
	@Override
	public Object requestSucessFinished(Object result, int requestToken){
		return null;
	};
	@Override
	public void httpRequestSuccessNOJson(Object result, int requestToken) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public Object HttpRequestBeforeNoHttp(int requestToken) {
		return null;
	}
	
	private final void executeCallBack(final Object result,final int requestToken,final int requestType){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (requestType == WeDroidHttpRequest.REQUEST_SUCCESS_CODE){
					requestSuccess(result, requestToken);
				}else if (requestType == WeDroidHttpRequest.REQUEST_FAIL_CODE){
					requestFail(result, requestToken);
				}else{
					requestBefore(requestToken);
				}
			}
		});
	}
	
	
	@Override
	public final void httpRequestSuccess(final Object result,final int requestToken) {
		executeCallBack(result, requestToken, WeDroidHttpRequest.REQUEST_SUCCESS_CODE);
	}

	@Override
	public final void HttpRequestFail(Object errorMessage,int requestToken) {
		executeCallBack(errorMessage, requestToken, WeDroidHttpRequest.REQUEST_FAIL_CODE);
	}

	@Override
	public final Object HttpRequestBefore(int requestToken) {
		executeCallBack(null, requestToken, 0);
		return null;
	}
	
	public ImageLoader getImageLoader() {
		return WeDroidImageLoader.getImageLoader(this);
	}

	public void display(String uri, ImageView imageView) {
		getImageLoader().displayImage(uri, imageView,WeDroidImageLoaderConfig.getDisplayOptions());
	}
}
