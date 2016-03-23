package com.wedroid.framework.v2.activity;

import java.lang.reflect.ParameterizedType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wedroid.framework.v2.cache.HttpBusinessMappingContainer;
import com.wedroid.framework.v2.common.ToastUtil;
import com.wedroid.framework.v2.module.business.WeDroidBusiness;
import com.wedroid.framework.v2.module.http.WeDroidHttpRequest;
import com.wedroid.framework.v2.module.http.WeDroidRequestCallBack;
import com.wedroid.framework.v2.module.http.WeDroidRequestExecutor;
import com.wedroid.framework.v2.module.imageLoader.WeDroidImageLoader;
import com.wedroid.framework.v2.module.imageLoader.WeDroidImageLoaderConfig;
import com.wedroid.framework.v2.viewHolder.AbstractViewHolder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public abstract class BasePresenterActivity<ViewHolder extends AbstractViewHolder> 
	extends Activity implements IActivityLifeCycle {

	public Context mContext;
	public ViewHolder mViewHolder;
	private BusinessCallBack businessCallBack;
	private BasePresenterActivity<ViewHolder> basePresenterActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Class<ViewHolder> viewHolderClazz = getViewHolderClass();
		if (viewHolderClazz != null) {
			try {
				mViewHolder = viewHolderClazz.newInstance();
				mViewHolder.init(getLayoutInflater(), null);
				beforeSetContentView();
				setContentView(mViewHolder.getContentView());
				mContext = this;
				basePresenterActivity = this;
				afterSetContentView();
				requestBindData();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取当前activity的viewHolder
	 */
	@SuppressWarnings("unchecked")
	public Class<ViewHolder> getViewHolderClass() {
		Class<ViewHolder> viewHolderClazz = null;
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		if (parameterizedType != null) {
			viewHolderClazz = (Class<ViewHolder>) parameterizedType.getActualTypeArguments()[0];
		}
		return viewHolderClazz;
	}

	class BusinessCallBack implements WeDroidRequestCallBack {

		@Override
		public void httpRequestSuccess(Object result, int requestToken) {
			if (basePresenterActivity != null)
				basePresenterActivity.httpRequestSuccess(result, requestToken);
		}

		@Override
		public void httpRequestSuccessInThread(Object result, int requestToken) {
			if (basePresenterActivity != null)
				basePresenterActivity.httpRequestSuccessInThread(result, requestToken);
		}

		@Override
		public void HttpRequestFail(Object errorMessage, int requestToken) {
			if (basePresenterActivity != null)
				basePresenterActivity.HttpRequestFail(errorMessage, requestToken);
		}

		@Override
		public Object HttpRequestBefore(int requestToken) {
			if (basePresenterActivity != null)
				return basePresenterActivity.HttpRequestBefore(requestToken);
			else
				return null;
		}

		@Override
		public Object requestSucessFinished(Object result, int requestToken) {
			if (basePresenterActivity != null)
				return basePresenterActivity.requestSucessFinished(result, requestToken);
			else
				return null;
		}

		@Override
		public Object HttpRequestBeforeNoHttp(int requestToken) {
			if (basePresenterActivity != null)
				return basePresenterActivity.HttpRequestBeforeNoHttp(requestToken);
			else
				return null;
		}

		@Override
		public void httpRequestSuccessNOJson(Object result, int requestToken) {
			if (basePresenterActivity != null)
				basePresenterActivity.httpRequestSuccessNOJson(result, requestToken);
		}

	}

	public void requestHttpData(WeDroidBusiness weDroidBusiness) {
		requestHttpData(weDroidBusiness, WeDroidRequestExecutor.THREAD_DEFAULT_PRIOITY);
	}

	public void requestHttpData(WeDroidBusiness weDroidBusiness, int prioity) {
		if (businessCallBack == null) {
			businessCallBack = new BusinessCallBack();
		}
		HttpBusinessMappingContainer.putMapping(weDroidBusiness, businessCallBack);
		weDroidBusiness.execute(prioity);
	}

	/************* 通过自己开启http线程的方式回调 ***************************/

	/**
	 * http请求成功，运行与ui线程
	 */
	protected void requestSuccess(Object result, int requestToken) {
	}

	/**
	 * http请求成功，运行与子线程
	 */
	public void httpRequestSuccessInThread(Object result, int requestToken) {
		requestSuccessInThread(result, requestToken);
	}

	/** http请求成功，运行与子線程线程 */
	public void requestSuccessInThread(Object result, int requestToken) {
	}

	/**
	 * http请求失败，运行与ui线程
	 */
	protected void requestFail(Object errorMessage, int requestToken) {
	}

	/**
	 * http请求之前，运行与ui线程
	 */
	protected void requestBefore(int requestToken) {
	}

	/**
	 * http请求成功之后，在子线程中处理数据 result:不为空
	 */
	public Object requestSucessFinished(Object result, int requestToken) {
		return null;
	};

	public void httpRequestSuccessNOJson(Object result, int requestToken) {
		// TODO Auto-generated method stub
	}

	public Object HttpRequestBeforeNoHttp(int requestToken) {
		return null;
	}

	private final void executeCallBack(final Object result, final int requestToken, final int requestType) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (requestType == WeDroidHttpRequest.REQUEST_SUCCESS_CODE) {
					requestSuccess(result, requestToken);
				} else if (requestType == WeDroidHttpRequest.REQUEST_FAIL_CODE) {
					requestFail(result, requestToken);
				} else {
					requestBefore(requestToken);
				}
			}
		});
	}

	public final void httpRequestSuccess(final Object result, final int requestToken) {
		executeCallBack(result, requestToken, WeDroidHttpRequest.REQUEST_SUCCESS_CODE);
	}

	public final void HttpRequestFail(Object errorMessage, int requestToken) {
		executeCallBack(errorMessage, requestToken, WeDroidHttpRequest.REQUEST_FAIL_CODE);
	}

	public final Object HttpRequestBefore(int requestToken) {
		executeCallBack(null, requestToken, 0);
		return null;
	}

	public ImageLoader getImageLoader() {
		return WeDroidImageLoader.getImageLoader(this);
	}

	public void display(String uri, ImageView imageView) {
		getImageLoader().displayImage(uri, imageView, WeDroidImageLoaderConfig.getDisplayOptions());
	}

	public void showToast(String text) {
		ToastUtil.showToast(text);
	}

	public View $(int resId) {
		return findViewById(resId);
	}

	public String getStringRes(int resId) {
		return mContext.getResources().getString(resId);
	}

	public int getColorRes(int resId) {
		return mContext.getResources().getColor(resId);
	}

	public String getEditText(EditText et) {
		return et.getText().toString().trim();
	}

	public void showView(View view) {
		if (view != null) {
			if (view.getVisibility() == View.GONE || view.getVisibility() == View.INVISIBLE) {
				view.setVisibility(View.VISIBLE);
			}
		}
	}

	public void hideView(View view) {
		if (view != null) {
			if (view.getVisibility() == View.VISIBLE) {
				view.setVisibility(View.GONE);
			}
		}
	}

	public ViewHolder getViewHolder() {
		return mViewHolder;
	}

	public Context getContext() {
		return mContext;
	}

	public Context getGlobleContext() {
		return mContext.getApplicationContext();
	}

	@Override
	protected final void onStart() {
		super.onStart();
		afterOnStart();
	}

	@Override
	protected final void onResume() {
		super.onResume();
		afterOnResume();
	}

	@Override
	protected final void onPause() {
		super.onPause();
		afterOnPause();
	}

	@Override
	protected final void onStop() {
		super.onStop();
		afterOnStop();
	}

	@Override
	protected final void onRestart() {
		super.onRestart();
		afterOnReStart();
	}

	@Override
	protected final void onDestroy() {
		super.onDestroy();
		afterOnDestory();
		mViewHolder = null;
		mContext = null;
		basePresenterActivity = null;
		businessCallBack = null;
	}

	/**
	 * 在setContentView()之前调用
	 */
	public void beforeSetContentView() {
	}

	/**
	 * 在setContentView()之后调用
	 */
	public void afterSetContentView() {
	}

	/**
	 * 绑定数据
	 */
	public void requestBindData() {
	}

	@Override
	public void afterOnStart() {
	}

	@Override
	public void afterOnResume() {
	}

	@Override
	public void afterOnPause() {
	}

	@Override
	public void afterOnStop() {
	}

	@Override
	public void afterOnReStart() {
	}

	@Override
	public void afterOnDestory() {
	}

}
