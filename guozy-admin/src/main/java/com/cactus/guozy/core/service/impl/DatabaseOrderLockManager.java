package com.cactus.guozy.core.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.service.OrderLockManager;
import com.cactus.guozy.core.service.OrderService;

@Component
public class DatabaseOrderLockManager implements OrderLockManager {

    protected static final Logger LOG = LoggerFactory.getLogger(DatabaseOrderLockManager.class);
    
    @Resource(name = "orderService")
    protected OrderService orderService;

    @Override
    public Object acquireLock(Order order) {
        if (order == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Thread[" + Thread.currentThread().getId() + "] Attempted to grab a lock for a null order. ");
            }
            return order;
        }

        boolean lockAcquired = false;
        int count = 0;
        while (!lockAcquired) {
            try {
                lockAcquired = orderService.acquireLock(order);
            } catch (Exception e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Couldn't acquire lock - that's ok, we'll retry shortly", e);
                }
            }

            if (!lockAcquired) {
                count++;
                if (count >= getDatabaseLockAcquisitionNumRetries()) {
                    LOG.warn(String.format("Exceeded max retries to attempt to acquire a lock on current Order (%s)", order.getId()));
                    throw new RuntimeException("Exceeded max retries to attempt to acquire a lock on current Order");
                }
                try {
                    long msToSleep = getDatabaseLockPollingIntervalMs();
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Thread[" + Thread.currentThread().getId() + "] Could not acquire order lock for order[" +
                                order.getId() + "] - sleeping for " + msToSleep + " ms");
                    }
                    Thread.sleep(msToSleep);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return order;
    }

    @Override
    public Object acquireLockIfAvailable(Order order) {
        if (order == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Attempted to grab a lock for a null order. Not blocking");
            }
            return order;
        }

        boolean lockAcquired = orderService.acquireLock(order); 
        return lockAcquired ? order : null;
    }

    @Override
    public void releaseLock(Object lockObject) {
        Order order = (Order) lockObject;
        if (order == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Thread[" + Thread.currentThread().getId() + "] Attempted to release a lock for a null order");
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Thread[" + Thread.currentThread().getId() + "] releasing lock for order[" + order.getId() + "]");
            }
            orderService.releaseLock(order);
        }
    }

    protected long getDatabaseLockPollingIntervalMs() {
        return RuntimeEnvConfigService.resolveLongSystemProperty("order.lock.databaseLockPollingIntervalMs", 2000);
    }
    
    protected int getDatabaseLockAcquisitionNumRetries() {
        return RuntimeEnvConfigService.resolveIntSystemProperty("order.lock.databaseLockAcquisitionNumRetries", 5);
    }

    @Override
    public boolean isActive() {
        return true;
    }
}