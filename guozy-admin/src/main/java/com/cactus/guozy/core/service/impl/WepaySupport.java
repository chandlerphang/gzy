package com.cactus.guozy.core.service.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cactus.guozy.core.pay.wechat.Wepay;
import com.cactus.guozy.core.pay.wechat.WepayBuilder;

@Component
public class WepaySupport {

    @Value("${pay.request.wechart.appid}")
    private String appId;

    @Value("${pay.request.wechart.appkey}")
    private String appKey;

    @Value("${pay.request.wechart.mch_id}")
    private String mchId;

    @Value("${pay.request.wechart.notify_url}")
    private String payNotifyUrl;
    
    private Wepay wepay;

    @PostConstruct
    public void initWepay() {
    	wepay = WepayBuilder.newBuilder(appId, appKey, mchId).build();
    }

    /**
     * 校验签名
     * @param params 参数(包含sign)
     * @return 校验成功返回true，反之false
     */
    public Boolean verifySign(Map<String, ?> params){
        return wepay.notifies().verifySign(params);
    }

    /**
     * 通知成功
     */
    public String notifyOk(){
        return wepay.notifies().ok();
    }

    /**
     * 通知不成功
     * @param errMsg 错误消息
     */
    public String notifyNotOk(String errMsg){
        return wepay.notifies().notOk(errMsg);
    }
}
