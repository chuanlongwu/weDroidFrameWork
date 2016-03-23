package com.wedroid.framework.v2.cache;

import java.util.HashMap;
import java.util.Map;

public class HttpBusinessMappingContainer {
	
	private static Map<Object, Object> httpViewMapping = new HashMap<Object, Object>();
	
	public static void putMapping(Object key, Object value){
		checkMappingContainer();
		if(httpViewMapping.containsKey(key)){
			httpViewMapping.remove(key);
		}
		httpViewMapping.put(key, value);
	}
	
	public static Object getMapping(Object key){
		checkMappingContainer();
		return httpViewMapping.get(key);
	}
	
	public static void checkMappingContainer(){
		if(httpViewMapping==null){
			httpViewMapping = new HashMap<Object, Object>();
		}
	}
	
	public static void removeMapping(Object key){
		checkMappingContainer();
		httpViewMapping.remove(key);
	}
	
	public static int getSize(){
		checkMappingContainer();
		return httpViewMapping.size();
	}
}
