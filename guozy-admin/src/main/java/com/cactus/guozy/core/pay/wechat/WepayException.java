package com.cactus.guozy.core.pay.wechat;

public class WepayException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
     * 当微信发生错误时，对应的错误码
     */
    private String errorCode;

    /**
     * 当微信发生错误时，对应的错误消息
     */
    private String errorMsg;

    public WepayException(Throwable cause) {
        super(cause);
    }

    public WepayException(String errorCode, String errorMsg){
        super("[" + errorCode + "]"+ errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
