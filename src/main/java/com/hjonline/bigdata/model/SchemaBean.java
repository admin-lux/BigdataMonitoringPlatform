package com.hjonline.bigdata.model;

import java.util.List;

public class SchemaBean {
	
	private String schema;
	private String name;
	private LinkBean link;
	private List<String> tables;
	private List<TableBean> table_list;
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LinkBean getLink() {
		return link;
	}
	public void setLink(LinkBean link) {
		this.link = link;
	}
	public List<String> getTables() {
		return tables;
	}
	public void setTables(List<String> tables) {
		this.tables = tables;
	}
	public List<TableBean> getTable_list() {
		return table_list;
	}
	public void setTable_list(List<TableBean> table_list) {
		this.table_list = table_list;
	}
	
	

}
