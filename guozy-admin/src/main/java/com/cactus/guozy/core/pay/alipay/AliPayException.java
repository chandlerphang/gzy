package com.cactus.guozy.core.pay.alipay;

public class AliPayException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AliPayException() {
        super();
    }

    public AliPayException(String message) {
        super(message);
    }

    public AliPayException(String message, Throwable cause) {
        super(message, cause);
    }

    public AliPayException(Throwable cause) {
        super(cause);
    }

    protected AliPayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
