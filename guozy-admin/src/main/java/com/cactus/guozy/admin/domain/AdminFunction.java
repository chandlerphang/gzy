package com.cactus.guozy.admin.domain;

import java.util.ArrayList;
import java.util.List;

public class AdminFunction {

	protected Long id;

	protected String name;

	protected String funcKey;

	protected String url;

	protected String icon;
	
	protected AdminModule module;

	protected List<AdminPermission> permissions = new ArrayList<>();

	protected Integer displayOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFuncKey() {
		return funcKey;
	}

	public void setFuncKey(String functionKey) {
		this.funcKey = functionKey;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<AdminPermission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<AdminPermission> permissions) {
		this.permissions = permissions;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public AdminModule getModule() {
		return module;
	}

	public void setModule(AdminModule module) {
		this.module = module;
	}

	public AdminFunction copyWithoutHierarchy() {
		AdminFunction dto = new AdminFunction();
		dto.setId(id);
		dto.setName(name);
		dto.setFuncKey(funcKey);
		dto.setIcon(icon);
		dto.setUrl(url);
		dto.setDisplayOrder(displayOrder);
		return dto;
	}
	
	@Override
	public String toString() {
		return id+":"+name;
	}

}
