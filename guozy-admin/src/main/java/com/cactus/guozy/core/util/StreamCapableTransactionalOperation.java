package com.cactus.guozy.core.util;

public interface StreamCapableTransactionalOperation extends TransactionalOperation {

    boolean shouldRetryOnTransactionLockAcquisitionFailure();

    int retryMaxCountOverrideForLockAcquisitionFailure();

}
