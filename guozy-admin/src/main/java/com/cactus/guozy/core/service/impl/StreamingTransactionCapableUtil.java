package com.cactus.guozy.core.service.impl;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.cactus.guozy.core.util.ExceptionHelper;
import com.cactus.guozy.core.util.LockAcquisitionException;
import com.cactus.guozy.core.util.StreamCapableTransactionalOperation;
import com.cactus.guozy.core.util.StreamingTransactionCapable;
import com.cactus.guozy.core.util.TransactionUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("streamingTransactionCapableUtil")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class StreamingTransactionCapableUtil implements StreamingTransactionCapable {

    @Resource(name = "transactionManager")
    protected PlatformTransactionManager transactionManager;

    @Value("${streaming.transaction.lock.retry.max:10}")
    protected int retryMax = 10;

    @Override
    public <G extends Throwable> void runTransactionalOperation(
    		StreamCapableTransactionalOperation operation,
            Class<G> exceptionType) throws G {
    	
        runTransactionalOperation(
        		operation, 
        		exceptionType, 
        		TransactionDefinition.PROPAGATION_REQUIRED, 
        		TransactionDefinition.ISOLATION_DEFAULT);
    }

    @Override
    public <G extends Throwable> void runTransactionalOperation(
    		StreamCapableTransactionalOperation operation,
            Class<G> exceptionType, 
            int transactionBehavior, 
            int isolationLevel) throws G {
    	
        runOptionalTransactionalOperation(
        		operation, 
        		exceptionType, 
        		true, 
        		transactionBehavior, 
        		isolationLevel);
    }

    @Override
    public <G extends Throwable> void runOptionalTransactionalOperation(
    		StreamCapableTransactionalOperation operation,
            Class<G> exceptionType, 
            boolean useTransaction) throws G {
    	
        runOptionalTransactionalOperation(
        		operation, 
        		exceptionType, 
        		useTransaction, 
        		TransactionDefinition.PROPAGATION_REQUIRED, 
        		TransactionDefinition.ISOLATION_DEFAULT);
    }

    @Override
    public <G extends Throwable> void runOptionalTransactionalOperation(
    		StreamCapableTransactionalOperation operation,
            Class<G> exceptionType, 
            boolean useTransaction, 
            int transactionBehavior, 
            int isolationLevel) throws G {
    	
        int maxCount = operation.retryMaxCountOverrideForLockAcquisitionFailure();
        if (maxCount == -1) {
            maxCount = retryMax;
        }
        int tryCount = 0;
        boolean retry = false;
        do {
            tryCount++;
            try {
                TransactionStatus status = null;
                if (useTransaction) {
                    status = startTransaction(transactionBehavior, isolationLevel);
                }
                boolean isError = false;
                try {
                    operation.execute();
                    retry = false;
                } catch (Throwable e) {
                    isError = true;
                    ExceptionHelper.processException(exceptionType, RuntimeException.class, e);
                } finally {
                    if (useTransaction) {
                        endTransaction(status, isError, exceptionType);
                    }
                }
            } catch (RuntimeException e) {
                checkException: {
                    if (operation.shouldRetryOnTransactionLockAcquisitionFailure()) {
                        Exception result = ExceptionHelper.refineException(LockAcquisitionException.class, RuntimeException.class, e);
                        if (result.getClass().equals(LockAcquisitionException.class)) {
                            if (tryCount < maxCount) {
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException ie) {
                                    //do nothing
                                }
                                if (log.isDebugEnabled()) {
                                    log.debug("Unable to acquire a transaction lock. Retrying - count(" + tryCount + ").");
                                }
                                retry = true;
                                break checkException;
                            }
                            log.warn("Unable to acquire a transaction lock after " + maxCount + " tries.");
                        }
                    }
                    throw e;
                }
            }
        } while (tryCount < maxCount && retry && operation.shouldRetryOnTransactionLockAcquisitionFailure());
    }

    @Override
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    @Override
    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public int getRetryMax() {
        return retryMax;
    }

    @Override
    public void setRetryMax(int retryMax) {
        this.retryMax = retryMax;
    }

    protected <G extends Throwable> void endTransaction(TransactionStatus status, boolean error, Class<G> exceptionType) throws G {
        try {
            TransactionUtils.finalizeTransaction(status, transactionManager, error);
        } catch (Throwable e) {
            ExceptionHelper.processException(exceptionType, RuntimeException.class, e);
        }
    }

    protected TransactionStatus startTransaction(int propagationBehavior, int isolationLevel) {
        TransactionStatus status;
        try {
            status = TransactionUtils.createTransaction(propagationBehavior, isolationLevel,
                    transactionManager, false);
        } catch (RuntimeException e) {
            log.error("Could not start transaction", e);
            throw e;
        }
        return status;
    }
}
