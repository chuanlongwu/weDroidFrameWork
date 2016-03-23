package com.wedroid.framework.module.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Scroller;

public class WeDroidScrollViewGroup extends RelativeLayout{

	Scroller scroller;
	public WeDroidScrollViewGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		scroller = new Scroller(getContext());
	}

	public WeDroidScrollViewGroup(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public WeDroidScrollViewGroup(Context context) {
		this(context,null,0);
	}
	
	int startY;
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startY = (int) event.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			int dy = (int) event.getY();
			Log.e("startScroll", startY+"---"+(getScrollY()-dy));
			scroller.startScroll(getScrollX(), startY, 0, getScrollY()-dy);
			startY = dy;
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(event);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		return super.onTouchEvent(event);
	}
	
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int measuredWidth = measureWidth(widthMeasureSpec);
//		int measuredHeight = measureHeight(heightMeasureSpec);
//		setMeasuredDimension(measuredWidth,measuredHeight);
//		int childCount = getChildCount();
//		for (int i = 0; i < childCount; i++) {
//			View view = getChildAt(i);
//			view.measure(measuredWidth, measuredHeight);
//		}
//	}
//	@Override
//	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
//		int childCount = getChildCount();
//		for (int i = 0; i < childCount; i++) {
//			getChildAt(i).layout(l, t, r, b);
//		}
//	}
//	public int measureWidth(int widthMeasureSpec){
//		int size = 0;
//		int specSize  = MeasureSpec.getSize(widthMeasureSpec);
//		int specMode = MeasureSpec.getMode(widthMeasureSpec);
//		
//		switch (specMode) {
//			case MeasureSpec.AT_MOST:
//				// 最多的，子元素最多是specSize的值	
//				break;
//			case MeasureSpec.EXACTLY:
//				// 确定的，就是说父元素确定了子元素的大小，子元素将被限定在给定的边界而忽略自身的大小
//				size = specSize;
//				break;
//			case MeasureSpec.UNSPECIFIED:
//				// 父元素不限制子元素的大小，子元素可以获取任意的大小
//				break;
//		}
//		return size;
//	}
//	
//	public int measureHeight(int heightMeasureSpec){
//		int size = 0;
//		int specSize  = MeasureSpec.getSize(heightMeasureSpec);
//		int specMode = MeasureSpec.getMode(heightMeasureSpec);
//		
//		switch (specMode) {
//			case MeasureSpec.AT_MOST:
//				// 最多的，子元素最多是specSize的值	
//				break;
//			case MeasureSpec.EXACTLY:
//				// 确定的，就是说父元素确定了子元素的大小，子元素将被限定在给定的边界而忽略自身的大小
//				size = specSize;
//				break;
//			case MeasureSpec.UNSPECIFIED:
//				// 父元素不限制子元素的大小，子元素可以获取任意的大小
//				break;
//		}
//		return size;
//	}
}
