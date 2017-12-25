package com.hjonline.bigdata.base;

import java.io.Serializable;
import java.util.List;

import com.github.pagehelper.Page;

public class PageInfo<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	// 当前页
	private int currentPage;
	// 总记录数
	private long total;
	// 结果集
	private List<T> tableData;

	public PageInfo(List<T> list) {
		if (list instanceof Page) {
			Page page = (Page) list;
			if (0 == page.getPageNum()) {
				this.currentPage = 1;
			} else {
				this.currentPage = page.getPageNum();
			}
			this.total = page.getTotal();
			this.tableData = page;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public List<T> getTableData() {
		return tableData;
	}

	public void setTableData(List<T> tableData) {
		this.tableData = tableData;
	}

}
