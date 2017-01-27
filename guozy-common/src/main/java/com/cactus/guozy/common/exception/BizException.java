package com.cactus.guozy.common.exception;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private String errno;

	public BizException(String errno, String errmsg) {
		super(errmsg);
        this.errno = errno;
    }

    public BizException(String errno, String message, Throwable cause) {
        super(message,  cause);
        this.errno = errno;
    }

	public String getErrno() {
		return errno;
	}

	public void setErrno(String errno) {
		this.errno = errno;
	}
    
}
