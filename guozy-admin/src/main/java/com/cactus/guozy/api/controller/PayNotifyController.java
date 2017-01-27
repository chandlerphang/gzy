package com.cactus.guozy.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cactus.guozy.common.exception.BusinessException;
import com.cactus.guozy.core.service.IAlipayNotifyService;
import com.cactus.guozy.core.service.IWechatNotifyService;

/**
 * 支付通知入口
 */
@RequestMapping(value = "/open/payNotify")
public class PayNotifyController {

    private static Logger logger = LoggerFactory.getLogger(PayNotifyController.class);
    
    @Autowired
    private IWechatNotifyService wechatNotifyService;
    @Autowired
    private IAlipayNotifyService alipayNotifyService;

    /**
     * 国内支付宝app通知回调
     * @param request
     * @param response
     * @throws SystemException
     * @throws BusinessException
     */
    @RequestMapping(value = "/alipayMainApp", method = RequestMethod.POST)
    public void alipayNotifyMainApp(HttpServletRequest request, HttpServletResponse response) {
        alipayNotifyService.alipayNotifyMainApp(request, response);
    }

    /**
     * 微信通知回调
     * @param request
     * @param response
     */
    @RequestMapping(value = "wechat", method = RequestMethod.POST)
    public void wechatNotify(HttpServletRequest request, HttpServletResponse response) {
        wechatNotifyService.wechatNotify(request, response);
    }

}
