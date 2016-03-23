package com.wedroid.framework.v2.module.ui.anim;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class FlexableAnimation extends Animation{
	
	/** 目标的高度 */
	private int targetHeight;
	/** 目标view */
	private View view;
	/** 是否向下展开 */
	private boolean down;
	
	public FlexableAnimation(View targetview, int vieweight, boolean isdown) {
		this.view = targetview;
		this.targetHeight = vieweight;
		this.down = isdown;
	}
	
	@Override
	protected void applyTransformation(float interpolatedTime, Transformation t) {
		int newHeight;
		if (down) {
			newHeight = (int) (targetHeight * interpolatedTime);
		} else {
			newHeight = (int) (targetHeight * (1 - interpolatedTime));
		}
		view.getLayoutParams().height = newHeight;
		view.requestLayout();
		if (view.getVisibility() == View.GONE) {
			view.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	public boolean willChangeBounds() {
		return true;
	}
	
}
