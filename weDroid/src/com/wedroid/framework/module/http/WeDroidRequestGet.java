package com.wedroid.framework.module.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.wedroid.framework.common.WeDroidUtil;
import com.wedroid.framework.constant.WeDroidGlobalConstant;

import com.wedroid.framework.common.WedroidLog;
/**
 * @author 吴传龙
 */
public class WeDroidRequestGet extends WeDroidHttpRequest{
	
	public WeDroidRequestGet(int requestToken) {
		super(requestToken);
	}
	
	/**
	 * 获取文本数据
	 */
	public void LoadTextData(final String uri, final WeDroidRequestCallBack callBack) {
		LoadTextData(uri, null, callBack);
	}

	/**
	 * 获取文本数据
	 */
	public void LoadTextData(final String uri,
			final Map<String, String> headerParams, final WeDroidRequestCallBack callBack) {
		long time = System.currentTimeMillis();
		if (callBack != null)
			callBack.HttpRequestBefore(requestToken);
		HttpGet get = new HttpGet(uri);
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, WeDroidRequestClient.TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, WeDroidRequestClient.TIME_OUT);
		get.setParams(httpParams);
		if (headerParams != null) {
			for (Map.Entry<String, String> item : headerParams.entrySet()) {
				get.setHeader(item.getKey(), item.getValue());
			}
		}
		InputStream inputStream = null;
		try {
			HttpResponse response = WeDroidRequestClient.getHttpClient().execute(get);
			if (response.getStatusLine().getStatusCode() == WeDroidRequestClient.SUCCESS_STATUS_CODE) {
				Object result = EntityUtils.toString(response.getEntity(),WeDroidGlobalConstant.CHARSET);
				WedroidLog.e("CurrentThread", Thread.currentThread().getName()+"请求网络完毕,耗时："+(System.currentTimeMillis()-time)+result);
//				Object result = WeDroidUtil.inputSream2String(inputStream);
				if (callBack != null)
					callBack.httpRequestSuccess(result ,requestToken);
				WedroidLog.e("WeDroidHttp", result.toString());
			} else {
				WedroidLog.e("CurrentThread", Thread.currentThread().getName()+"请求网络失败,耗时："+(System.currentTimeMillis()-time));
				if (callBack != null){
					callBack.HttpRequestFail(null,requestToken);
				}
			}
			setNowDate(response);
		} catch (Exception e) {
			WedroidLog.e("CurrentThread", Thread.currentThread().getName()+"请求网络失败,耗时："+(System.currentTimeMillis()-time)+":失败原因："+e.getLocalizedMessage());
			if (callBack != null)
				callBack.HttpRequestFail(e.getLocalizedMessage(),requestToken);
		}finally {
			if (inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				inputStream = null;
			}
		}
	}

	
	/**
	 * 获取文件数据
	 */
	public void LoadFileData(final String uri, final WeDroidRequestCallBack callBack,final String path) {
		HttpGet get = new HttpGet(uri);
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, WeDroidRequestClient.TIME_OUT);
		HttpConnectionParams.setSoTimeout(httpParams, WeDroidRequestClient.TIME_OUT);
		get.setParams(httpParams);
		try {
			if (callBack!=null){
				callBack.HttpRequestBefore(requestToken);
			}
			HttpResponse response = WeDroidRequestClient.getHttpClient().execute(get);
			if (response.getStatusLine().getStatusCode() == WeDroidRequestClient.SUCCESS_STATUS_CODE) {
				InputStream is = response.getEntity().getContent();
				if (callBack!=null){
					File apkFile = new File(path);
					if (!apkFile.exists()) {
						apkFile.createNewFile();
					}else{
						apkFile.delete();
						apkFile.createNewFile();
					}
					FileOutputStream fos = new FileOutputStream(apkFile);
					// 缓存
					byte buf[] = new byte[50];
					int numread = 0;
					// 写入到文件中
					while((numread = is.read(buf)) >=0){
						fos.write(buf, 0, numread);
					}
					fos.flush();
					fos.close();
					is.close();
					callBack.httpRequestSuccess(path,requestToken);
				}
			}
			setNowDate(response);
		} catch (Exception e) {
			if (	callBack!=null){
				callBack.HttpRequestFail(e.getLocalizedMessage(),requestToken);
			}
		}
	}
}
