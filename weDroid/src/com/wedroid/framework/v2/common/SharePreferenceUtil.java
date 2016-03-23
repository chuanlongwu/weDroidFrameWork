package com.wedroid.framework.v2.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferenceUtil {
	
	public static String getFromsp(String xml,String key,Context context){
		if(context!=null){
			SharedPreferences sharedPreferences = context.getSharedPreferences(xml, Context.MODE_PRIVATE);
			return sharedPreferences.getString(key, null);
		}
		return null;
	}
	
	public static void save2sp(String xml,String key,String value,Context context){
		if(context!=null){
			SharedPreferences sharedPreferences = context.getSharedPreferences(xml, Context.MODE_PRIVATE);
			Editor edit = sharedPreferences.edit();
			edit.putString(key, value);
			edit.commit();
		}
	}
}	
