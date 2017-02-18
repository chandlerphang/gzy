package com.cactus.guozy.core.util;

import org.springframework.transaction.PlatformTransactionManager;

public interface StreamingTransactionCapable {

    int getRetryMax();

    void setRetryMax(int retryMax);

    <G extends Throwable> void runTransactionalOperation(StreamCapableTransactionalOperation operation,
                                                         Class<G> exceptionType) throws G;

    <G extends Throwable> void runTransactionalOperation(StreamCapableTransactionalOperation operation,
                                                         Class<G> exceptionType, int transactionBehavior, int
            isolationLevel) throws G;

    <G extends Throwable> void runOptionalTransactionalOperation(StreamCapableTransactionalOperation operation,
                                                                 Class<G> exceptionType, boolean useTransaction)
            throws G;

    <G extends Throwable> void runOptionalTransactionalOperation(StreamCapableTransactionalOperation operation,
                                                                 Class<G> exceptionType, boolean useTransaction,
                                                                 int transactionBehavior, int isolationLevel) throws G;

    PlatformTransactionManager getTransactionManager();

    void setTransactionManager(PlatformTransactionManager transactionManager);

}
