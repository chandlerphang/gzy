package com.cactus.guozy.core.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信通知业务接口
 */
public interface IWechatNotifyService {

    /**
     * 微信通知
     * @param request
     * @param response
     */
    void wechatNotify(HttpServletRequest request, HttpServletResponse response);

}
