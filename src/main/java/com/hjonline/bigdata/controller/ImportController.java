package com.hjonline.bigdata.controller;

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
import com.hjonline.bigdata.model.Import;
import com.hjonline.bigdata.model.Users;
import com.hjonline.bigdata.model.request.SelectPageImports;
import com.hjonline.bigdata.model.response.BaseResponse;
import com.hjonline.bigdata.model.response.BaseResponseFactory;
import com.hjonline.bigdata.service.ImportService;
import com.hjonline.bigdata.utils.Utils;

@RestController
@RequestMapping("imports")
public class ImportController {
	@Autowired
	private ImportService importService;

	@RequestMapping(value = "addImportConf", method = RequestMethod.POST)
	public BaseResponse addImportConf(@RequestBody Import im) {
		if (null == im || im.getDatasourceId() < 0 || im.getTaskId() < 0 || Utils.isEmpty(im.getTableName())
				|| Utils.isEmpty(im.getHbaseName()) || Utils.isEmpty(im.getColumnFamily())
				|| Utils.isEmpty(im.getRowKey()) || Utils.isEmpty(im.getSplitBy())) {
			return BaseResponseFactory.get500();
		}
		Users users = LocalManager.getVal(LocalManagerType.USERS);
		im.setLastUserId(users.getId());
		im.setCrdate(new Date());
		im.setUpdate(new Date());
		im.setLastdate(new Date());
		importService.addImportConf(im);
		return BaseResponseFactory.get200().setData(im);
	}

	@RequestMapping(value = "upImportConf", method = RequestMethod.POST)
	public BaseResponse upImportConf(@RequestBody Import im) {
		if (null == im || im.getId() < 0 || im.getDatasourceId() < 0 || im.getTaskId() < 0
				|| Utils.isEmpty(im.getTableName()) || Utils.isEmpty(im.getHbaseName())
				|| Utils.isEmpty(im.getColumnFamily()) || Utils.isEmpty(im.getRowKey())
				|| Utils.isEmpty(im.getSplitBy())) {
			return BaseResponseFactory.get500();
		}
		Import t = importService.selectById(im.getId());
		t.setDatasourceId(im.getDatasourceId());
		t.setTaskId(im.getTaskId());
		t.setTableName(im.getTableName());
		t.setHbaseName(im.getHbaseName());
		t.setColumnFamily(im.getColumnFamily());
		t.setRowKey(im.getRowKey());
		t.setSplitBy(im.getSplitBy());
		t.setNumMappers(im.getNumMappers());
		t.setColumns(im.getColumns());
		t.setAddRowKey(im.getAddRowKey());
		t.setBeforeImport(im.getBeforeImport());
		t.setImportType(im.getImportType());
		t.setIncrementIf(im.getIncrementIf());
		Users users = LocalManager.getVal(LocalManagerType.USERS);
		t.setLastUserId(users.getId());
		t.setUpdate(new Date());
		t.setLastdate(new Date());
		importService.upImportConf(t);
		return BaseResponseFactory.get200().setData(t);
	}

	@RequestMapping(value = "deleteImportConf", method = RequestMethod.POST)
	public BaseResponse deleteImportConf(@RequestBody Import im) {
		if (null == im || im.getId() < 0) {
			return BaseResponseFactory.get500();
		}
		Import it = importService.selectById(im.getId());
		it.setState(0);
		importService.deleteImportConf(it);
		return BaseResponseFactory.get200();
	}

	@RequestMapping(value = "deleteImportConfs", method = RequestMethod.POST)
	public BaseResponse deleteImportConfs(@RequestBody List<Import> ims) {
		if (null == ims || ims.size() < 0) {
			return BaseResponseFactory.get500();
		}
		ims.forEach(im -> {
			if (im.getId() >= 0) {
				Import it = importService.selectById(im.getId());
				it.setState(0);
				importService.deleteImportConf(it);
			}
		});
		return BaseResponseFactory.get200();
	}

	@RequestMapping(value = "selectPageImports", method = RequestMethod.POST)
	public BaseResponse selectPageImports(@RequestBody SelectPageImports sp) {
		return BaseResponseFactory.get200().setData(new PageInfo<>(importService.selectPageImports(sp)));
	}

	
	

}
