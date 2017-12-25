package com.hjonline.bigdata.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.hjonline.bigdata.model.Monitor;

@Mapper
public interface MonitorMapper {
	public Page<Monitor> selectMonitorInfos(HashMap<String, Object> params);
	
	public List<Monitor> selectMonitors(HashMap<String, Object> params);

	public List<Monitor> selectByTaskName(@Param("taskName")String taskName);

	public HashMap<String, Object> selectByDiff(@Param("taskName") String taskName);

	public long countMonitors(@Param("taskName")String taskName);
	
	public List<Monitor> selectFaileds(@Param("taskName") String taskName);

	
}
