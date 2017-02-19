package com.cactus.guozy.profile.service;

import org.springframework.stereotype.Component;

import com.cactus.guozy.common.extension.ExtensionManager;
import com.cactus.guozy.common.extension.ExtensionResultStatus;
import com.cactus.guozy.profile.domain.User;

@Component("userServiceExtensionManager")
public class UserServiceExtensionManager extends ExtensionManager<UserServiceExtension> implements UserServiceExtension{

	public UserServiceExtensionManager() {
		super(UserServiceExtension.class);
	}
	
	@Override
	public boolean continueOnHandled() {
        return true;
    }

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public ExtensionResultStatus postRegisterUser(User user) {
		return getProxy().postRegisterUser(user);
	}
}
