package com.cactus.guozy.core.pay;

public enum PayType {

    ALIPAY_APP(1, "支付宝手机APP支付"),
    WECHAT_APP(2, "微信app支付");

    private Integer value;
    private String desc;

    PayType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static PayType valueOf(int value) {
        for (PayType type : PayType.values()) {
            if (type.value() == value) {
                return type;
            }
        }
        return null;
    }

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

}
