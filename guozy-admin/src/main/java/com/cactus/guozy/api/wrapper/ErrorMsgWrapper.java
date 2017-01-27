package com.cactus.guozy.api.wrapper;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "errmsg")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class ErrorMsgWrapper extends BaseWrapper {

    @XmlElement
    protected String key;

    @XmlElement
    protected String msg;
    
    public static ErrorMsgWrapper error(String key) {
    	ErrorMsgWrapper error = new ErrorMsgWrapper();
    	error.key = key;
    	return error;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    
    public ErrorMsgWrapper withKey(String key) {
    	this.key = key;
    	return this;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String message) {
        this.msg = message;
    }
    
    public ErrorMsgWrapper withMsg(String message) {
    	this.msg = message;
    	return this;
    }

}
