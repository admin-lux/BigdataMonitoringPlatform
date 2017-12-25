package com.hjonline.bigdata.controller;

import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hjonline.bigdata.base.PageInfo;
import com.hjonline.bigdata.model.Ope;
import com.hjonline.bigdata.model.request.SelectAllLoginlog;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.OpeService;
import com.hjonline.bigdata.utils.Utils;

@RestController
@RequestMapping("opes")
public class OpeController {
	@Autowired
	private OpeService opeService;

	@RequestMapping(value = "selectOpes", method = RequestMethod.POST)
	public BaseResponse selectOpes(@RequestBody SelectAllLoginlog usersParams) {
		if (null == usersParams.getPage()) {
			usersParams.setPage(1);
		}
		if (null == usersParams.getSize()) {
			usersParams.setSize(10);
		}
		HashMap<String, Object> params = new HashMap<>();
		if (!Utils.isEmpty(usersParams.getName())) {
			params.put("name", usersParams.getName());
		}
		if (!Utils.isEmpty(usersParams.getAccount())) {
			params.put("account", usersParams.getAccount());
		}
		if (null != usersParams.getStdate()) {
			params.put("stdate", new Date(usersParams.getStdate()));
		}
		if (null != usersParams.getEnddate()) {
			params.put("enddate", new Date(usersParams.getEnddate()));
		}
		PageInfo<Ope> pageinfo = new PageInfo<>(opeService.selectOpes(usersParams.getPage(), usersParams.getSize(), params));
		return BaseResponseFactory.get200().setData(pageinfo);
	}

}
