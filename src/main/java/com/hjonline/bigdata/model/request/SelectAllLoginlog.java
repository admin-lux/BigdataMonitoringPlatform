package com.hjonline.bigdata.model.request;

public class SelectAllLoginlog extends BaseRequest{
	private String name;
	private String account;
	private Long stdate;
	private Long enddate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Long getStdate() {
		return stdate;
	}
	public void setStdate(Long stdate) {
		this.stdate = stdate;
	}
	public Long getEnddate() {
		return enddate;
	}
	public void setEnddate(Long enddate) {
		this.enddate = enddate;
	}
	
	
	
}
