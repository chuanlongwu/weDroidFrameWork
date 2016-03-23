package com.wedroid.framework.common;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtil {

	public static <T> List<T> string2List(String result, Class<T> clazz) {
		List<T> list = JSONArray.parseArray(result, clazz);
		return list;
	}

	/**
	 * 对象转化成json字符串
	 * @param t
	 * @return
	 */
	public static <T> String object2JsonString(T t) {
		return JSONObject.toJSONString(t);
	}

	/**
	 * json字符串对象转化成
	 */
	public static <T> T JsonString2object(String result, Class<T> clazz) {
//		result = result.substring(1, result.length() - 1).replace("\\", "");
		return JSON.parseObject(result, clazz);
	}
}
