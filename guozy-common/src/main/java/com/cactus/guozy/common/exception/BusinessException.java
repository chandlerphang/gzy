package com.cactus.guozy.common.exception;

/**
 * 业务异常的自定义封装类
 */
public class BusinessException extends BaseException {
	private static final long serialVersionUID = 6390986919802660564L;

	public BusinessException(String message) {
        super(message, new Throwable(message));
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

}
