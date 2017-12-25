package com.hjonline.bigdata.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SyslogMapper {

	
	public List<String> selectByParams(HashMap<String, Object>ps);
}
