package com.wedroid.framework.v2.common;

import android.content.Context;
/**
 * @author 吴传龙
 */
public class DensityUtil {
	
	/**
	 * 获取宽高
	 */
	public static float[] getHeightAndWidth(Context context){
		  float heightPixels = context.getResources().getDisplayMetrics().heightPixels; 
		  float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
		  float[] h = new float[2];
		  h[0] = heightPixels;
		  h[1] = widthPixels;
		  return h;
	}
	
    /** 
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    } 
    
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
    
    
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
}
