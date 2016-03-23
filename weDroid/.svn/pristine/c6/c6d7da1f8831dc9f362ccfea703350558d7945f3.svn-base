package com.wedroid.framework.module.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * @author 吴传龙
 */
public class WeDroidRequestStatus {
	
	/**
	 * 通过ping百度的域名，来判断网络是否通了<br/>
	 * 普通方法不能判断外网的网络是否连接，比如连接上局域网<br/>
	 * 此方法最好在联网广播里面检测到联网时，判断是否真正的连上了网络
	 */
	public static boolean netWorkAvaliable(){
		String ip = "www.baidu.com";
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec("ping -c 3 w 100"+ip);
			InputStream inputStream = process.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer stringBuffer = new StringBuffer();
			String readLine = null;
			while((readLine = bufferedReader.readLine())!=null){
				stringBuffer.append(readLine);
			}
			int status = process.waitFor();
			if(status == 0){
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * WIFI网络是否联通
	 */
	public static boolean wiFiNetWorkAvaliable(Context context){
		return netWorkAvaliable(context, ConnectivityManager.TYPE_WIFI);
	}
	
	/**
	 * 移动网络是否联通
	 */
	public static boolean mobileNetWorkAvaliable(Context context){
		return netWorkAvaliable(context, ConnectivityManager.TYPE_MOBILE);
	}
	
	/**
	 * 
	 * @param context
	 * @param networkType 
	 * <br/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -1则代表检测是否已经连上任意一个网络	
	 * @return
	 */
	public static boolean netWorkAvaliable(Context context,int networkType){
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (networkType == -1){
			NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
			for(NetworkInfo networkInfo:allNetworkInfo){
				if (networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnected()){
					return true;
				}
			}
		}else{
			NetworkInfo networkInfo = connectivityManager.getNetworkInfo(networkType);
			if (networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnected()){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断GPS是否打开
	 */
	public static boolean isGpsEnabled(Context context) {   
        LocationManager lm = ((LocationManager) context   
                .getSystemService(Context.LOCATION_SERVICE));   
        List<String> accessibleProviders = lm.getProviders(true);   
        return accessibleProviders != null && accessibleProviders.size() > 0;   
    } 
}
