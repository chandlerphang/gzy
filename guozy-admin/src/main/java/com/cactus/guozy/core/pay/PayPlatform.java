package com.cactus.guozy.core.pay;

/**
 * 三方支付
 */
public enum PayPlatform {

    ALIPAY_GLOBAL("301", "支付宝(国际)"),
    ALIPAY_COMMON("302", "支付宝(普通)"),
    WECHAT_APP("401", "微信支付(开放平台)"),
    WECHAT_WAP("402", "微信支付(公众平台)");
    
    private String code;
    private String label;

    private PayPlatform(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    public static PayPlatform getByCode(String code) {
        for (PayPlatform o : PayPlatform.values()) {
            if (o.getCode().equals(code)) {
                return o;
            }
        }
        throw new IllegalArgumentException("Not exist "
                + PayPlatform.class.getName() + " for code " + code);
    }
}
