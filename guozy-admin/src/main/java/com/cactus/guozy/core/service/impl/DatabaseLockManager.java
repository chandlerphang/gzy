package com.cactus.guozy.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.service.LockManager;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.SalerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatabaseLockManager implements LockManager  {
	
	@Autowired
	protected OrderService orderService;
	
	@Autowired
	protected SalerService salerService;

	@Override
	public Object acquireLock(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object acquireLockIfAvailable(Order order) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object acquireLockIfAvailable(Saler saler, Long belongTo) {
        boolean lockAcquired = salerService.acquireLock(saler, belongTo); 
        return lockAcquired ? saler : null;
	}

	@Override
	public void releaseLock(Object lockObject) {
        if (lockObject instanceof Saler) {
        	Saler saler = (Saler) lockObject;
        	if (log.isDebugEnabled()) {
                log.debug("Thread[" + Thread.currentThread().getId() + "] releasing lock for saler[" + saler.getId() + "]");
            }
        	
        	salerService.releaseLock(saler);
        } else if(lockObject instanceof Order){
        	Order order = (Order) lockObject;
        	if (log.isDebugEnabled()) {
                log.debug("Thread[" + Thread.currentThread().getId() + "] releasing lock for order[" + order.getId() + "]");
            }
            orderService.releaseLock(order);
        }
	}

	@Override
	public boolean isActive() {
		return true;
	}

}
