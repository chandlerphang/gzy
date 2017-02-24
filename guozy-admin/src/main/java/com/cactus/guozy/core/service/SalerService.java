package com.cactus.guozy.core.service;

import com.cactus.guozy.common.service.BaseService;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.profile.domain.User;

public interface SalerService extends BaseService<Saler> {
	
	Saler tryLoginSaler(String phone, String passwd, String channelId);

	void logoutSaler(Long salerId);

	void noBusy(Long salerId);

	void setBusy(Long salerId);
	
	boolean acquireLock(final Saler saler, Long belongTo);
	
	boolean releaseLock(final Saler saler);

	void tryToConnect(Saler saler, User  user, String usrChannelId);
	
	void userDisConnect(Saler saler, Long usrId, String homeId);
	
	void salerDisConnect(Saler saler, Long usrId, String usrChannelId, String homeId);

	void userConfirmConnect(Saler saler, Long usrId);

	void salerRejectConnect(Saler saler, Long usrId, String usrChannelId);

	void salerConfirmConnect(Saler saler, Long usrId, String usrChannelId, String homeId);
	
}
