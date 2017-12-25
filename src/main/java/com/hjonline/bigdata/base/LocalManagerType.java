package com.hjonline.bigdata.base;

import com.hjonline.bigdata.model.Users;
/**
 * 本地线程变量的存储类型
 */
public enum LocalManagerType {

	TOKEN(String.class),
	IP(String.class),
	USERS(Users.class);
	LocalManagerType(Class<?> clz) {
		this.clz = clz;
	}

	@SuppressWarnings("rawtypes")
	private Class clz;

	@SuppressWarnings("rawtypes")
	public Class get() {
		return clz;
	}

}