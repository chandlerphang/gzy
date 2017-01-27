package com.cactus.guozy.admin.web;

import javax.annotation.Resource;

import org.springframework.ui.Model;

import com.cactus.guozy.admin.domain.AdminFunction;
import com.cactus.guozy.admin.domain.AdminMenu;
import com.cactus.guozy.admin.domain.AdminUser;
import com.cactus.guozy.admin.service.AdminNavigationService;
import com.cactus.guozy.common.WakaRequestContext;
import com.cactus.guozy.profile.service.UserService;

public abstract class AbstractAdminController {

	public static final String CURRENT_ADMIN_MODULE_ATTR_NAME = "currentAdminModule";
	public static final String CURRENT_ADMIN_FUNC_ATTR_NAME = "currentAdminFunc";
	
	@Resource(name="adminNavigationService")
	protected AdminNavigationService adminNavigationService;
	
	@Resource(name="userService")
	protected UserService userService;

	protected void setModelAttributes(Model model, String funcKey) {
		AdminUser adminUser = WakaRequestContext.instance().getUser();
		AdminMenu adminMenu = adminNavigationService.buildMenu(adminUser);
		model.addAttribute("adminUser", adminUser);
		model.addAttribute("adminMenu", adminMenu);
		
		AdminFunction func = adminNavigationService.findFuncByKey(funcKey);
		if (func != null) {
			model.addAttribute("funcKey", func.getFuncKey());
			model.addAttribute(CURRENT_ADMIN_MODULE_ATTR_NAME, func.getModule());
			model.addAttribute(CURRENT_ADMIN_FUNC_ATTR_NAME, func);
		}
	}
}
