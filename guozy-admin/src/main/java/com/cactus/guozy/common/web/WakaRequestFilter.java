package com.cactus.guozy.common.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cactus.guozy.admin.security.AdminUserDetails;
import com.cactus.guozy.common.WakaRequestContext;

@Component("wakaRequestFilter")
public class WakaRequestFilter extends OncePerRequestFilter {

	private final Logger LOG = LoggerFactory.getLogger(getClass());

	public static final String ADMIN_USER_ID_PARAM_NAME = "blAdminUserId";
	public static final String PROFILE_REQ_PARAM = "blProfileId";
    private Set<String> ignoreSuffixes;

    @Value("${asset.url.prefix}")
    private String assetPrefix;

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws IOException, ServletException {
        if (!shouldProcessURL(request, request.getRequestURI())) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Process URL not processing URL " + request.getRequestURI());
            }
            filterChain.doFilter(request, response);
            return;
        }
        
        ServletWebRequest webReq = new ServletWebRequest(request, response);
        WakaRequestContext brc = WakaRequestContext.instance();
        brc.setWebRequest(webReq);
        brc.setAdmin(true);
        
        SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx != null) {
            Authentication auth = ctx.getAuthentication();
            if (auth != null) {
            	AdminUserDetails temp = (AdminUserDetails) auth.getPrincipal();
            	brc.setUser(temp.getAdminUser());
            }
        }
        
        filterChain.doFilter(request, response);
    }
    
    protected boolean shouldProcessURL(HttpServletRequest request, String requestURI) {
        int pos = requestURI.lastIndexOf(".");
        if (pos > 0 && !requestURI.contains(assetPrefix)) {
            String suffix = requestURI.substring(pos);
            if (getIgnoreSuffixes().contains(suffix.toLowerCase())) {
                if (LOG.isTraceEnabled()) {
                    LOG.trace("WakaProcessURLFilter ignoring request due to suffix: " + requestURI);
                }
                return false;
            }
        }
        
        return true;
    }

    @SuppressWarnings("rawtypes")
    protected Set getIgnoreSuffixes() {
        if (ignoreSuffixes == null || ignoreSuffixes.isEmpty()) {
            String[] ignoreSuffixList = {".aif", ".aiff", ".asf", ".avi", ".bin", ".bmp", ".css", ".doc", ".eps", ".gif", ".hqx", ".js", ".jpg", ".jpeg", ".mid", ".midi", ".mov", ".mp3", ".mpg", ".mpeg", ".p65", ".pdf", ".pic", ".pict", ".png", ".ppt", ".psd", ".qxd", ".ram", ".ra", ".rm", ".sea", ".sit", ".stk", ".swf", ".tif", ".tiff", ".txt", ".rtf", ".vob", ".wav", ".wmf", ".xls", ".zip"};
            ignoreSuffixes = new HashSet<String>(Arrays.asList(ignoreSuffixList));
        }
        return ignoreSuffixes;
    }
    
}
