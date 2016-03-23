package com.wedroid.framework.v2.common;

import com.wedroid.framework.v2.activity.WeDroidApplication;

public class WedroidLog {
	
	private static final int v = 0;
	private static final int d = 1;
	private static final int i = 2;
	private static final int w = 3;
	private static final int e = 4;
	
	public static void d(String tag, String msg) {
		if (WeDroidApplication.loggerLevel < d && WeDroidApplication.loggerLevel>=0){
			android.util.Log.e(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (WeDroidApplication.loggerLevel < e && WeDroidApplication.loggerLevel>=0){
			android.util.Log.e(tag, msg);
		}
	}

	public static void v(String tag, String msg) {
		if (WeDroidApplication.loggerLevel == v && WeDroidApplication.loggerLevel>=0){
			android.util.Log.v(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (WeDroidApplication.loggerLevel < i && WeDroidApplication.loggerLevel>=0){
			android.util.Log.i(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (WeDroidApplication.loggerLevel < w && WeDroidApplication.loggerLevel>=0){
			android.util.Log.w(tag, msg);
		}
	}
	
}
