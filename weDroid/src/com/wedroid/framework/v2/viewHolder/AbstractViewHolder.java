package com.wedroid.framework.v2.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class AbstractViewHolder {

	protected View mContentView;
	
	public abstract int getLayoutResourceId();
	
	/**
	 * 初始化view
	 */
	public final void init(LayoutInflater inflater, ViewGroup parentView) {
		int layoutResource = getLayoutResourceId();
		mContentView = inflater.inflate(layoutResource, parentView);
		initViewById(mContentView);
	}
	
	/**
	 * 根据id初始化view控件
	 */
	public void initViewById(View mContentView){
		
	}
	
	/***
	 * 返回当前的Content View
	 */
	public final View getContentView() {
		return mContentView;
	}

}
