package com.cactus.guozy.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.core.dto.GenericWebResult;

@ControllerAdvice
public class ApiExceptionHandlers {
	
    private static final Logger LOG = LoggerFactory.getLogger(ApiExceptionHandlers.class);

    @ExceptionHandler(WebServiceException.class)
    public @ResponseBody GenericWebResult handleWebServiceException(HttpServletRequest request, HttpServletResponse response, Exception ex){
        if (LOG.isErrorEnabled() && ex.getCause() != null) {
            LOG.error("An error occured invoking a REST service.", ex.getCause());
        }
        
        WebServiceException wsException = (WebServiceException) ex;
        int status = wsException.getHttpStatusCode();
        if(status == 0) {
        	status = 500;
        }
        
        GenericWebResult result = GenericWebResult.error(status+"");	
        response.setStatus(status);
        
        if (wsException.getKey() != null && !wsException.getKey().equals("")) {
        	ErrorMsgWrapper errorMessageWrapper = new ErrorMsgWrapper();
            errorMessageWrapper.setKey(wsException.getKey());
            errorMessageWrapper.setMsg(wsException.getMsg());
            result.withData(errorMessageWrapper);
        } else {
            ErrorMsgWrapper errorMessageWrapper = new ErrorMsgWrapper();
            errorMessageWrapper.setKey(WebServiceException.UNKNOWN_ERROR);
            errorMessageWrapper.setMsg("未知的错误");
            result.withData(errorMessageWrapper);
        }

        return result;
    }
    
    @ExceptionHandler(BizException.class)
    public @ResponseBody GenericWebResult handleBizException(HttpServletRequest request, HttpServletResponse response, Exception ex){
        if (LOG.isErrorEnabled() && ex.getCause() != null) {
            LOG.error("An error occured invoking a REST service.", ex.getCause());
        }
        
        BizException wsException = (BizException) ex;
        GenericWebResult result = GenericWebResult.error(wsException.getErrno());	
        response.setStatus(500);
        
        ErrorMsgWrapper errorMessageWrapper = new ErrorMsgWrapper();
        errorMessageWrapper.setKey(WebServiceException.UNKNOWN_ERROR);
        errorMessageWrapper.setMsg(wsException.getMessage());
        result.withData(errorMessageWrapper);

        return result;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public @ResponseBody GenericWebResult handleNoHandlerFoundException(HttpServletRequest request, HttpServletResponse response, Exception ex){
    	if(LOG.isErrorEnabled()) {
    		LOG.error("An error occured invoking a REST service", ex);
    	}
    	GenericWebResult result = GenericWebResult.error(HttpServletResponse.SC_NOT_FOUND + "");	
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        ErrorMsgWrapper errorMessageWrapper = new ErrorMsgWrapper();
        errorMessageWrapper.setKey(WebServiceException.NOT_FOUND);
        errorMessageWrapper.setMsg("请求资源不存在");
        result.withData(errorMessageWrapper);
   
        return result;
    }
    
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public @ResponseBody GenericWebResult handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpServletResponse response, Exception ex){
    	if(LOG.isErrorEnabled()) {
    		LOG.error("An error occured invoking a REST service", ex);
    	}
    	GenericWebResult result = GenericWebResult.error(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE + "");	
        response.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
        ErrorMsgWrapper errorMessageWrapper = new ErrorMsgWrapper();
        errorMessageWrapper.setKey(WebServiceException.CONTENT_TYPE_NOT_SUPPORTED);
        errorMessageWrapper.setMsg("ContentType类型不支持");
        result.withData(errorMessageWrapper);
        
        return result;
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public @ResponseBody GenericWebResult handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	if(LOG.isErrorEnabled()) {
    		LOG.error("An error occured invoking a REST service", ex);
    	}
    	GenericWebResult result = GenericWebResult.error(HttpServletResponse.SC_METHOD_NOT_ALLOWED + "");	
        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
        ErrorMsgWrapper errorMessageWrapper = new ErrorMsgWrapper();
        errorMessageWrapper.setKey(WebServiceException.METHOD_NOT_SUPPORTED);
        errorMessageWrapper.setMsg("Method不支持");
        result.withData(errorMessageWrapper);

        return result;
    }
    
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public @ResponseBody GenericWebResult handleMissingServletRequestParameterException(HttpServletRequest request, HttpServletResponse response, Exception ex) {
    	if(LOG.isErrorEnabled()) {
    		LOG.error("An error occured invoking a REST service", ex);
    	}
    	
    	MissingServletRequestParameterException castedException = (MissingServletRequestParameterException)ex;
        String parameterType = castedException.getParameterType();
        String parameterName = castedException.getParameterName();
        
        if (parameterType == null) {
            parameterType = "String";
        }
        if(parameterName == null) {
            parameterName = "[unknown name]";
        }
        
        GenericWebResult result = GenericWebResult.error(HttpServletResponse.SC_BAD_REQUEST + "");	
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorMsgWrapper errorMessageWrapper = new ErrorMsgWrapper();
        errorMessageWrapper.setKey(WebServiceException.QUERY_PARAMETER_NOT_PRESENT);
        errorMessageWrapper.setMsg("未找到参数 " + parameterName + ":" +  parameterType);
        result.withData(errorMessageWrapper);
    
        return result;
    }

    @ExceptionHandler(Exception.class)
    public @ResponseBody GenericWebResult handleException(HttpServletRequest request, HttpServletResponse response, Exception ex){
    	if(LOG.isErrorEnabled()) {
    		LOG.error("An error occured invoking a REST service", ex);
    	}
    	
    	GenericWebResult result = GenericWebResult.error(HttpServletResponse.SC_INTERNAL_SERVER_ERROR + "");	
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ErrorMsgWrapper errorMessageWrapper = new ErrorMsgWrapper();
        errorMessageWrapper.setKey(WebServiceException.UNKNOWN_ERROR);
        errorMessageWrapper.setMsg(ex.getMessage());
        result.withData(errorMessageWrapper);
    
        return result;
    }

}
