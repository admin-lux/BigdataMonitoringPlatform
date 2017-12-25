package com.hjonline.bigdata.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hjonline.bigdata.mapper.ImportMapper;
import com.hjonline.bigdata.model.Import;
import com.hjonline.bigdata.model.request.SelectPageImports;
import com.hjonline.bigdata.utils.Utils;

@Service
public class ImportService {
	@Autowired
	private ImportMapper importMapper;
	@Autowired
	private OpeService opeService;

	public int addImportConf(Import im) {
		HashMap<String, Object> params = new HashMap<>();
		params.put("taskId", im.getTaskId());
		params.put("tableName", im.getTableName());
		params.put("datasourceId", im.getDatasourceId());
		List<Import> ims = importMapper.selectByParams(params);
		opeService.addOpe("数据介入配置", String.format("增加了数据接入配置[%s]记录", im.getTableName()), 1);
		if (null != ims && ims.size() > 0) {
			ims.forEach(im2 -> {
				im.setId(im2.getId());
				im.setState(1);
				importMapper.upImportConf(im);
			});
		} else {
			importMapper.addImportConf(im);
		}
		return 1;
	}

	public int upImportConf(Import im) {
		opeService.addOpe("数据介入配置", String.format("修改了数据接入配置[%s]记录", im.getTableName()), 2);
		return importMapper.upImportConf(im);
	}

	public int deleteImportConf(Import im) {
		opeService.addOpe("数据介入配置", String.format("删除了数据接入配置[%s]记录", im.getTableName()), 0);
		return importMapper.upImportConf(im);
	}

	public Import selectById(int id) {
		return importMapper.selectById(id);
	}

	public Page<Import> selectPageImports(SelectPageImports sp) {
		if (null == sp.getPage()) {
			sp.setPage(1);
		}
		if (null == sp.getSize()) {
			sp.setSize(5);
		}

		PageHelper.startPage(sp.getPage(), sp.getSize(), true);
		PageHelper.orderBy("up_date desc");

		HashMap<String, Object> params = new HashMap<>();
		if (!Utils.isEmpty(sp.getSysName())) {
			params.put("sysName", sp.getSysName());
		}
		if (!Utils.isEmpty(sp.getDatabaseName())) {
			params.put("databaseName", sp.getDatabaseName());
		}
		if (!Utils.isEmpty(sp.getTableName())) {
			params.put("tableName", sp.getTableName());
		}
		if (sp.getTaksId() > 0) {
			params.put("taskId", sp.getTaksId());
		}
		return importMapper.selectPageImports(params);
	}

	public long countImports(HashMap<String, Object> params) {
		return importMapper.countImports(params);
	}

	public List<Import> selectByParams(String tableName,String taskName) {
		HashMap<String, Object> ps = new HashMap<>();
		ps.put("tableName", tableName);
		ps.put("taskName", taskName);
		return importMapper.selectByParams2(ps);
	}
}
