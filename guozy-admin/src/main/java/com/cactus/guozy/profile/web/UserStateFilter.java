package com.cactus.guozy.profile.web;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.filter.GenericFilterBean;

import com.cactus.guozy.profile.UserState;
import com.cactus.guozy.profile.domain.User;
import com.cactus.guozy.profile.service.UserService;

public class UserStateFilter extends GenericFilterBean implements Ordered {

    protected static final Logger LOG = LoggerFactory.getLogger(UserStateFilter.class);
    
    @Resource(name="userService")
    protected UserService userService;
    
    protected String userIdAttributeName = "userId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        
        String userId=null;

        HttpServletRequest request = (HttpServletRequest)servletRequest;
        
        // If someone already set the user on the request then we don't need to do anything.
        if (UserState.getUser() == null){
    
            // First check to see if someone already put the userId on the request
            if (request.getAttribute(userIdAttributeName) != null) {
                userId = String.valueOf(request.getAttribute(userIdAttributeName));
            }
            
            if (userId == null) {
                //If it's not on the request parameter, look on the header
            	userId = request.getHeader(userIdAttributeName);
            }
            
            if (userId == null) {
                //If it's not on the request attribute, try the parameter
            	userId = servletRequest.getParameter(userIdAttributeName);
            }
            
            if (userId != null && userId.trim().length() > 0) {
                if (NumberUtils.isCreatable(userId)) {
                    //If we found it, look up the customer and put it on the request.
                    User customer = userService.readUserById(Long.valueOf(userId));
                    if (customer != null) {
                        UserState.setUser(customer);
                    }
                } else {
                	if(LOG.isWarnEnabled()) {
                		LOG.warn(String.format("The user id passed in '%s' was not a number", userId));	
                	}
                }
            }
            
            if (userId == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("No customer ID was found for the API request. In order to look up a customer for the request" +
                            " send a request parameter or request header for the '" + userIdAttributeName + "' attribute");
                }
            }
        }

        filterChain.doFilter(request, servletResponse);
    }

    @Override
    public int getOrder() {
        return 2000;
    }

    public String getUserIdAttributeName() {
        return userIdAttributeName;
    }

    public void setUserIdAttributeName(String userIdAttributeName) {
        if (userIdAttributeName == null || userIdAttributeName.trim().length() < 1) {
            throw new IllegalArgumentException("customerIdAttributeName cannot be null");
        }
        this.userIdAttributeName = userIdAttributeName;
    }
}
