package com.cactus.guozy.core.service.offer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cactus.guozy.common.extension.ExtensionResultStatus;
import com.cactus.guozy.profile.domain.User;
import com.cactus.guozy.profile.service.UserServiceExtension;
import com.cactus.guozy.profile.service.UserServiceExtensionManager;

@Component
public class AutoAddUserRegisterOffer implements UserServiceExtension {

	@Autowired
	protected OfferService offerService;
	
	@Resource(name="userServiceExtensionManager")
	protected UserServiceExtensionManager mgr;
	
	@PostConstruct
	private void init() {
		mgr.registerHandler(this);
	}
	
	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public ExtensionResultStatus postRegisterUser(User user) {
		offerService.addOffer(user, 100L, 3);
		return ExtensionResultStatus.HANDLED_CONTINUE;
	}

}
