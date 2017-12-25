package com.hjonline.bigdata.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hjonline.bigdata.mapper.SyslogMapper;

@Service
public class SyslogService {
	@Autowired
	private SyslogMapper syslogMapper;

	public List<String> selectByParams(HashMap<String, Object> ps) {
		return syslogMapper.selectByParams(ps);
	}

}
