package com.hjonline.bigdata.model.request;

import java.util.List;

import com.hjonline.bigdata.model.Import;

/**
 * crYaml接口参数类
 * @author rick_lu
 *
 */
public class CrYamlParams {
	/***
	 * 类型：0:仅生成配置文件;1:生成配置文件并发布到线上
	 */
	private int type = 0;
//	/**
//	 * 线上类型:0:发布到0点正常任务;1:发布到0点依赖表任务;2:发布到6点任务;3:发布到10点任务
//	 * 
//	 */
//	private int devType = 0;

	/**
	 * 表
	 */
	private List<Import> datas;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public List<Import> getDatas() {
		return datas;
	}

	public void setDatas(List<Import> datas) {
		this.datas = datas;
	}

}
