package com.cactus.guozy.core.pay;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 支付宝app支付
 */
public class AlipayAppStrategy implements PayStrategy {

    private static Logger logger = LoggerFactory.getLogger(AlipayAppStrategy.class);

    @Override
    public String generatePayParams(PayType payType, Map<String, Object> params) {
    	return null;
    }

    private String makeGlobalParam(Map<String, Object> params) {
        return null;
    }

    private String makeMainParams(Map<String, Object> params, String retUrl) {
       return null;
    }

}
