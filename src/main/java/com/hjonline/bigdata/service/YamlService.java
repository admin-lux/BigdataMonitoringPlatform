package com.hjonline.bigdata.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hjonline.bigdata.mapper.YamlMapper;
import com.hjonline.bigdata.model.YamlModel;

@Service
public class YamlService {
	@Autowired
	private YamlMapper yamlMapper;
	
	
	public int crYaml(YamlModel ym) {
		return yamlMapper.crYaml(ym);
	}
	
	public YamlModel selectById(long id) {
		return yamlMapper.selectById(id);
	}
	
}
