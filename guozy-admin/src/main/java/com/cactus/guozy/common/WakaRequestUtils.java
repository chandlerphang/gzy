package com.cactus.guozy.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.WebRequest;

public class WakaRequestUtils {
    
    private static String OK_TO_USE_SESSION = "blOkToUseSession";

    public static boolean isOKtoUseSession(WebRequest request) {
        Boolean useSessionForRequestProcessing = (Boolean) request.getAttribute(OK_TO_USE_SESSION, WebRequest.SCOPE_REQUEST);
        if (useSessionForRequestProcessing == null) {
            // 默认可以使用session
            return true;
        } else {
            return useSessionForRequestProcessing.booleanValue();
        }
    }
    
    public static void setOKtoUseSession(WebRequest request, Boolean value) {
        request.setAttribute(OK_TO_USE_SESSION, value, WebRequest.SCOPE_REQUEST);
    }

    public static Object getSessionAttributeIfOk(WebRequest request, String attribute) {
        if (isOKtoUseSession(request)) {
            return request.getAttribute(attribute, WebRequest.SCOPE_GLOBAL_SESSION);
        }
        return null;
    }

    public static boolean setSessionAttributeIfOk(WebRequest request, String attribute, Object value) {
        if (isOKtoUseSession(request)) {
            request.setAttribute(attribute, value, WebRequest.SCOPE_GLOBAL_SESSION);
            return true;
        }
        return false;
    }

    public static String getURLorHeaderParameter(WebRequest request, String varName) {
        String returnValue = request.getHeader(varName);
        if (returnValue == null) {
            returnValue = request.getParameter(varName);
        }
        return returnValue;
    }

    public static String getRequestedServerPrefix() {
        HttpServletRequest request = WakaRequestContext.instance().getRequest();
        String scheme = request.getScheme();
        StringBuilder serverPrefix = new StringBuilder(scheme);
        serverPrefix.append("://");
        serverPrefix.append(request.getServerName());
        if ((scheme.equalsIgnoreCase("http") && request.getServerPort() != 80) || (scheme.equalsIgnoreCase("https") && request.getServerPort() != 443)) {
            serverPrefix.append(":");
            serverPrefix.append(request.getServerPort());
        }
        return serverPrefix.toString();
    }
}