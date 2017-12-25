package com.hjonline.bigdata.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hjonline.bigdata.mapper.TaskMapper;
import com.hjonline.bigdata.model.Tasks;
import com.hjonline.bigdata.model.request.SelectTasks;

@Service
public class TaskService {
	@Autowired
	private TaskMapper taskMapper;
	@Autowired
	private OpeService opeService;

	@Transactional
	public int addTask(Tasks tasks) {
		opeService.addOpe("任务管理", String.format("增加了任务[%s]记录", tasks.getName()), 1);
		return taskMapper.addTask(tasks);
	}

	@Transactional
	public int upTask(Tasks tasks) {
		if (tasks.getState() == 0) {
			opeService.addOpe("任务管理", String.format("删除了任务[%s]记录", tasks.getName()), 0);
		} else {
			opeService.addOpe("任务管理", String.format("修改了任务[%s]记录", tasks.getName()), 2);
		}
		return taskMapper.upTask(tasks);
	}

	public Tasks selectById(long id) {
		return taskMapper.selectById(id);
	}

	public Page<Tasks> selectTasks(SelectTasks tasks) {
		if (null == tasks.getPage()) {
			tasks.setPage(1);
		}
		if (null == tasks.getSize()) {
			tasks.setSize(5);
		}

		PageHelper.startPage(tasks.getPage(), tasks.getSize(), true);
		PageHelper.orderBy("up_date desc");
		HashMap<String, Object> params = new HashMap<>();
		params.put("name", tasks.getTaskName());
		return taskMapper.selectTasks(params);
	}

	public Page<Tasks> selectTaskAll() {
		return taskMapper.selectTasks(null);
	}

}
