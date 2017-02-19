package com.cactus.guozy.core.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 支付宝通知业务接口
 */
public interface AlipayNotifyService {

    /**
     * 国内支付宝app通知
     * @param request
     * @param response
     */
    void alipayNotifyMainApp(HttpServletRequest request, HttpServletResponse response);

}
