package com.wedroid.framework.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurtityUtil {
	
	/**
	 * md5加密
	 */
	public static String md5Encode(String str){
		StringBuffer sb = new StringBuffer();
		char[] chars = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = md.digest(str.getBytes());
			for(byte b:bytes){
				sb.append(chars[(b >> 4) & 0x0F]);
				sb.append(chars[b & 0x0F]);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();
	}
}
