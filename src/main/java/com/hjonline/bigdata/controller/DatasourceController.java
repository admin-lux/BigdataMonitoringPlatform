package com.hjonline.bigdata.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hjonline.bigdata.base.LocalManager;
import com.hjonline.bigdata.base.LocalManagerType;
import com.hjonline.bigdata.base.PageInfo;
import com.hjonline.bigdata.model.Datasources;
import com.hjonline.bigdata.model.Users;
import com.hjonline.bigdata.model.request.SelectPageDatasource;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.DatasourceService;
import com.hjonline.bigdata.utils.Utils;

@RestController
@RequestMapping("datasources")
public class DatasourceController {
	@Autowired
	private DatasourceService dsService;

	@RequestMapping(value = "selectPageDatasources", method = RequestMethod.POST)
	public BaseResponse selectPage(@RequestBody SelectPageDatasource ds) {
		return BaseResponseFactory.get200().setData(new PageInfo<>(dsService.selectPage(ds)));
	}

	@RequestMapping(value = "selectDatasourceAll", method = RequestMethod.POST)
	public BaseResponse selectDatasourceAll() {
		return BaseResponseFactory.get200().setData(dsService.selectDatasourceAll());
	}

	@RequestMapping(value = "addDatasource", method = RequestMethod.POST)
	public BaseResponse addDatasource(@RequestBody Datasources ds) {
		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getNotVerify();
		}
		
		if (null == ds || Utils.isEmpty(ds.getSysName()) || Utils.isEmpty(ds.getConnect()) || ds.getSourceType() < 0
				|| Utils.isEmpty(ds.getUserName()) || Utils.isEmpty(ds.getPwd())) {
			return BaseResponseFactory.get500();
		}
		ds.setUserId(((Users) LocalManager.getVal(LocalManagerType.USERS)).getId());
		ds.setLastdate(new Date());
		ds.setState(1);
		ds.setCrdate(new Date());
		ds.setUpdate(new Date());
		int index = dsService.addDatasource(ds);
		if (index > 0) {
			return BaseResponseFactory.get200().setData(dsService.selectById(ds.getId()));
		} else {
			return BaseResponseFactory.get500().setMsg("参数错误，或者已存在");
		}
	}

	@RequestMapping(value = "upDatasource", method = RequestMethod.POST)
	public BaseResponse upDatasource(@RequestBody Datasources ds) throws ClassNotFoundException, SQLException {
		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getNotVerify();
		}
		
		if (null == ds || Utils.isEmpty(ds.getSysName()) || Utils.isEmpty(ds.getConnect()) || ds.getSourceType() < 0
				|| Utils.isEmpty(ds.getUserName()) || Utils.isEmpty(ds.getPwd())) {
			return BaseResponseFactory.get500();
		}
		// if (dsService.testConnect(ds) == 0) {
		// return BaseResponseFactory.get500();
		// }

		Datasources datasources = dsService.selectById(ds.getId());
		if (null == datasources) {
			return BaseResponseFactory.get500();
		}
		datasources.setLastdate(new Date());
		datasources.setUpdate(new Date());
		datasources.setSysName(ds.getSysName());
		datasources.setDatabaseName(ds.getDatabaseName());
		datasources.setConnect(ds.getConnect());
		datasources.setSourceType(ds.getSourceType());
		datasources.setUserName(ds.getUserName());
		datasources.setPwd(ds.getPwd());
		dsService.upDatasource(datasources);
		return BaseResponseFactory.get200().setData(datasources);
	}

	@RequestMapping(value = "deleteDatasource", method = RequestMethod.POST)
	public BaseResponse deleteDatasource(@RequestBody Datasources ds) {
		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getNotVerify();
		}
		
		if (null == ds || ds.getId() < 0) {
			return BaseResponseFactory.get500();
		}
		Datasources datasources = dsService.selectById(ds.getId());
		if (null == datasources) {
			return BaseResponseFactory.get500();
		}
		datasources.setState(0);
		datasources.setLastdate(new Date());
		datasources.setUpdate(new Date());
		dsService.upDatasource(datasources);
		return BaseResponseFactory.get200();
	}

	@RequestMapping(value = "deleteDatasources", method = RequestMethod.POST)
	public BaseResponse deleteDatasources(@RequestBody List<Datasources> ds) {
		Users currentUser = LocalManager.getVal(LocalManagerType.USERS);
		if (0 == currentUser.getAdmin()) {
			return BaseResponseFactory.getNotVerify();
		}
		
		if (null == ds || ds.size() < 0) {
			return BaseResponseFactory.get500();
		}

		List<Datasources> results = new ArrayList<>();
		ds.forEach(d -> {
			Datasources datasources = dsService.selectById(d.getId());
			if (null != datasources) {
				datasources.setState(0);
				datasources.setLastdate(new Date());
				datasources.setUpdate(new Date());
				dsService.upDatasource(datasources);
				results.add(datasources);
			}
		});
		return BaseResponseFactory.get200().setData(results);
	}

	@RequestMapping(value = "testConnect", method = RequestMethod.POST)
	public BaseResponse testConnect(@RequestBody Datasources ds) throws ClassNotFoundException, SQLException {
		if (null == ds || Utils.isEmpty(ds.getConnect()) || ds.getSourceType() <= 0 || Utils.isEmpty(ds.getUserName())
				|| Utils.isEmpty(ds.getPwd())) {
			return BaseResponseFactory.get500();
		}
		if (dsService.testConnect(ds) == 1) {
			return BaseResponseFactory.get200();
		}
		return BaseResponseFactory.get500();
	}

}
