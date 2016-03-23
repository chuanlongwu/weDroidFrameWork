package com.wedroid.framework.v2.module.http;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.wedroid.framework.v2.constant.WeDroidGlobalConstant;

public class WeDroidRequestClient {
	
	private static HttpClient client;

	public static final int TIME_OUT = 100000;
	
	public static final int SUCCESS_STATUS_CODE = 200;
	
	public static synchronized HttpClient getHttpClient() {
		if (client == null) {
			synchronized (WeDroidRequestClient.class) {
				if (client == null) {
					HttpParams params =new BasicHttpParams();
					// 设置一些基本参数
					HttpConnectionParams.setStaleCheckingEnabled(params, false);  
					HttpConnectionParams.setSocketBufferSize(params, 8192 * 5);
		            HttpConnectionParams.setConnectionTimeout(params, TIME_OUT);
		            HttpConnectionParams.setSoTimeout(params, TIME_OUT);
		            
		            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		            HttpProtocolParams.setContentCharset(params,WeDroidGlobalConstant.CHARSET);
		            HttpProtocolParams.setUseExpectContinue(params, true);
		            HttpProtocolParams.setUserAgent(params,
                            "Mozilla/5.0(Linux;U;Android 2.2.1;en-us;Nexus One Build.FRG83) "
                                    +"AppleWebKit/553.1(KHTML,like Gecko) Version/4.0 Mobile Safari/533.1");
//		            // 从连接池中取连接的超时时间 
		            ConnManagerParams.setTimeout(params, 60000);
		            // 设置每个路由最大连接数
		            ConnPerRouteBean connPerRoute = new ConnPerRouteBean(300);
		            ConnManagerParams.setMaxConnectionsPerRoute(params,connPerRoute);
		            // 设置我们的HttpClient支持HTTP和HTTPS两种模式
		            SchemeRegistry schReg =new SchemeRegistry();
		            schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		            schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
		            // 使用线程安全的连接管理来创建HttpClient
		            ClientConnectionManager conMgr =new ThreadSafeClientConnManager( params, schReg);
		            client = new DefaultHttpClient(conMgr,params);
//		            client = new DefaultHttpClient(params);
				}
			}
		}
		return client;
	}
	
}
