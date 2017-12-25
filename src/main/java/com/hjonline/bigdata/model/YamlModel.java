package com.hjonline.bigdata.model;

import java.util.Date;

public class YamlModel extends BaseModel {
	private String fileName;
	private String filePath;
	private byte[] dataYaml;
	private int userId;
	private Date crdate;
	private Date update;
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public byte[] getDataYaml() {
		return dataYaml;
	}
	public void setDataYaml(byte[] dataYaml) {
		this.dataYaml = dataYaml;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
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
}
