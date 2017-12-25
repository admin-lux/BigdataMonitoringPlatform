package com.hjonline.bigdata.mapper;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.hjonline.bigdata.model.Ope;

@Mapper
public interface OpeMapper {
	public int addOpe(Ope ope);
	
	public Page<Ope> selectOpes(HashMap<String, Object> params);
}
