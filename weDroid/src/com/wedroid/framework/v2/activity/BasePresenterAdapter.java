package com.wedroid.framework.v2.activity;

import java.lang.reflect.ParameterizedType;

import com.wedroid.framework.v2.viewHolder.AbstractViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BasePresenterAdapter<ViewHolder extends AbstractViewHolder> extends BaseAdapter{
	
	public ViewHolder mViewHolder;
	
	@Override
	public Object getItem(int position) {return null;}

	@Override
	public long getItemId(int position) {return 0;}

	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			if(convertView==null){
				mViewHolder = getViewHolderClass().newInstance();
				LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				mViewHolder.init(layoutInflater, parent);
				convertView = mViewHolder.getContentView();
				convertView.setTag(mViewHolder);
			}else{
				mViewHolder = (ViewHolder) convertView.getTag();
			}
			if(convertView!=null){
				requestBindDataOnPosition(position);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
	
	/**
	 * 绑定当前position的数据
	 */
	public abstract void requestBindDataOnPosition(int position);
	
	/**
	 * 获取当前adapter的viewHolder
	 */
	@SuppressWarnings("unchecked")
	public Class<ViewHolder> getViewHolderClass(){
		Class<ViewHolder> viewHolderClazz = null;
		ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
		if(parameterizedType!=null){
			viewHolderClazz = (Class<ViewHolder>) parameterizedType.getActualTypeArguments()[0];
		}
		return viewHolderClazz;
	}

}
