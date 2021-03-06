package com.wedroid.framework.v2.module.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

public class FlexAbleTextView extends TextView{

	public interface Clicked{
		void click(boolean spreadOut);
	};
	Clicked clicked;
	int hasShowLines = 3;
	int showLines = 3;
	int totalLines;
	public FlexAbleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setClickable(true);
		setFocusable(true);
	}

	public FlexAbleTextView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public FlexAbleTextView(Context context) {
		this(context,null,0);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		totalLines = getLineCount();
	}
	
	public void initShowLines(){
		isFirstClick = true;
		if(hasShowLines > showLines){
			this.hasShowLines = showLines;
			setLines(hasShowLines);
		}
	}

	private boolean isFirstClick;
	public boolean isFirstClick(){
//		if(totalLines == showLines){
//			// 表示总的行数只有showLines行
//			return true;
//		}
//		return hasShowLines < totalLines;
		return isFirstClick;
	}
	
	private boolean isSpreadOut;
	public boolean isSpreadOut(){
		return isSpreadOut;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			isFirstClick = !isFirstClick;
			if(totalLines <= showLines){
				isSpreadOut = !isSpreadOut;
			}else if(totalLines > hasShowLines){
				isSpreadOut = true;
				hasShowLines = totalLines;
			}else{
				isSpreadOut = false;
				hasShowLines = showLines;
			}
			if(clicked!=null){
				clicked.click(isSpreadOut);
			}
			setLines(hasShowLines);
		}
		return true;
	}
	public void setClicked(Clicked clicked) {
		this.clicked = clicked;
	}
	
}
