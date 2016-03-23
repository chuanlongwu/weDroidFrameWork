package com.wedroid.framework.module.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.message.BasicNameValuePair;
import com.wedroid.framework.common.WeDroidUtil;
import com.wedroid.framework.constant.WeDroidGlobalConstant;

import com.wedroid.framework.common.WedroidLog;

/**
 * @author 吴传龙
 */
public class WeDroidRequestPost extends WeDroidHttpRequest {

	public WeDroidRequestPost(int requestToken) {
		super(requestToken);
	}

	/**
	 * 提交表单数据
	 */
	public void postData(String uri, Map<String, String> params, WeDroidRequestCallBack callBack) {
		SKPostDataCore(uri, params, null, callBack);
	}

	/**
	 * 单个文件上传
	 */
	public void postFile(String uri, File file, WeDroidRequestCallBack callBack) {
		SKPostDataCore(uri, null, new File[] { file }, callBack);
	}

	/**
	 * 单个文件上传带参
	 */
	public void postFile(String uri, Map<String, String> params, File file, WeDroidRequestCallBack callBack) {
		SKPostDataCore(uri, params, new File[] { file }, callBack);
	}

	/**
	 * 多个文件上传
	 */
	public void postFiles(String uri, File[] files, WeDroidRequestCallBack callBack) {
		SKPostDataCore(uri, null, files, callBack);
	}

	/**
	 * 多个文件上传，并且带参数
	 */
	public void postFiles(String uri, Map<String, String> params, File[] files, WeDroidRequestCallBack callBack) {
		SKPostDataCore(uri, params, files, callBack);
	}

	/**
	 * 
	 * @param uri
	 * @param params
	 *            <br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;key值和服务器端接收时用的字段一致
	 * @param files
	 *            <br/>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果是图像尽量先做压缩操作
	 * @param callBack
	 */
	private void SKPostDataCore(final String uri, final Map<String, String> params, final File[] files,
			final WeDroidRequestCallBack callBack) {
		HttpPost httpPost = null;
		try {
			if (callBack != null) {
				Object object = callBack.HttpRequestBefore(requestToken);
				if(object!=null){
					callBack.httpRequestSuccess(object, requestToken);
				}else{
					Object beforeNoHttp = callBack.HttpRequestBeforeNoHttp(requestToken);
					if(beforeNoHttp!=null){
						callBack.httpRequestSuccessNOJson(beforeNoHttp, requestToken);
						return;
					}
				}
			}
			MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
			// //如果有SocketTimeoutException等情况，可修改这个枚举
			multipartEntityBuilder.setMode(HttpMultipartMode.STRICT);
			// //不要用这个，会导致服务端接收不到参数
			multipartEntityBuilder.setCharset(Charset.forName(WeDroidGlobalConstant.CHARSET));
			if (files != null && files[0] != null) {
				FileBody fileBody = null;
				// 加入文件参数
				for (File file : files) {
					// Bitmap bm = BitmapUtil.revitionImageSize(file.getPath());
					// file = BitmapUtil.compressImage(bm, path);
					file.length();
					fileBody = new FileBody(file);
					multipartEntityBuilder.addPart(file.getName(), fileBody);
					// multipartEntityBuilder.addPart("files", fileBody);
				}
			}
			// 加入文本参数
			if (params != null) {
				Set<String> keySet = params.keySet();
				Iterator<String> it = keySet.iterator();
				// StringBody stringBody = null;
				while (it.hasNext()) {
					String key = it.next();
					// stringBody = new
					// StringBody(params.get(key),Charset.defaultCharset());
					// multipartEntityBuilder.addPart(key, stringBody);
					multipartEntityBuilder.addTextBody(String.valueOf(key), params.get(key),
							ContentType.TEXT_PLAIN.withCharset(WeDroidGlobalConstant.CHARSET));
				}
			}
			System.currentTimeMillis();
			HttpEntity httpEntity = multipartEntityBuilder.build();
			httpPost = new HttpPost(uri);
			httpPost.setEntity(httpEntity);
			HttpResponse response = WeDroidRequestClient.getHttpClient().execute(httpPost);
			if (response.getStatusLine().getStatusCode() == WeDroidRequestClient.SUCCESS_STATUS_CODE) {
				HttpEntity he = response.getEntity();
				Object result = WeDroidUtil.inputSream2String(he.getContent(), WeDroidGlobalConstant.CHARSET);
				if (callBack != null) {
					WedroidLog.e("WeDroidHttp", result.toString());
					callBack.httpRequestSuccess(result, requestToken);
				}
			}
			setNowDate(response);
			response = null;
			httpPost = null;
			httpEntity = null;
		} catch (Exception e) {
//			WedroidLog.e("CurrentThread", Thread.currentThread().getName() + "请求网络失败,耗时：" + (System.currentTimeMillis() - time)
//					+ ":失败原因：" + e.getLocalizedMessage());
			if (callBack != null) {
				WedroidLog.e("WeDroidHttp", e.getLocalizedMessage());
				callBack.HttpRequestFail("服务器忙", requestToken);
			}
		} finally {
		}
	}

	public void sendPost(String uri, Map<String, String> params, Map<String, String> headerParams,
			WeDroidRequestCallBack callBack) {
		WedroidLog.e("CurrentThread",Thread.currentThread().getName() + "即将开启httpPost请求" + (System.currentTimeMillis()));
		HttpPost post = new HttpPost(uri);
		// // 处理超时
		// HttpParams httpParams = new BasicHttpParams();//
		// httpParams = new BasicHttpParams();
		// HttpConnectionParams.setConnectionTimeout(httpParams,
		// WeDroidRequestClient.TIME_OUT);
		// HttpConnectionParams.setSoTimeout(httpParams,
		// WeDroidRequestClient.TIME_OUT);
		// post.setParams(httpParams);
		if (headerParams != null) {
			for (Map.Entry<String, String> item : headerParams.entrySet()) {
				post.setHeader(item.getKey(), item.getValue());
			}
		}
		sendPostCore(params, callBack, post);
	}

	private void sendPostCore(Map<String, String> params, WeDroidRequestCallBack callBack, HttpPost post) {
		
		// 设置参数
		if (params != null && params.size() > 0) {
			List<BasicNameValuePair> parameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, String> item : params.entrySet()) {
				if (item.getValue() != null) {
					BasicNameValuePair pair = new BasicNameValuePair(item.getKey(), item.getValue());
					parameters.add(pair);
				}
			}
			try {
				if (callBack != null) {
					Object object = callBack.HttpRequestBefore(requestToken);
					if(object!=null){
						callBack.httpRequestSuccess(object, requestToken);
					}else{
						Object beforeNoHttp = callBack.HttpRequestBeforeNoHttp(requestToken);
						if(beforeNoHttp!=null){
							callBack.httpRequestSuccessNOJson(beforeNoHttp, requestToken);
							return;
						}
					}
				}
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters, WeDroidGlobalConstant.CHARSET);
				post.setEntity(entity);
			} catch (UnsupportedEncodingException e) {
				if (callBack != null) {
					callBack.HttpRequestFail(e.getLocalizedMessage(), requestToken);
				}
			}
		}
		InputStream inputStream = null;
		try {
			long time = System.currentTimeMillis();
			HttpClient httpClient = WeDroidRequestClient.getHttpClient();
			WedroidLog.e("CurrentThread",Thread.currentThread().getName() + "正在开启httpPost请求" + (System.currentTimeMillis()));
			HttpResponse response = httpClient.execute(post);
			WedroidLog.e("CurrentThread",Thread.currentThread().getName() + "请求完毕,httpPost请求耗时：" + (System.currentTimeMillis() - time));
			if (response.getStatusLine().getStatusCode() == WeDroidRequestClient.SUCCESS_STATUS_CODE) {
				if (callBack != null) {
					inputStream = response.getEntity().getContent();
					Object result = WeDroidUtil.inputSream2String(inputStream, WeDroidGlobalConstant.CHARSET);
					WedroidLog.e("CurrentThread",Thread.currentThread().getName() + "请求成功,数据:"+result);
					callBack.httpRequestSuccess(result, requestToken);
				}
			} else {
				WedroidLog.e("CurrentThread", Thread.currentThread().getName() + "请求网络失败,耗时："
						+ (System.currentTimeMillis() - time) + ":失败原因："+response.getStatusLine().getStatusCode());
				if (callBack != null) {
					WedroidLog.e("WeDroidHttp", "数据获取失败");
					callBack.HttpRequestFail("数据获取失败", requestToken);
				}
			}
			setNowDate(response);
		} catch (Exception e) {
			if (callBack != null) {
				WedroidLog.e("WeDroidHttp", post.getURI() + "--" + e.getMessage());
				callBack.HttpRequestFail("服务器忙", requestToken);
			}
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				inputStream = null;
			}
		}
	}

}
