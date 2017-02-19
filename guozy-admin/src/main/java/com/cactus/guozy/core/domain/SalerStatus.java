package com.cactus.guozy.core.domain;

import com.cactus.guozy.common.utils.UnreachableCodeException;

import lombok.Getter;

@Getter
public enum SalerStatus {
	
	ON_LINE(0, "ON_LINE"),
	OFF_LINE(2, "OFF_LINE"),
    BUSY(1, "BUSY");

    private int code;
    private String desc;

    SalerStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public static SalerStatus of(int code) {
    	SalerStatus[] ins = SalerStatus.values();
    	for(SalerStatus s : ins) {
    		if(s.getCode() == code) return s;
    	}
    	
    	throw new UnreachableCodeException();
    }

}
