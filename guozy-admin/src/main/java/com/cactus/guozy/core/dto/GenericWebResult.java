package com.cactus.guozy.core.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;

/**
 * ajax 请求的返回类型封装JSON结果
 */
@XmlRootElement(name="result")
public class GenericWebResult {
	
	public static GenericWebResult PARAMETER_ERROR = GenericWebResult.error("000101").withData(ErrorMsgWrapper.error("paramsError").withMsg("参数错误"));

	@XmlElement
	private String status;

	@XmlElement
    private Object data;

    public GenericWebResult(String status) {
        this.status = status;
    }
    
    public GenericWebResult(String status, Object data) {
        this.status = status;
        this.data = data;
    }
    
    public static GenericWebResult error(String errcode) {
    	return new GenericWebResult(errcode);
    }
    
    public GenericWebResult withData(Object data) {
    	this.data = data;
    	return this;
    }
    
    public static GenericWebResult success(Object data) {
    	return new GenericWebResult("200", data);
    } 

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BaseResult [status=" + status + ", data=" + data + "]";
	}
}
