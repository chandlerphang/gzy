package com.cactus.guozy.common.config;

public enum SystemPropertyFieldType {
    INT("整型"),
    LONG("长整型"),
    DOUBLE("双精度"),
    BOOLEAN("布尔型"),
    STRING("字符串");

    private String friendlyName;
    
    private SystemPropertyFieldType(final String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getName() {
    	return name();
    }

    public String getFriendlyName() {
        return friendlyName;
    }

}
