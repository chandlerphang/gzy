package com.cactus.guozy.core.service;

import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.Saler;

public interface LockManager {
	
	Object acquireLock(Order order);
	
	Object acquireLockIfAvailable(Order order);
	
	Object acquireLockIfAvailable(Saler saler, Long belongTo);
	
	void releaseLock(Object lockObject);
	
	boolean isActive();

}
