package com.hjonline.bigdata.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = Exception.class)
	public BaseResponse defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		e.printStackTrace();
		return BaseResponseFactory.getErrorResponse("500",e.getMessage());
	}
}
