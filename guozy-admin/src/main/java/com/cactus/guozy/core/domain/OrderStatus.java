package com.cactus.guozy.core.domain;

public enum OrderStatus {
	
	SALER_IN_PROCESS("SALER_IN_PROCESS", "导购员处理中"),
	IN_PROCESS("IN_PROCESS", "处理中"),
	SUBMITTED("SUBMITTED", "已提交"),
	COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消");
	
	private String type;
    private String friendlyType;

	private OrderStatus() {
        //do nothing
    }
	
	private OrderStatus(final String type, final String friendlyType) {
    	this.type = type;
        this.friendlyType = friendlyType;
    }

    public String getType() {
		return type;
	}

	public String getFriendlyType() {
		return friendlyType;
	}
	
}
