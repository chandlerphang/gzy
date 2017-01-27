package com.cactus.guozy.admin.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

/**
 * @author Jason Phang
 */
public class AdminAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    protected String loginUri = "/login";
    private RequestCache requestCache = new HttpSessionRequestCache();
    private static final String successUrlParameter = "successUrl=";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
    	
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl() || (targetUrlParameter != null && StringUtils.hasText(request.getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);
            return;
        }

        clearAuthenticationAttributes(request);

        String targetUrl = savedRequest.getRedirectUrl();
        
        targetUrl = targetUrl.replace("sessionTimeout=true", "");
        if (targetUrl.charAt(targetUrl.length() - 1) == '?') {
            targetUrl = targetUrl.substring(0, targetUrl.length() - 1);
        }

        if (targetUrl.contains(successUrlParameter)) {
            int successUrlPosistion = targetUrl.indexOf(successUrlParameter) + successUrlParameter.length();
            int nextParamPosistion = targetUrl.indexOf("&", successUrlPosistion);
            if (nextParamPosistion == -1) {
                targetUrl = targetUrl.substring(successUrlPosistion, targetUrl.length());
            } else {
                targetUrl = targetUrl.substring(successUrlPosistion, nextParamPosistion);
            }
        }
        targetUrl = removeLoginSegment(targetUrl);

        logger.debug("重定向到之前保存的请求上: " + targetUrl);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String removeLoginSegment(String url) {
        if (StringUtils.isEmpty(url)) {
            return "/";
        }
        int lastSlashPos = url.lastIndexOf(loginUri);
        if (lastSlashPos >= 0) {
            return url.substring(0, lastSlashPos);
        } else {
            return url;
        }
    }

    public String getLoginUri() {
        return loginUri;
    }

    public void setLoginUri(String loginUri) {
        this.loginUri = loginUri;
    }
}
