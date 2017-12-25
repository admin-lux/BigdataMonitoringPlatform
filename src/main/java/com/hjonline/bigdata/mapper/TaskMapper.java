package com.hjonline.bigdata.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.hjonline.bigdata.model.Tasks;

@Mapper
public interface TaskMapper {

	public int addTask(Tasks tasks);

	public int upTask(Tasks tasks);

	public Tasks selectById(long id);

	public Page<Tasks> selectTasks(HashMap<String, Object> params);
}
