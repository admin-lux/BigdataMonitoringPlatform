package com.hjonline.bigdata.model.response;

public class BaseResponseFactory {

	public static BaseResponse getParamError_500() {
		return getErrorResponse("500", "参数错误。。。");
	}

	public static BaseResponse get500() {
		return getParamError_500();
	}

	public static BaseResponse get500(String msg) {
		return getErrorResponse("500", msg);
	}
	
	public static BaseResponse getNotVerify() {
		return getErrorResponse("500", "无权限!");
	}

	public static BaseResponse get200() {
		return getErrorResponse("200", "ok");
	}

	public static BaseResponse getErrorResponse(String code, String msg) {
		BaseResponse response = new BaseResponse();
		response.setCode(code);
		response.setMsg(msg);
		return response;
	}

}
