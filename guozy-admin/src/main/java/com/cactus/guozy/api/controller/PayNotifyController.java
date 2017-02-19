package com.cactus.guozy.api.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cactus.guozy.common.exception.BusinessException;
import com.cactus.guozy.core.service.AlipayNotifyService;
import com.cactus.guozy.core.service.WechatNotifyService;

/**
 * 支付通知入口
 */
@Controller
@RequestMapping(value = "/open/payNotify")
public class PayNotifyController {

    @Autowired
    private WechatNotifyService wechatNotifyService;
    @Autowired
    private AlipayNotifyService alipayNotifyService;

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
    @RequestMapping(value = "/wechat", method = RequestMethod.POST)
    public void wechatNotify(HttpServletRequest request, HttpServletResponse response) {
        wechatNotifyService.wechatNotify(request, response);
    }

}
