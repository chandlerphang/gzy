package com.cactus.guozy.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.service.SalerLockManager;
import com.cactus.guozy.core.service.SalerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DatabaseSalerLockManager implements SalerLockManager {

	@Autowired
	protected SalerService salerService;

	@Override
	public Object acquireLock(Saler saler) {
		if (saler == null) {
			if (log.isDebugEnabled()) {
				log.debug("Thread[" + Thread.currentThread().getId() + "] Attempted to grab a lock for a null saler. ");
			}
			return saler;
		}

		boolean lockAcquired = false;
		int count = 0;
		while (!lockAcquired) {
			try {
				lockAcquired = salerService.acquireLock(saler);
			} catch (Exception e) {
				if (log.isDebugEnabled()) {
					log.debug("Couldn't acquire lock - that's ok, we'll retry shortly", e);
				}
			}

			if (!lockAcquired) {
				count++;
				if (count >= getDatabaseLockAcquisitionNumRetries()) {
					log.warn(String.format("Exceeded max retries to attempt to acquire a lock on current Saler (%s)", saler.getId()));
					throw new RuntimeException("Exceeded max retries to attempt to acquire a lock on current Saler");
				}
				try {
					long msToSleep = getDatabaseLockPollingIntervalMs();
					if (log.isDebugEnabled()) {
						log.debug(
								"Thread[" + Thread.currentThread().getId() + "] Could not acquire saler lock for saler["
										+ saler.getId() + "] - sleeping for " + msToSleep + " ms");
					}
					Thread.sleep(msToSleep);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}

		return saler;
	}

	@Override
	public Object acquireLockIfAvailable(Saler saler) {
		if (saler == null) {
            if (log.isDebugEnabled()) {
                log.debug("Attempted to grab a lock for a null order. Not blocking");
            }
            return saler;
        }

        boolean lockAcquired = salerService.acquireLock(saler); 
        return lockAcquired ? saler : null;
	}

	@Override
	public void releaseLock(Object lockObject) {
		Saler saler = (Saler) lockObject;
		if (saler == null) {
			if (log.isDebugEnabled()) {
				log.debug(
						"Thread[" + Thread.currentThread().getId() + "] Attempted to release a lock for a null Saler");
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug("Thread[" + Thread.currentThread().getId() + "] releasing lock for saler[" + saler.getId()
						+ "]");
			}
			salerService.releaseLock(saler);
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
