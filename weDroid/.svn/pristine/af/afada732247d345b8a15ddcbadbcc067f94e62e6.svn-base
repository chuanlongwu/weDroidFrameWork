package com.wedroid.framework.fragment;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wedroid.framework.common.ToastUtil;
import com.wedroid.framework.module.http.WeDroidHttpRequest;
import com.wedroid.framework.module.http.WeDroidRequestCallBack;

/**
 * @author 吴传龙
 */
public abstract class WeDroidFragment extends Fragment implements WeDroidRequestCallBack {

	protected Context mContext;

	protected View mView;

	protected AsyncQueryHandler asyncQueryHandler;

	protected abstract View initContentView(LayoutInflater inflater);

	protected abstract void initViewById(View view);

	protected abstract void initListener();

	protected abstract void initData(Bundle savedInstanceState);

	protected View $(int resId) {
		return mView == null ? null : mView.findViewById(resId);
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

	public String getStringRes(int resId) {
		return mContext.getResources().getString(resId);
	}

	public int getColorRes(int resId) {
		return mContext.getResources().getColor(resId);
	}

	public String getEditText(EditText et) {
		return et.getText().toString().trim();
	}

	public void showToast(String text) {
		ToastUtil.showToast(text);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mView = initContentView(inflater);
		return mView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViewById(mView);
		initData(savedInstanceState);
		initListener();
	}
	
	@Override
	public Object HttpRequestBeforeNoHttp(int requestToken) {
		return null;
	}
	@Override
	public void httpRequestSuccessNOJson(Object result, int requestToken) {
		// TODO Auto-generated method stub
		
	}
	/********************************* 数据库的CRUD操作 ******************/
	public AsyncQueryHandler getAsyncQueryHandler() {
		if (asyncQueryHandler == null) {
			asyncQueryHandler = new AsyncQueryHandler(mContext.getContentResolver()) {
				@Override
				protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
					queryComplete(token, cookie, cursor);
				}

				@Override
				protected void onInsertComplete(int token, Object cookie, Uri uri) {
					insertComplete(token, cookie, uri);
				}

				@Override
				protected void onDeleteComplete(int token, Object cookie, int result) {
					deleteComplete(token, cookie, result);
				}

				@Override
				protected void onUpdateComplete(int token, Object cookie, int result) {
					updateComplete(token, cookie, result);
				}
			};
		}
		return asyncQueryHandler;
	}

	protected void queryComplete(int token, Object cookie, Cursor cursor) {
	}

	protected void insertComplete(int token, Object cookie, Uri uri) {
	}

	protected void deleteComplete(int token, Object cookie, int result) {
	}

	protected void updateComplete(int token, Object cookie, int result) {
	}

	@Override
	public void httpRequestSuccessInThread(Object result, int requestToken) {
		requestSuccessInThread(result, requestToken);
	}

	/** http请求成功，运行与ui线程 */
	public void requestSuccess(Object result, int requestToken) {
	}
	
	/** http请求成功，运行与子線程线程 */
	public void requestSuccessInThread(Object result, int requestToken) {
	}

	/** http请求失败，运行与ui线程 */
	public void requestFail(Object errorMessage, int requestToken) {
	}

	/** http请求之前，运行与ui线程 */
	public void requestBefore(int requestToken) {
	}

	/************* 通过自己开启线程的方式回调 ***************************/
	public void executeCallBack(final Object result, final int requestToken, final int requestType) {
		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (requestType == WeDroidHttpRequest.REQUEST_SUCCESS_CODE) {
						requestSuccess(result, requestToken);
					} else if (requestType == WeDroidHttpRequest.REQUEST_SUCCESS_CODE) {
						requestFail(result, requestToken);
					} else {
						requestBefore(requestToken);
					}
				}
			});
		}
	}

	@Override
	public final void httpRequestSuccess(final Object result, final int requestToken) {
		executeCallBack(result, requestToken, WeDroidHttpRequest.REQUEST_SUCCESS_CODE);
	}

	@Override
	public final void HttpRequestFail(Object errorMessage, int requestToken) {
		executeCallBack(errorMessage, requestToken, WeDroidHttpRequest.REQUEST_FAIL_CODE);
	}

	@Override
	public final Object HttpRequestBefore(int requestToken) {
		executeCallBack(null, requestToken, 0);
		return null;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Object requestSucessFinished(Object result, int requestToken) {
		return null;
	}
}
