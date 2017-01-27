package com.cactus.guozy.core.service.impl;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.security.web.session.HttpSessionDestroyedEvent;

import com.cactus.guozy.common.WakaRequestContext;
import com.cactus.guozy.common.WakaRequestUtils;
import com.cactus.guozy.core.domain.Order;

public class SessionOrderLockManager implements OrderLockManager, ApplicationListener<HttpSessionDestroyedEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SessionOrderLockManager.class);
    private static final Object LOCK = new Object();
    private static final ConcurrentMap<String, ReentrantLock> SESSION_LOCKS;
    
    static {
        SESSION_LOCKS = new java.util.concurrent.ConcurrentHashMap<>();
            //.maximumWeightedCapacity(10000)
    }

    @Override
    public Object acquireLock(Order order) {
        ReentrantLock lockObject = getSessionLock();
        lockObject.lock();
        return lockObject;
    }

    @Override
    public Object acquireLockIfAvailable(Order order) {
        ReentrantLock lockObject = getSessionLock();
        boolean locked = lockObject.tryLock();
        return locked ? lockObject : null;
    }

    @Override
    public void releaseLock(Object lockObject) {
        ReentrantLock lock = (ReentrantLock) lockObject;
        lock.unlock();
    }
    
    @Override
    public void onApplicationEvent(HttpSessionDestroyedEvent event) {
        ReentrantLock lock = SESSION_LOCKS.remove(event.getSession().getId());
        if (lock != null && LOG.isDebugEnabled()) {
            LOG.debug("Destroyed lock due to session invalidation: " + lock.toString());
        }
    }

    protected ReentrantLock getSessionLock() {
        if (!isActive()) {
            throw new IllegalStateException("This is currently a sessionless environment and session cannot be used " +
                    "to obtain a lock. Consider using a different implementation of OrderLockManager.");
        }

        HttpSession session = WakaRequestContext.instance().getRequest().getSession();
        ReentrantLock lock = SESSION_LOCKS.get(session.getId());

        if (lock == null) {
            // There was no session lock object. We'll need to create one. To do this, we have to synchronize the
            // creation globally, so that two threads don't create the session lock at the same time.
            synchronized (LOCK) {
                lock = SESSION_LOCKS.get(session.getId());
                if (lock == null) {
                    lock = new ReentrantLock();
                    SESSION_LOCKS.put(session.getId(), lock);
                }
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("Created new lock object: " + lock.toString());
            }
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Returning previously created lock object: " + lock.toString());
            }
        }

        return lock;
    }

    @Override
    public boolean isActive() {
        if (WakaRequestContext.instance().getWebRequest() != null) {
            if (!WakaRequestUtils.isOKtoUseSession(WakaRequestContext.instance().getWebRequest())) {
                return false;
            }
            return true;
        }
        return false;
    }
}
