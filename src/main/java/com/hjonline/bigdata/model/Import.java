package com.hjonline.bigdata.model;

import java.util.Date;

/**
 * 
 * @author rick_lu
 *
 */
public class Import extends BaseModel {
	private int datasourceId = -1;
	private int taskId = -1;
	private String tableName;
	private String hbaseName;
	private String columns;
	private int numMappers = 1;
	private String columnFamily = "info";
	private String rowKey;
	private String splitBy;
	private int addRowKey = 0;
	private String beforeImport;
	private int importType = 0;
	private String incrementIf;
	private int state;
	private int lastUserId;
	private Date lastdate;
	private Date crdate;
	private Date update;
	private String sysName;
	private String databaseName;
	private String name;
	private String taskName;

	public int getDatasourceId() {
		return datasourceId;
	}

	public void setDatasourceId(int datasourceId) {
		this.datasourceId = datasourceId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getHbaseName() {
		return hbaseName;
	}

	public void setHbaseName(String hbaseName) {
		this.hbaseName = hbaseName;
	}

	public String getColumns() {
		return columns;
	}

	public void setColumns(String columns) {
		this.columns = columns;
	}

	public int getNumMappers() {
		return numMappers;
	}

	public void setNumMappers(int numMappers) {
		this.numMappers = numMappers;
	}

	public String getColumnFamily() {
		return columnFamily;
	}

	public void setColumnFamily(String columnFamily) {
		this.columnFamily = columnFamily;
	}

	public String getRowKey() {
		return rowKey;
	}

	public void setRowKey(String rowKey) {
		this.rowKey = rowKey;
	}

	public String getSplitBy() {
		return splitBy;
	}

	public void setSplitBy(String splitBy) {
		this.splitBy = splitBy;
	}

	public int getAddRowKey() {
		return addRowKey;
	}

	public void setAddRowKey(int addRowKey) {
		this.addRowKey = addRowKey;
	}

	public String getBeforeImport() {
		return beforeImport;
	}

	public void setBeforeImport(String beforeImport) {
		this.beforeImport = beforeImport;
	}

	public int getImportType() {
		return importType;
	}

	public void setImportType(int importType) {
		this.importType = importType;
	}

	public String getIncrementIf() {
		return incrementIf;
	}

	public void setIncrementIf(String incrementIf) {
		this.incrementIf = incrementIf;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getLastUserId() {
		return lastUserId;
	}

	public void setLastUserId(int lastUserId) {
		this.lastUserId = lastUserId;
	}

	public Date getLastdate() {
		return lastdate;
	}

	public void setLastdate(Date lastdate) {
		this.lastdate = lastdate;
	}

	public Date getCrdate() {
		return crdate;
	}

	public void setCrdate(Date crdate) {
		this.crdate = crdate;
	}

	public Date getUpdate() {
		return update;
	}

	public void setUpdate(Date update) {
		this.update = update;
	}

	public String getSysName() {
		return sysName;
	}

	public void setSysName(String sysName) {
		this.sysName = sysName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
