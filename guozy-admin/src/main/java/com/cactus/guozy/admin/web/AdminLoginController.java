package com.cactus.guozy.admin.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cactus.guozy.admin.domain.AdminFunction;
import com.cactus.guozy.admin.domain.AdminMenu;
import com.cactus.guozy.admin.domain.AdminModule;
import com.cactus.guozy.admin.service.AdminNavigationService;
import com.cactus.guozy.common.WakaRequestContext;

@Controller
public class AdminLoginController {

	@Resource(name = "adminNavigationService")
	protected AdminNavigationService adminNavigationService;
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		return "login";
	}
	
	@RequestMapping(value = {"/home"}, method = RequestMethod.GET)
	public String loginSuccess() {
		AdminMenu adminMenu = adminNavigationService.buildMenu(WakaRequestContext.instance().getUser());
		if (!adminMenu.getAdminModules().isEmpty()) {
			AdminModule first = adminMenu.getAdminModules().get(0);
			List<AdminFunction> funcs = first.getFuncs();
			if(!funcs.isEmpty()) {
				return "redirect:" + funcs.get(0).getUrl();
			}
		}
		return "noAccess";
	}
}
