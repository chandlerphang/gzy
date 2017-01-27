package com.cactus.guozy.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.cactus.guozy.admin.domain.AdminUser;
import com.cactus.guozy.common.utils.ThreadLocalManager;

public class WakaRequestContext {
    
    protected static final Logger LOG = LoggerFactory.getLogger(WakaRequestContext.class);
    
    private static final ThreadLocal<WakaRequestContext> WAKA_REQUEST_CONTEXT = ThreadLocalManager.createThreadLocal(WakaRequestContext.class);
    
    public static WakaRequestContext instance() {
        return WAKA_REQUEST_CONTEXT.get();
    }
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected WebRequest webRequest;
    protected Boolean isAdmin = false;
    protected AdminUser user;
    
    public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public AdminUser getUser() {
		return user;
	}

	public void setUser(AdminUser user) {
		this.user = user;
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

    public Boolean getAdmin() {
        return isAdmin == null ? false : isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

}
