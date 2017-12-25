package com.hjonline.bigdata.mapper;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.hjonline.bigdata.model.Import;

@Mapper
public interface ImportMapper {

	public int addImportConf(Import im);

	public int upImportConf(Import im);

	public Import selectById(long id);

	public Page<Import> selectPageImports(HashMap<String, Object> params);

	public Page<Import> selectByParams(HashMap<String, Object> params);

	public Page<Import> selectByParams2(HashMap<String, Object> params);

	public long countImports(HashMap<String, Object> params);

	public List<Import> selectByTableName(String tableName);
}
