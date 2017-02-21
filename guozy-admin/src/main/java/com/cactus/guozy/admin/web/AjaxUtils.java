package com.cactus.guozy.admin.web;

import org.springframework.web.context.request.WebRequest;

public class AjaxUtils {
    /**
     * 验证是否是ajax请求
     * @param webRequest
     * @return
     */
    public static boolean isAjaxRequest(WebRequest webRequest) {
        String requestedWith = webRequest.getHeader("X-Requested-With");
        return requestedWith != null ? "XMLHttpRequest".equals(requestedWith) : false;
    }

    public static boolean isAjaxUploadRequest(WebRequest webRequest) {
        return webRequest.getParameter("ajaxUpload") != null;
    }
    
    private AjaxUtils(){}

}
