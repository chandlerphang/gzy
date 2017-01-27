package com.cactus.guozy.core.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GenericResponse {
    
    private List<String> errorCodes;

    public boolean hasErrors() {
        return errorCodes != null && errorCodes.size() > 0;
    }

    public List<String> getErrorCodes() {
        return errorCodes == null ? Collections.emptyList() : errorCodes;
    }
    
    public static GenericResponse error() {
    	return new GenericResponse();
    }
    
    public static GenericResponse success() {
    	return new GenericResponse();
    }

    public GenericResponse addErrorCode(String errorCode) {
    	if(errorCodes == null) {
    		errorCodes = new ArrayList<String>();
    	}
        errorCodes.add(errorCode);
        return this;
    }

}