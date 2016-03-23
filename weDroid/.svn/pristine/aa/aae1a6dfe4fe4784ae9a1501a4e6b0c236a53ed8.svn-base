package com.wedroid.framework.module.imageLoader;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;


public class ImageDecoder {
	
	public InputStream getImageStream(String uri,Context context) throws IOException {
		ImageDownloader downloader = new BaseImageDownloader(context);
		return downloader.getStream(uri,null);
	}
}
