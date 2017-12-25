package com.hjonline.bigdata.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.hjonline.bigdata.model.YamlModel;

@Mapper
public interface YamlMapper {

	public int crYaml(YamlModel ym);

	public YamlModel selectById(long id);
}
