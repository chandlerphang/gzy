package com.cactus.guozy.core.service.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cactus.guozy.core.pay.alipay.Alipay;
import com.cactus.guozy.core.pay.alipay.AlipayBuilder;

@Component
public class AlipaySupport {

    @Value("${pay.request.alipay.appid}")
    private String merchantId;

    @Value("${pay.request.alipay.secret}")
    private String secret;

    @Value("${pay.request.alipay.ali_pub_key}")
    private String aliPubKey;
    
    @Value("${pay.request.alipay.pid}")
    private String sellerId;

    private Alipay alipay;

    @PostConstruct
    public void initAlipay(){
        alipay = AlipayBuilder.newBuilder(merchantId, secret)
        		.aliPubKey(aliPubKey)
        		.sellerId(sellerId)
        		.build();
    }

    public Boolean notifyVerify(Map<String, String> params){
        return alipay.verify().rsa(params);
    }
    
    public Alipay getAlipay() {
    	return alipay;
    }
    
}
