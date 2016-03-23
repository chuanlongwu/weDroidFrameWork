package com.wedroid.framework.v2.common;


import com.wedroid.framework.R;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class WeDroidViewUtil {
	
	public static void shakeView(View view,Context context,boolean vibratorAble){
		if(vibratorAble){
			Vibrator vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);  
			long [] pattern = {100,400,100,400};   // 停止 开启 停止 开启   
			vibrator.vibrate(pattern,-1);
		}
		Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
		view.startAnimation(shake);
	}
	
	public static void shakeView(View view,Context context){
		shakeView(view, context, false);
	}
}
