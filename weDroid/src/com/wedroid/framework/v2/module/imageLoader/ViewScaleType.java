package com.wedroid.framework.v2.module.imageLoader;

import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/**
 * Simplify {@linkplain ScaleType ImageView's scale type} to 2 types: {@link #FIT_INSIDE} and {@link #CROP}
 * 
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.6.1
 */
public enum ViewScaleType {
	/**
	 * Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions (width and height) of the
	 * image will be equal to or less the corresponding dimension of the view.
	 */
	FIT_INSIDE,
	/**
	 * Scale the image uniformly (maintain the image's aspect ratio) so that both dimensions (width and height) of the
	 * image will be equal to or larger than the corresponding dimension of the view.
	 */
	CROP;

	/**
	 * Defines scale type of ImageView.
	 * 
	 * @param imageView {@link ImageView}
	 * @return {@link #FIT_INSIDE} for
	 *         <ul>
	 *         <li>{@link ScaleType#FIT_CENTER}</li>
	 *         <li>{@link ScaleType#FIT_XY}</li>
	 *         <li>{@link ScaleType#FIT_START}</li>
	 *         <li>{@link ScaleType#FIT_END}</li>
	 *         <li>{@link ScaleType#CENTER_INSIDE}</li>
	 *         </ul>
	 *         {@link #CROP} for
	 *         <ul>
	 *         <li>{@link ScaleType#CENTER}</li>
	 *         <li>{@link ScaleType#CENTER_CROP}</li>
	 *         <li>{@link ScaleType#MATRIX}</li>
	 *         </ul>
	 *         ,
	 * 
	 */
	public static ViewScaleType fromImageView(ImageView imageView) {
		switch (imageView.getScaleType()) {
			case FIT_CENTER:
			case FIT_XY:
			case FIT_START:
			case FIT_END:
			case CENTER_INSIDE:// 缩放，已两者几何中心点为基准，讲图片的内容居中显示
				return FIT_INSIDE;
			case MATRIX:
			case CENTER://不缩放： 以imageview和原图的几何中心点为基准，按图片的原来大小居中显示，
			case CENTER_CROP:// 缩放，已两者的几何中心点为基准，使得原图大于等于imageciew的长宽
			default:
				return CROP;
		}
	}
}
