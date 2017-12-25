package com.hjonline.bigdata.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hjonline.bigdata.mapper.LoginLogMapper;
import com.hjonline.bigdata.model.LoginLog;
import com.hjonline.bigdata.model.UsersJoinLoginLog;

@Service
public class LoginLogService {
	@Autowired
	private LoginLogMapper loginLogMapper;

	@Transactional
	public int addLog(LoginLog log) {
		return loginLogMapper.addLog(log);
	}

	public Page<UsersJoinLoginLog> findPage(int page, int size, HashMap<String, Object> params) {
		PageHelper.startPage(page, size, true);
		PageHelper.orderBy("logindate desc");
		return loginLogMapper.findParams(params);
	}

	public Page<UsersJoinLoginLog> findParams(HashMap<String, Object> params) {
		return loginLogMapper.findParams(params);
	}

}
