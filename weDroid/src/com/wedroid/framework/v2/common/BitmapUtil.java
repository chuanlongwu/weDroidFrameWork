package com.wedroid.framework.v2.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
/**
 * @author 吴传龙
 */
public class BitmapUtil {

	public static Bitmap revitionImageSize(String path) throws IOException {
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;
		Bitmap bitmap = null;
		while (true) {
			if ((options.outWidth >> i <= 1000) 
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(2.0D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}

	public static Bitmap revitionImageSize(String path, int reqWidth,
			int reqHeight) {
		BufferedInputStream in = null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(path)));
			BitmapFactory.decodeStream(in, null, options);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		options.inSampleSize = calculateInSampleSize(options, reqWidth,reqHeight);
		options.inJustDecodeBounds = false;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(path)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(in, null, options);
	}
	
	public static Bitmap revitionImageSize(InputStream in) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
//		options.inJustDecodeBounds = true;
//		try {
//			BitmapFactory.decodeStream(in, null, options);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		options.inSampleSize = 2;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(in, null, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;
			while ((halfHeight / inSampleSize) >= reqHeight
					&& (halfWidth / inSampleSize) >= reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}

	public static Bitmap flipBitmap(Bitmap bm) {
		Matrix matrix = new Matrix();
		matrix.postRotate(90); 
		int width = bm.getWidth();
		int height = bm.getHeight();
		bm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return bm;
	}

	/**
	 * bitmap2file
	 */
	public static File bitmap2file(Bitmap bm,String path) {
		if (bm != null) {
			BufferedOutputStream stream = null;
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			bm.compress(CompressFormat.JPEG, 50, os);
			byte[] bytes = os.toByteArray();
			try {
				File file = new File(path);
				FileOutputStream fstream = new FileOutputStream(file);
				stream = new BufferedOutputStream(fstream);
				stream.write(bytes);
				return file;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}

	public static File compressImage(Bitmap bm,String path) {
		File file = new File(path);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int options = 80;
		bm.compress(Bitmap.CompressFormat.JPEG,options,byteArrayOutputStream);
		while(byteArrayOutputStream.toByteArray().length/1024>100){
			byteArrayOutputStream.reset();
			bm.compress(Bitmap.CompressFormat.JPEG,options,byteArrayOutputStream);
			options -= 5;
		}
		file = bs2file(byteArrayOutputStream,path);
		return file;
	}
	
	private static  File bs2file(ByteArrayOutputStream byteArrayOutputStream,String path) {
		if (byteArrayOutputStream != null) {
			BufferedOutputStream stream = null;
			try {
				File file = new File(path);
				FileOutputStream fstream = new FileOutputStream(file);
				stream = new BufferedOutputStream(fstream);
				stream.write(byteArrayOutputStream.toByteArray());
				return file;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (stream != null) {
					try {
						stream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
		return null;
	}
}
