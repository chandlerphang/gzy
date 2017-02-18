package com.cactus.guozy.core.util;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionUtils {

    public static final String DEFAULT_TRANSACTION_MANAGER = "transactionManager";

    public static TransactionStatus createTransaction(String name, int propagationBehavior, PlatformTransactionManager transactionManager) {
        return createTransaction(name, propagationBehavior, transactionManager, false);
    }

    public static TransactionStatus createTransaction(String name, int propagationBehavior, PlatformTransactionManager transactionManager, boolean isReadOnly) {
        return createTransaction(name, propagationBehavior, TransactionDefinition.ISOLATION_DEFAULT, transactionManager, isReadOnly);
    }

    public static TransactionStatus createTransaction(String name, int propagationBehavior, int isolationLevel, PlatformTransactionManager transactionManager, boolean isReadOnly) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName(name);
        def.setReadOnly(isReadOnly);
        def.setPropagationBehavior(propagationBehavior);
        def.setIsolationLevel(isolationLevel);
        return transactionManager.getTransaction(def);
    }

    public static TransactionStatus createTransaction(int propagationBehavior, PlatformTransactionManager transactionManager, boolean isReadOnly) {
        return createTransaction(propagationBehavior, TransactionDefinition.ISOLATION_DEFAULT, transactionManager, isReadOnly);
    }

    public static TransactionStatus createTransaction(int propagationBehavior, int isolationLevel, PlatformTransactionManager transactionManager, boolean isReadOnly) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(isReadOnly);
        def.setPropagationBehavior(propagationBehavior);
        def.setIsolationLevel(isolationLevel);
        return transactionManager.getTransaction(def);
    }

    public static void finalizeTransaction(TransactionStatus status, PlatformTransactionManager transactionManager, boolean isError) {
        boolean isActive = false;
        try {
            if (!status.isRollbackOnly()) {
                isActive = true;
            }
        } catch (Exception e) {
            //do nothing
        }
        if (isError || !isActive) {
            try {
                transactionManager.rollback(status);
            } catch (Exception e) {
                log.error("Rolling back caused exception. Logging and continuing.", e);
            }
        } else {
            transactionManager.commit(status);
        }
    }

}
