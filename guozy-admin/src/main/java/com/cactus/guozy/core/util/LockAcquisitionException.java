package com.cactus.guozy.core.util;

public class LockAcquisitionException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public LockAcquisitionException(String message) {
        super(message, new Throwable());
    }

    public LockAcquisitionException(String message, Throwable cause) {
        super(message, cause);
    }

}
