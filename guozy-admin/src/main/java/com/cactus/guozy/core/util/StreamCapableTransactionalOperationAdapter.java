package com.cactus.guozy.core.util;

public abstract class StreamCapableTransactionalOperationAdapter implements StreamCapableTransactionalOperation {

    @Override
    public void execute() throws Throwable {
        //do nothing
    }

    @Override
    public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
        return false;
    }

    @Override
    public int retryMaxCountOverrideForLockAcquisitionFailure() {
        return -1;
    }
}
