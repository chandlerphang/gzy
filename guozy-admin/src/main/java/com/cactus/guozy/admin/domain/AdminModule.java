package com.cactus.guozy.admin.domain;

import java.util.ArrayList;
import java.util.List;

public class AdminModule {

	protected Long id;

	protected String name;

	protected String moduleKey;

	protected String icon;

	protected List<AdminFunction> funcs = new ArrayList<AdminFunction>();

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

	public String getModuleKey() {
		return moduleKey;
	}

	public void setModuleKey(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<AdminFunction> getFuncs() {
		return funcs;
	}

	public void setFuncs(List<AdminFunction> sections) {
		this.funcs = sections;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public AdminModule copyAdminModule() {
		AdminModule dto = new AdminModule();
		dto.setDisplayOrder(displayOrder);
		dto.setIcon(icon);
		dto.setId(id);
		dto.setModuleKey(moduleKey);
		dto.setName(name);
		return dto;
	}

}
