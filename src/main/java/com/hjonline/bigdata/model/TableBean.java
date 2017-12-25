package com.hjonline.bigdata.model;

public class TableBean {
	private String table;
	private String hbase_table;
	private Integer num_mappers;
	private String where;
	private String columns;
	private String column_family;
	private String row_key;
	private String split_by;
	private String add_row_key;
	private String before_import;
	private String init;
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getHbase_table() {
		return hbase_table;
	}
	public void setHbase_table(String hbase_table) {
		this.hbase_table = hbase_table;
	}
	public Integer getNum_mappers() {
		return num_mappers;
	}
	public void setNum_mappers(Integer num_mappers) {
		this.num_mappers = num_mappers;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getColumn_family() {
		return column_family;
	}
	public void setColumn_family(String column_family) {
		this.column_family = column_family;
	}
	public String getRow_key() {
		return row_key;
	}
	public void setRow_key(String row_key) {
		this.row_key = row_key;
	}
	public String getSplit_by() {
		return split_by;
	}
	public void setSplit_by(String split_by) {
		this.split_by = split_by;
	}
	public String getAdd_row_key() {
		return add_row_key;
	}
	public void setAdd_row_key(String add_row_key) {
		this.add_row_key = add_row_key;
	}
	public String getBefore_import() {
		return before_import;
	}
	public void setBefore_import(String before_import) {
		this.before_import = before_import;
	}
	public String getInit() {
		return init;
	}
	public void setInit(String init) {
		this.init = init;
	}
	

}
