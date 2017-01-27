package com.cactus.guozy.admin.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 后台菜单
 */
public class AdminMenu {

	private List<AdminModule> adminModules = new ArrayList<AdminModule>();

	public List<AdminModule> getAdminModules() {
		return adminModules;
	}

	public void setAdminModule(List<AdminModule> adminModules) {
		this.adminModules = adminModules;
	}

}
