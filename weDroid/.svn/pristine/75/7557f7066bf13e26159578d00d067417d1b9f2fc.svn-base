package com.wedroid.framework.activity;

import java.io.IOException;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wedroid.framework.common.AndroidUtil;
import com.wedroid.framework.module.db.WeDroidSQLiteOpenHelper;
import com.wedroid.framework.module.imageLoader.ImageDecoder;
import com.wedroid.framework.module.imageLoader.WeDroidImageLoaderConfig;
import com.wedroid.framework.module.imageLoader.ImageLoaderExecutor;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * @author 吴传龙
 */
public abstract class WeDroidApplication extends Application {

	public static int loggerLevel;
	public static ImageLoader imageLoader;
	public static Context mContext;
	public static long nowNetTimeMills;// 当前网络时间的毫秒值，这个值在每次请求网络的时候都会更新
	// 数据库操作类
	protected static WeDroidSQLiteOpenHelper baseDBHelper;
	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		loggerLevel = getLoggerLevel();
		baseDBHelper = initDBHelper();
	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		baseDBHelper = initDBHelper();
	}
	
	public abstract WeDroidSQLiteOpenHelper initDBHelper();
	
	public  static WeDroidSQLiteOpenHelper getDBHelper(Context context){
		return baseDBHelper;
	}
	
	/**
	 * 日志输出级别 <BR/>
	 *  private static final int v = 0;<BR/>
		private static final int d = 1;<BR/>
		private static final int i = 2;<BR/>
		private static final int w = 3;<BR/>
		private static final int e = 4;<BR/>
	 */
	public int getLoggerLevel() {
		try {
			String metaValue = AndroidUtil.getMetaValue(mContext,"logger_level");
			if (metaValue != null) {
				return Integer.parseInt(metaValue);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public synchronized static ImageLoader getImageLoader() {
		if (imageLoader == null) {
			imageLoader = ImageLoaderExecutor.getImageLoader(mContext);
		}
		return imageLoader;
	}

	
	
	/**
	 * 通过imageLoader加载数据
	 */
	public static void display(String uri, ImageView imageView) {
		getImageLoader().displayImage(uri, imageView,
				WeDroidImageLoaderConfig.getDisplayOptions());
	}

	/**
	 * 不通过imageLoader框架加载数据
	 */
	public static Bitmap display(String uri) {
		try {
			return BitmapFactory.decodeStream(new ImageDecoder()
					.getImageStream(uri, mContext));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static String oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;
	public static void showToast(String message) {
		if (toast == null) {
			toast = Toast.makeText(mContext, message, Toast.LENGTH_SHORT);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (message.equals(oldMsg)) {
				if (twoTime - oneTime > Toast.LENGTH_SHORT) {
					toast.show();
				}
			} else {
				oldMsg = message;
				toast.setText(message);
				toast.show();
			}
		}
		oneTime = twoTime;
	}
	
	/**
	 * 获取网络时间，毫秒值
	 */
	public static long  getNetTimeMills(){
		if(nowNetTimeMills==0){
			nowNetTimeMills = System.currentTimeMillis();
		}
		return nowNetTimeMills;
	}
}
