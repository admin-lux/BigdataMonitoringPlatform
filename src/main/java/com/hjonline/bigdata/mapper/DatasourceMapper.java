package com.hjonline.bigdata.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.hjonline.bigdata.model.Datasources;

@Mapper
public interface DatasourceMapper {

	Page<Datasources> selectByParams(HashMap<String, Object> params);

	Datasources selectById(long id);

	int addDatasource(Datasources datasources);

	int upDatasource(Datasources datasources);

	Datasources selectBySysName(@Param("sysName") String sysName);

}
