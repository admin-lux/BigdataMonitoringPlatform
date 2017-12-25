package com.hjonline.bigdata.service;

import java.util.HashMap;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hjonline.bigdata.mapper.MonitorMapper;
import com.hjonline.bigdata.model.Monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitorService {
	@Autowired
	private MonitorMapper monitorMapper;

	public List<Monitor> selectMonitors(HashMap<String, Object> ps) {
		return monitorMapper.selectMonitors(ps);
	}
	
	public Page<Monitor> selectMonitorInfos(HashMap<String, Object> ps, int page, int size) {
		PageHelper.startPage(page, size, true);
		return monitorMapper.selectMonitorInfos(ps);
	}
	
	

	public List<Monitor> selectByTaskName(String taskName) {
		return monitorMapper.selectByTaskName(taskName);
	}

	public HashMap<String, Object> selectByDiff(String taskName) {
		return monitorMapper.selectByDiff(taskName);
	}

	public long countMonitors(String taskName) {
		return monitorMapper.countMonitors(taskName);
	}

	public List<Monitor> selectFaileds(String taskName) {
		return monitorMapper.selectFaileds(taskName);
	}

}
