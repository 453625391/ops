package com.meitianhui.common.constant;

import java.io.Serializable;

/**
 * 分页参数类 分页构建的对象名称为 pageParam
 * 
 */
public class PageParam implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public static final int DEFAULT_PAGE_SIZE = 20;
	private int page_no;
	private int page_size;
	private int total_page;
	private int total_count;

	public PageParam() {
		this.page_no = 1;
		this.page_size = DEFAULT_PAGE_SIZE;
		this.total_page = 0;
		this.total_count = 0;
	}

	/**
	 * 
	 * @param page
	 * @param pageSize
	 */
	public PageParam(int page_no, int page_size) {
		if (page_no < 1) {
			page_no = 1;
		}
		this.page_no = page_no;
		this.page_size = page_size;
	}

	public int getPage_no() {
		return page_no;
	}

	public void setPage_no(int page_no) {
		if (page_no < 1) {
			page_no = 1;
		}
		this.page_no = page_no;
	}

	public int getPage_size() {
		return page_size;
	}

	public void setPage_size(int page_size) {
		this.page_size = page_size;
	}

	public int getTotal_page() {
		return total_page;
	}

	public void setTotal_page(int total_page) {
		this.total_page = total_page;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

}
