package com.cactus.guozy.core.service.impl;

import com.cactus.guozy.core.domain.Order;

public interface OrderLockManager {

    public Object acquireLock(Order order);

    public Object acquireLockIfAvailable(Order order);

    public void releaseLock(Object lockObject);

    public boolean isActive();
}
