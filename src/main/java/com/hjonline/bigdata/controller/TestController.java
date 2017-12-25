package com.hjonline.bigdata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hjonline.bigdata.base.conf.Audience;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;

@RestController
@RequestMapping("test")
public class TestController {
	@Autowired
	private Audience audience;

	@RequestMapping("")
	String test() {
		return "hello world";
	}

	@RequestMapping("testMap")
	Map<String, Object> testMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("name", "rick");
		map.put("age", 26);
		return map;
	}

	@RequestMapping("testList")
	List<String> testList() {
		List<String> list = new ArrayList<>();
		list.add("rick");
		list.add("jerome");
		return list;
	}

	@RequestMapping("getaudience")
	public BaseResponse getAudience() {
		//,audience.getName(),audience.getBase64Secret(),audience.getExpiresSecond())
		return BaseResponseFactory.get200().setData(audience.getClientId());
	}
}
