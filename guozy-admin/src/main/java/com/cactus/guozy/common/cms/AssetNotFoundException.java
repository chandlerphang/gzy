package com.cactus.guozy.common.cms;

public class AssetNotFoundException extends RuntimeException {
    private static final long serialVersionUID = -6349160176427682630L;

    public AssetNotFoundException() {
        //do nothing
    }

    public AssetNotFoundException(Throwable cause) {
        super(cause);
    }

    public AssetNotFoundException(String message) {
        super(message);
    }

    public AssetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
