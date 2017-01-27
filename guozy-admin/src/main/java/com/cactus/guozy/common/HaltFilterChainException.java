package com.cactus.guozy.common;

public class HaltFilterChainException extends RuntimeException {

    private static final long serialVersionUID = -4138837912315748276L;

    protected HaltFilterChainException() {
        super();
    }
    
    public HaltFilterChainException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public HaltFilterChainException(String message) {
        super(message);
    }
    
    public HaltFilterChainException(Throwable cause) {
        super(cause);
    }

}