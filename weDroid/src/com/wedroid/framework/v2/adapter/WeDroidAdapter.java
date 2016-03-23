package com.wedroid.framework.v2.adapter;

import android.widget.BaseAdapter;
/**
 * @author 吴传龙
 */
public abstract class WeDroidAdapter extends BaseAdapter{
	
	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
