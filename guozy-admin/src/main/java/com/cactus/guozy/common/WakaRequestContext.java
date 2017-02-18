package com.cactus.guozy.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.cactus.guozy.admin.domain.AdminUser;
import com.cactus.guozy.common.utils.ThreadLocalManager;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.profile.domain.User;

public class WakaRequestContext {
    
    private static final ThreadLocal<WakaRequestContext> WAKA_REQUEST_CONTEXT = ThreadLocalManager.createThreadLocal(WakaRequestContext.class);
    public static WakaRequestContext instance() {
        return WAKA_REQUEST_CONTEXT.get();
    }
    
    /**
     * 是否是管理员
     */
    protected boolean isAdmin = false;
    
    /**
     * 是否是导购员
     */
    protected boolean isSaler = false;
    
    /**
     * 是否是普通用户
     */
    protected boolean isUser = false;
    
    /**
     * 当前登录的用户，可能是普通用户user，导购员saler，管理员adminuser
     */
    protected Object subject;
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected WebRequest webRequest;
    
    public boolean isAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	
    public boolean isSaler() {
		return isSaler;
	}

	public void setIsSaler(boolean isSaler) {
		this.isSaler = isSaler;
	}
	
    public boolean isUser() {
		return isUser;
	}

	public void setIsUser(boolean isUser) {
		this.isUser = isUser;
	}

	public AdminUser getAdminUser() {
		if(isAdmin) {
			return (AdminUser) subject;
		}
		
		return null;
	}
	
	public User getUser() {
		if(isUser) {
			return (User) subject;
		}
		
		return null;
	}
	
	public Saler getSaler() {
		if(isSaler) {
			return (Saler) subject;
		}
		
		return null;
	}
	
	public void setSubject(Object subject) {
		this.subject = subject;
	}

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
        this.webRequest = new ServletWebRequest(request);
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public void setWebRequest(WebRequest webRequest) {
        this.webRequest = webRequest;
        if (webRequest instanceof ServletWebRequest) {
            this.request = ((ServletWebRequest) webRequest).getRequest();
            setResponse(((ServletWebRequest) webRequest).getResponse());
        }
    }

    public WebRequest getWebRequest() {
        return webRequest;
    }

    public String getRequestURIWithoutContext() {
        String requestURIWithoutContext = null;
        if (request != null && request.getRequestURI() != null) {
            if (request.getContextPath() != null) {
                requestURIWithoutContext = request.getRequestURI().substring(request.getContextPath().length());
            } else {
                requestURIWithoutContext = request.getRequestURI();
            }

            // Remove JSESSION-ID or other modifiers
            int pos = requestURIWithoutContext.indexOf(";");
            if (pos >= 0) {
                requestURIWithoutContext = requestURIWithoutContext.substring(0,pos);
            }
        }
        
        return requestURIWithoutContext;
        
    }
    
    public boolean isSecure() {
        boolean secure = false;
        if (request != null) {
             secure = ("HTTPS".equalsIgnoreCase(request.getScheme()) || request.isSecure());
        }
        return secure;
    }

}
