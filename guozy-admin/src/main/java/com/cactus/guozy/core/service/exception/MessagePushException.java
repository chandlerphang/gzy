package com.cactus.guozy.core.service.exception;

public class MessagePushException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public MessagePushException(String message) {
        super(message, new Throwable(message));
    }

    public MessagePushException(Throwable cause) {
        super(cause);
    }

    public MessagePushException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
