package com.wedroid.framework.module.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.wedroid.framework.activity.WeDroidApplication;
import com.wedroid.framework.common.WeDroidTimeUtil;

public abstract class WeDroidHttpRequest {
	
	/**
	 * 请求成功
	 */
	public static final int REQUEST_SUCCESS_CODE = 1 << 1;
	
	/**
	 * 请求失败
	 */
	public static final int REQUEST_FAIL_CODE = 1 << 2;
	
	/**
	 * 无效的请求
	 */
	public static final int REQUEST_INVALID_CODE = -1;
	
	/**
	 * 请求单个
	 */
	public static final int REQUEST_SINGLE_TOKEN = 1<<0;
	
	/**
	 * 请求列表
	 */
	public static final int REQUEST_LIST_TOKEN = 1 << 2;
	
	/**
	 * 请求更多
	 */
	public static final int REQUEST_MORE_TOKEN = 1 << 4;
	
	/**
	 * 请求刷新
	 */
	public static final int REQUEST_REFRESH_TOKEN = 1 << 8;
	
	/**
	 * 请求初始化
	 */
	public static final int REQUEST_INIT_TOKEN = 1 << 16;
	
	protected int requestToken;
	
	public WeDroidHttpRequest(int requestToken) {
		this.requestToken = requestToken;
	}
	
	
	public void setNowDate(HttpResponse res){
		try {
			if(res!=null){
				Header[] allHeaders = res.getAllHeaders();
				for(Header header:allHeaders){
					if("Date".equalsIgnoreCase(header.getName())){
						long nowDateMills = WeDroidTimeUtil.GMT2BJTimeMills(header.getValue());
						WeDroidApplication.nowNetTimeMills = nowDateMills;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
