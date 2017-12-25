package com.hjonline.bigdata.model;

import java.util.Date;

public class Monitor extends BaseModel {
	private String taskname;
	private String tablename;
	private String stime;
	private String etime;
	private String rtime;
	private long qcount;
	private long ecount;
	private String result;
	private long diff;
	private Date createtime;
	private Date updatetime;
	
	private long positive;
	private long negative;
	private long faileds;
	

	public String getTaskname() {
		return taskname;
	}

	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}

	public String getStime() {
		return stime;
	}

	public void setStime(String stime) {
		this.stime = stime;
	}

	public String getEtime() {
		return etime;
	}

	public void setEtime(String etime) {
		this.etime = etime;
	}

	public String getRtime() {
		return rtime;
	}

	public void setRtime(String rtime) {
		this.rtime = rtime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}


	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public long getQcount() {
		return qcount;
	}

	public void setQcount(long qcount) {
		this.qcount = qcount;
	}

	public long getEcount() {
		return ecount;
	}

	public void setEcount(long ecount) {
		this.ecount = ecount;
	}

	public long getDiff() {
		return diff;
	}

	public void setDiff(long diff) {
		this.diff = diff;
	}

	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}

	public long getPositive() {
		return positive;
	}

	public void setPositive(long positive) {
		this.positive = positive;
	}

	public long getNegative() {
		return negative;
	}

	public void setNegative(long negative) {
		this.negative = negative;
	}

	public long getFaileds() {
		return faileds;
	}

	public void setFaileds(long faileds) {
		this.faileds = faileds;
	}

	
}
