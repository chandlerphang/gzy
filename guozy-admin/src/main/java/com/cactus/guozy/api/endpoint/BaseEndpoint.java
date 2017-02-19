package com.cactus.guozy.api.endpoint;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.cactus.guozy.common.cms.AssetService;
import com.cactus.guozy.common.cms.AssetStorageService;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.service.AppSettingService;
import com.cactus.guozy.core.service.CatalogService;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.SalerService;
import com.cactus.guozy.profile.domain.User;
import com.cactus.guozy.profile.service.UserService;

public abstract class BaseEndpoint {
	
	@Resource(name="appSettingService")
	protected AppSettingService appSettingService;
	
	@Resource(name = "orderService")
	protected OrderService orderService;
	
	@Autowired
	protected UserService userService;
	
	@Autowired
	protected SalerService salerService;
	
	@Resource(name="catalogService")
	protected CatalogService catalogService;
	
	@Resource(name="assetStorageService")
	protected AssetStorageService ass;
	
	@Resource(name="assetService")
	protected AssetService assetService;
	
	protected Long getCurrentUserId() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx != null && ctx.getAuthentication() != null) {
			Object temp = ctx.getAuthentication().getDetails();
			if(temp instanceof Saler) {
				return ((Saler) temp).getId();
			} else {
				return ((User) temp).getId();
			}
		}

		return null;
	}

}
