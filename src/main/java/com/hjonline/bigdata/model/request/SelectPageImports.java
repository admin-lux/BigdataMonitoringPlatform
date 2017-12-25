package com.hjonline.bigdata.model.request;

public class SelectPageImports extends BaseRequest {

	private String sysName;
	private String databaseName;
	private String tableName;
	private int taksId = -1;
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getSysName() {
		return sysName;
	}
	public void setSysName(String sysName) {
		this.sysName = sysName;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public int getTaksId() {
		return taksId;
	}
	public void setTaksId(int taksId) {
		this.taksId = taksId;
	}

}
