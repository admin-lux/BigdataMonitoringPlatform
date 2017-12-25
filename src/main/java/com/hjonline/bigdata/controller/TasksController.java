package com.hjonline.bigdata.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hjonline.bigdata.base.PageInfo;
import com.hjonline.bigdata.model.Tasks;
import com.hjonline.bigdata.model.request.SelectTasks;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.TaskService;
import com.hjonline.bigdata.utils.Utils;

@RestController
@RequestMapping("tasks")
public class TasksController {

	@Autowired
	private TaskService taskService;

	@RequestMapping(value = "addTask", method = RequestMethod.POST)
	public BaseResponse addTask(@RequestBody Tasks tasks) {
		if (null == tasks || Utils.isEmpty(tasks.getConfName()) || Utils.isEmpty(tasks.getName())
				|| Utils.isEmpty(tasks.getFilePath()) || Utils.isEmpty(tasks.getDesc())) {
			return BaseResponseFactory.getParamError_500();
		}
		tasks.setCrdate(new Date());
		tasks.setUpdate(new Date());
		tasks.setState(1);

		taskService.addTask(tasks);
		Tasks tasks2 = taskService.selectById(tasks.getId());
		return BaseResponseFactory.get200().setData(tasks2);
	}

	@RequestMapping(value = "upTask", method = RequestMethod.POST)
	public BaseResponse upTask(@RequestBody Tasks tasks) {
		if (null == tasks || tasks.getId() < 0 || Utils.isEmpty(tasks.getConfName()) || Utils.isEmpty(tasks.getName())
				|| Utils.isEmpty(tasks.getFilePath()) || Utils.isEmpty(tasks.getDesc())) {
			return BaseResponseFactory.getParamError_500();
		}
		Tasks tasks2 = taskService.selectById(tasks.getId());
		tasks2.setUpdate(new Date());
		tasks2.setConfName(tasks.getConfName());
		tasks2.setFilePath(tasks.getFilePath());
		tasks2.setName(tasks.getName());
		tasks2.setRundate(tasks.getRundate());
		tasks2.setDesc(tasks.getDesc());
		taskService.upTask(tasks2);
		return BaseResponseFactory.get200().setData(tasks2);
	}

	@RequestMapping(value = "deleteTask", method = RequestMethod.POST)
	public BaseResponse deleteTask(@RequestBody Tasks tasks) {
		if (null == tasks || tasks.getId() < 0) {
			return BaseResponseFactory.getParamError_500();
		}
		Tasks tasks2 = taskService.selectById(tasks.getId());
		tasks2.setState(0);
		tasks2.setUpdate(new Date());
		taskService.upTask(tasks2);
		return BaseResponseFactory.get200().setData(tasks2);
	}

	@RequestMapping(value = "deleteTasks", method = RequestMethod.POST)
	public BaseResponse deleteTasks(@RequestBody List<Tasks> tasks) {
		if (null == tasks || tasks.size() <= 0) {
			return BaseResponseFactory.getParamError_500();
		}
		List<Tasks> result = new ArrayList<>();
		for (Tasks t : tasks) {
			if (null == t || t.getId() < 0) {
				continue;
			}
			Tasks tasks2 = taskService.selectById(t.getId());
			tasks2.setState(0);
			tasks2.setUpdate(new Date());
			taskService.upTask(tasks2);
			result.add(tasks2);
		}
		return BaseResponseFactory.get200().setData(result);
	}

	@RequestMapping(value = "selectTasks", method = RequestMethod.POST)
	public BaseResponse selectTasks(@RequestBody SelectTasks tasks) {
		return BaseResponseFactory.get200().setData(new PageInfo<>(taskService.selectTasks(tasks)));
	}
	
	@RequestMapping(value = "selectTaskAll", method = RequestMethod.POST)
	public BaseResponse selectTaskAll() {
		return BaseResponseFactory.get200().setData(taskService.selectTaskAll());
	}

}
