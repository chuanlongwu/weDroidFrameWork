package com.wedroid.framework.v2.module.ui;

import android.content.Context;
import android.util.AttributeSet;

public class WeDroidListView extends XListView {

	public WeDroidListView(Context context) {
		this(context,null,0);
	}

	public WeDroidListView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public WeDroidListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	
	public void init(){
		setOverScrollMode(OVER_SCROLL_NEVER);
	}

}
