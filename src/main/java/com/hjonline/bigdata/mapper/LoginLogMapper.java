package com.hjonline.bigdata.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.hjonline.bigdata.model.LoginLog;
import com.hjonline.bigdata.model.UsersJoinLoginLog;

@Mapper
public interface LoginLogMapper {
	public int addLog(LoginLog log);

	public Page<UsersJoinLoginLog> findParams(HashMap<String, Object> params);
}
