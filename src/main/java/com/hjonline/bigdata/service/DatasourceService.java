package com.hjonline.bigdata.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hjonline.bigdata.mapper.DatasourceMapper;
import com.hjonline.bigdata.model.Datasources;
import com.hjonline.bigdata.model.request.SelectPageDatasource;

@Service
public class DatasourceService {
	@Autowired
	private DatasourceMapper datasourceMapper;
	@Autowired
	private OpeService opeService;

	public Page<Datasources> selectPage(SelectPageDatasource ds) {
		if (null == ds.getPage()) {
			ds.setPage(1);
		}
		if (null == ds.getSize()) {
			ds.setSize(5);
		}

		PageHelper.startPage(ds.getPage(), ds.getSize(), true);
		PageHelper.orderBy("lastdate desc");
		HashMap<String, Object> params = new HashMap<>();
		params.put("sysName", ds.getSysName());
		params.put("databaseName", ds.getDatabaseName());
		if (ds.getSourceType() != 0) {
			params.put("sourceType", ds.getSourceType());
		}
		params.put("state", 1);
		return datasourceMapper.selectByParams(params);
	}

	public Page<Datasources> selectDatasourceAll() {
		HashMap<String, Object> params = new HashMap<>();
		params.put("state", 1);
		return datasourceMapper.selectByParams(params);
	}

	public Datasources selectById(long id) {
		return datasourceMapper.selectById(id);
	}

	public int addDatasource(Datasources datasources) {
		Datasources ds = datasourceMapper.selectBySysName(datasources.getSysName());
		if (null == ds) {
			datasourceMapper.addDatasource(datasources);
		} else {
			datasources.setId(ds.getId());
			datasources.setState(1);
			datasourceMapper.upDatasource(datasources);
		}
		opeService.addOpe("数据源管理", String.format("增加了数据源[%s]记录", datasources.getSysName()), 1);
		return 1;
	}

	public int upDatasource(Datasources datasources) {
		if (datasources.getState() == 0) {
			opeService.addOpe("数据源管理", String.format("删除了数据源[%s]记录", datasources.getSysName()), 0);
		} else {
			opeService.addOpe("数据源管理", String.format("修改了数据源[%s]记录", datasources.getSysName()), 2);
		}
		return datasourceMapper.upDatasource(datasources);
	}

	public int testConnect(Datasources datasources) throws SQLException, ClassNotFoundException {

		if (datasources.getSourceType() == 1) {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} else {
			Class.forName("com.mysql.jdbc.Driver");
		}

		Connection conn = DriverManager.getConnection(datasources.getConnect(), datasources.getUserName(),
				datasources.getPwd());
		if (null != conn) {
			return 1;
		}
		return 0;
	}

	public static void main(String[] args) {
		Datasources ds = new Datasources();
		ds.setSourceType(2);
		ds.setUserName("root");
		ds.setPwd("123456");
		ds.setConnect("jdbc:mysql://127.0.0.1/bigdata");

		try {
			System.out.println(new DatasourceService().testConnect(ds));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
