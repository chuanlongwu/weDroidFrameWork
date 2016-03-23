package com.wedroid.framework.v2.module.imageLoader;

import java.io.File;
import java.util.List;
import android.content.Context;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.core.assist.MemoryCacheUtil;

/**
 * 图片加载核心类 TODO
 * 
 * @author 吴传龙
 * @date 2014年8月7日 上午11:29:18
 */
public class ImageLoaderExecutor {
	private static ImageLoader imageLoader;

	public static ImageLoader getImageLoader(Context context) {
		if (imageLoader == null) {
			synchronized (ImageLoaderExecutor.class) {
				if (imageLoader == null) {
					imageLoader = ImageLoader.getInstance();
					imageLoader.init(WeDroidImageLoaderConfig
							.getImageLoaderConfiguration(context));
				}
			}
		}
		return imageLoader;
	}

	private static String getMomeryCacheKeyForImageUri(String uri,
			Context context) {// SKConstant.user_avatar_downLoad+userAccount
		List<String> cacheKeys = MemoryCacheUtil.findCacheKeysForImageUri(uri,
				ImageLoaderExecutor.getImageLoader(context).getMemoryCache());
		return cacheKeys != null && !cacheKeys.isEmpty() ? cacheKeys.get(0)
				: null;
	}

	/**
	 * 从内存缓存中移除图片
	 * 
	 * @param uri
	 * @param context
	 */
	public static void removeImageFromMomeryCache(String uri, Context context) {
		String key = getMomeryCacheKeyForImageUri(uri, context);
		if (key == null) {
			return;
		}
		ImageLoaderExecutor.getImageLoader(context).getMemoryCache().remove(key);
	}

	public static File getFileForImageUri(String uri, Context context) {// SKConstant.user_avatar_downLoad+userAccount

		File file = DiscCacheUtil.findInCache(uri, ImageLoaderExecutor
				.getImageLoader(context).getDiscCache());
		return file != null ? file : null;
	}

	/**
	 * 从硬盘上移除文件
	 * 
	 * @param uri
	 * @param context
	 */
	public static void removeFileFromDisCache(String uri, Context context) {
		File file = getFileForImageUri(uri, context);
		if (file != null) {
			file.delete();
		}
	}

	/**
	 * 把图片从内存缓存和硬盘缓存中移除
	 * 
	 * @param uri
	 * @param context
	 */
	public static void removeImageFromMomeryAndDis(String uri, Context context) {
		removeImageFromMomeryCache(uri, context);
		removeFileFromDisCache(uri, context);
		// listView.setOnScrollListener(new PauseOnScrollListener(imageLoader,
		// pauseOnScroll, pauseOnFling));

	}
}
