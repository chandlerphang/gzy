package com.cactus.guozy.core.domain;

public class Page {
	private Long id;
	private Integer perNum;
	private Integer pageNum;
	

	public Page(Long id, Integer perNum, Integer pageNum) {
		super();
		this.id = id;
		this.perNum = perNum;
		this.pageNum = pageNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getPerNum() {
		return perNum;
	}

	public void setPerNum(Integer perNum) {
		this.perNum = perNum;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

}
