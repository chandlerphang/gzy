package com.cactus.guozy.admin.security;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class AdminAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private String defaultFailureUrl;
//
//    public AdminAuthenticationFailureHandler() {
//        super();
//    }
//
    public AdminAuthenticationFailureHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
        this.defaultFailureUrl = defaultFailureUrl;
    }
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
//        String failureUrlParam = com.zyrone.util.StringUtil.cleanseUrlString(request.getParameter("failureUrl"));
//        String successUrlParam = com.zyrone.util.StringUtil.cleanseUrlString(request.getParameter("successUrl"));
//        String failureUrl = failureUrlParam == null ? null : failureUrlParam.trim();
//        Boolean sessionTimeout = (Boolean) request.getAttribute("sessionTimeout");
//
//        if (StringUtils.isEmpty(failureUrl) && BooleanUtils.isNotTrue(sessionTimeout)) {
//            failureUrl = defaultFailureUrl;
//        }
//
//        if (BooleanUtils.isTrue(sessionTimeout)) {
//            failureUrl = "?sessionTimeout=true";
//        }
//        
//        successUrlParam = request.getHeader("referer");
//        if (failureUrl != null) {
//            if (!StringUtils.isEmpty(successUrlParam)) {
//                if (!failureUrl.contains("?")) {
//                    failureUrl += "?successUrl=" + successUrlParam;
//                } else {
//                    failureUrl += "&successUrl=" + successUrlParam;
//                }
//            }
//
//            saveException(request, exception);
//            getRedirectStrategy().sendRedirect(request, response, failureUrl);
//        } else {
//            super.onAuthenticationFailure(request, response, exception);
//        }
//    }

}
