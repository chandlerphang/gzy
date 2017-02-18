package com.cactus.guozy.profile.service;

import com.cactus.guozy.common.extension.ExtensionHandler;
import com.cactus.guozy.common.extension.ExtensionResultStatus;
import com.cactus.guozy.profile.domain.User;

public interface UserServiceExtension extends ExtensionHandler {
	
	 ExtensionResultStatus postRegisterUser(User user);
	
}
