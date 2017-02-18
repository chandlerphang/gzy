package com.cactus.guozy.core.pay.alipay;

public enum SignType {

    MD5("MD5"),

    DSA("DSA"),

    RSA("RSA");

    private String value;

    private SignType(String value){
        this.value = value;
    }

    public String value(){
        return value;
    }
}