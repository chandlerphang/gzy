package com.cactus.guozy.core.service;

import com.cactus.guozy.core.domain.Saler;

public interface SalerLockManager {
	
	Object acquireLock(Saler saler);
	
	Object acquireLockIfAvailable(Saler saler);
	
	void releaseLock(Object lockObject);
	
	boolean isActive();

}
