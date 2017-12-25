package com.hjonline.bigdata.model.response;

public class BaseResponse {
	private String code;
	private String msg;
	private Object data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public BaseResponse setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return data;
	}

	public BaseResponse setData(Object data) {
		this.data = data;
		return this;
	}
	

}
