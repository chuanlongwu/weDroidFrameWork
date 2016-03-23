package com.wedroid.framework.entity;

import java.io.Serializable;

public class WeDroidEntity implements Serializable{
	public static final long serialVersionUID = 1L;
	
	public String id;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
