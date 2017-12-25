package com.hjonline.bigdata.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.hjonline.bigdata.model.TokenVerify;

@Mapper
public interface TokenVerifyMapper {
	
	public int addTokenLog(TokenVerify tokenVerify);
	
	public TokenVerify selectByParams(HashMap<String, Object> ps);
	
	public int upTokenLog(TokenVerify ty);

}
