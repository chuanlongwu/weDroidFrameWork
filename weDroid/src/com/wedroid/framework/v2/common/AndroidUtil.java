package com.wedroid.framework.v2.common;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
/**
 * @author 吴传龙
 */
public class AndroidUtil {
	public interface callback {
		public void onClick(View widget);
	}

	/**
	 * 分割text，相应点击事件
	 * 
	 * @param textView
	 * @param clickableSpan
	 * @param text
	 * @param split
	 */
	public static void splitText(TextView textView, final callback callback, String text, char split,final int initColor) {
		textView.setText(text, BufferType.SPANNABLE);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
		Spannable spans = (Spannable) textView.getText();
		// 将文本内容根据空格划分成各个单词
		int pos = text.indexOf(split, 0);
		List<Integer> indicesList = new ArrayList<Integer>();
		while (pos != -1) {
			indicesList.add(pos);
			pos = text.indexOf(split, pos + 1);
		}
		Integer[] indices = indicesList.toArray(new Integer[0]);

		int start = 0;
		int end = 0;
		for (int i = 0; i <= indices.length; i++) {
			end = (i < indices.length ? indices[i] : spans.length());
			// 为每个单词添加ClickableSpan
			spans.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					callback.onClick(widget);
				}

				@Override
				public void updateDrawState(TextPaint ds) {
					ds.setColor(initColor);
					ds.setUnderlineText(true);
				}
			}, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			start = end + 1;
		}
		// 改变选中文本的高亮颜色
		// textView.setHighlightColor(Color.BLUE);
	}

	/**
	 * 返回的 bitmap就是屏幕的内容
	 */
	@SuppressWarnings("unused")
	public static String takeScreenShot(Activity activity) {
		String path = null;
		try {
			View view = activity.getWindow().getDecorView();
			/// Enables or disables the drawing cache
			view.setDrawingCacheEnabled(true);
			// will draw the view in a bitmap
			view.buildDrawingCache();
			Bitmap bitmap = view.getDrawingCache();
			Rect frame = new Rect();
			activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			int width = activity.getWindowManager().getDefaultDisplay().getWidth();
			int height = activity.getWindowManager().getDefaultDisplay().getHeight();
			// 去掉标题栏
			Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);
			view.destroyDrawingCache();
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis()
						+ ".jpg";
			} else {
				path = activity.getCacheDir() + "/" + System.currentTimeMillis() + ".jpg";
			}
			BitmapUtil.compressImage(b, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	
	
	public static String takeScreenShot(ViewGroup viewGroup,Activity activity) {
		String path = null;
		try {
			int height = 0;
	        for (int i = 0; i < viewGroup.getChildCount(); i++) {
	        	height += viewGroup.getChildAt(i).getHeight();
	        }
			Bitmap bitmap = viewGroup.getDrawingCache(true);
			// 去掉标题栏
	        Bitmap b=  Bitmap.createBitmap(bitmap, 0, 0, viewGroup.getWidth(), height);
//			Bitmap b = Bitmap.createBitmap(viewGroup.getWidth(), height, Bitmap.Config.RGB_565);
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis()
						+ ".jpg";
			} else {
				path = activity.getCacheDir() + "/" + System.currentTimeMillis() + ".jpg";
			}
			BitmapUtil.compressImage(b, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}
	
	
	
	
	
	/**
	 * 返回的 bitmap就是屏幕的内容
	 */
	@SuppressWarnings("unused")
	public static String takeScreenShotContainSurface(Activity activity,Bitmap surfaceBitMap,int left,int top) {
		String path = null;
		try {
			View view = activity.getWindow().getDecorView();
			/// Enables or disables the drawing cache
			view.setDrawingCacheEnabled(true);
			// will draw the view in a bitmap
			view.buildDrawingCache();
			Bitmap bitmap = view.getDrawingCache();
			Rect frame = new Rect();
			activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
			int statusBarHeight = frame.top;
			int width = activity.getWindowManager().getDefaultDisplay().getWidth();
			int height = activity.getWindowManager().getDefaultDisplay().getHeight();
			// 去掉标题栏
			Bitmap b = Bitmap.createBitmap(bitmap, 0, statusBarHeight, width, height - statusBarHeight);
			view.destroyDrawingCache();
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/takeScreenShotContainSurface.jpg";
			} else {
				path = activity.getCacheDir() + "/takeScreenShotContainSurface.jpg";
			}
			Canvas canvas = new Canvas(b);
			canvas.drawBitmap(surfaceBitMap, left, top, null);
//			b.createBitmap(surfaceBitMap, left, top, surfaceBitMap.getWidth(),surfaceBitMap.getHeight());
			BitmapUtil.compressImage(b, path);
			b.recycle();
			b = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public static String getFilePath(Context context, Uri contentUri) {
		String filePath = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (DocumentsContract.isDocumentUri(context, contentUri)) {
				String wholeID = DocumentsContract.getDocumentId(contentUri);
				String id = wholeID.split(":")[1];
				String[] column = { MediaStore.Images.Media.DATA };
				String sel = MediaStore.Images.Media._ID + "=?";
				Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column,
						sel, new String[] { id }, null);
				int columnIndex = cursor.getColumnIndex(column[0]);
				if (cursor.moveToFirst()) {
					filePath = cursor.getString(columnIndex);
				}
				cursor.close();
			}
		} else {
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			filePath = cursor.getString(column_index);
		}
		return filePath;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem != null) {
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static int getListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return 0;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem != null) {
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}
		}
		return totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	}

	/**
	 * 获得当前配置的版本号
	 *
	 * @return String
	 */
	public static String getVersionName(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
		}
		return packInfo.versionName;
	}

	/**
	 * 获得当前配置的版本号
	 *
	 * @return String
	 */
	public static int getVersionCode(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			return packInfo.versionCode;
		} catch (Exception e) {
		}
		return 0;
	}

	/**
	 * 获取清单文件meta信息
	 */
	public static String getMetaValue(Context context, String metaKey) {
		Bundle metaData = null;
		String apiKey = null;
		if (context == null || metaKey == null) {
			return null;
		}
		try {
			ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			if (null != ai) {
				metaData = ai.metaData;
			}
			if (null != metaData) {
				apiKey = metaData.getString(metaKey);
			}
		} catch (NameNotFoundException e) {

		}
		return apiKey;
	}
	
	
	public static String getImeiNo (Context context){
		TelephonyManager TelephonyMgr = null;
		try {
			TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
			return TelephonyMgr.getDeviceId(); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
