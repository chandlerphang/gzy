package com.cactus.guozy.core.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.core.domain.PayMap;
import com.cactus.guozy.core.pay.PayPlatform;
import com.cactus.guozy.core.pay.PlatformType;
import com.cactus.guozy.core.service.AlipayNotifyService;
import com.cactus.guozy.core.service.PayMapService;

/**
 * 支付宝通知业务
 */
@Service
public class AlipayNotifyServiceImpl implements AlipayNotifyService {

    private static Logger logger = LoggerFactory.getLogger(AlipayNotifyServiceImpl.class);
    
    @Autowired
    private PayMapService payMapService;
    
    @Override
    public void alipayNotifyMainApp(HttpServletRequest request, HttpServletResponse response) {
        //商户订单号
        String out_trade_no = null;
        //交易状态
        String trade_status = null;
        Map params = getRequestParams(request);
        if (AlipayNotify.verify(params)) {//验证成功
            try {
            	
                out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
                trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("request getParameter error:{}",e.getMessage());
                throw new RuntimeException(e);
            }
            String platformCode=PayPlatform.ALIPAY_COMMON.getCode();
            if (trade_status.equals("TRADE_FINISHED")) {
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                pay2NextBiz(response, out_trade_no, params, platformCode);
            }
        } else {//验证失败
            try {
                response.getOutputStream().println("fail");
            } catch (IOException e) {
                logger.error("response IO error:{}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    private Map getRequestParams(HttpServletRequest request) {
        Map requestParams = request.getParameterMap();
        Map params = new HashMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        if(logger.isDebugEnabled()){
            logger.debug("订单orderCode：{}所对应的支付宝通知交易码为{}返回参数列表：{}", new Object[]{params.get("out_trade_no"), params.get("trade_no"), params.toString()});
        }
        return params;
    }

    private void pay2NextBiz(HttpServletResponse response, String out_trade_no, Map params, String platformCode) {
        if(logger.isDebugEnabled()){
            logger.debug("订单orderCode：{}所对应的支付宝通知交易码为:{}支付成功", new Object[]{params.get("out_trade_no"), params.get("trade_no")});
        }
        PayMap payMap = payMapService.updatePayMapByPayCode(out_trade_no, params.toString(), null, PlatformType.TB, String.valueOf(params.get("trade_no")), platformCode);
        try {
            //向支付平台回写本地处理成功消息，让支付平台不再继续发送通知消息
            response.getWriter().println("success");
        } catch (IOException e) {
            logger.error("response IO error:{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
