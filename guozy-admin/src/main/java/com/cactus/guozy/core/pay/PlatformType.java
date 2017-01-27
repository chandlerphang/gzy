package com.cactus.guozy.core.pay;

public enum PlatformType {

    TB("TB", "淘宝"),
    WECHAT("WECHAT", "微信支付");
    
    private String value;
    private String desc;

    PlatformType(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static PlatformType getPlatform(Integer value) {
        if (1 == value) {
            return PlatformType.TB;
        } else if (2 == value) {
            return PlatformType.WECHAT;
        }
        return null;
    }

    public String value() {
        return value;
    }

    public String desc() {
        return desc;
    }
}
