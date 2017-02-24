package com.cactus.guozy.core.pay.alipay;

import java.util.Map;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.pay.PayStrategy;
import com.cactus.guozy.core.pay.PayType;

/**
 * 支付宝app支付
 */
public class AlipayAppStrategy implements PayStrategy {

    @Override
    public String generatePayParams(PayType payType, Map<String, Object> params) {
    	String retUrl = null;
        if (params.size() > 0 && null != params.get("retUrl")) {
            retUrl = (String) params.get("retUrl");
        }
        return makeMainParams(params, retUrl);
    }

    private String makeMainParams(Map<String, Object> params, String retUrl) {
    	
    	final String appPriKey = RuntimeEnvConfigService.resolveSystemProperty("pay.request.alipay.app_pri_key");
    	final String appPubKey = RuntimeEnvConfigService.resolveSystemProperty("pay.request.alipay.app_pub_key");
    	final String appid = RuntimeEnvConfigService.resolveSystemProperty("pay.request.alipay.appid");
    	final String notifyUrl = RuntimeEnvConfigService.resolveSystemProperty("pay.request.alipay.notify_url");
    	
    	Order order = (Order)params.get("order");
    	AppPayDetail req = new AppPayDetail(order.getId().toString(), order.getOrderNumber(), order.getTotal().toString(), null);
		req.setAppId(appid);
		req.setNotifyUrl(notifyUrl);
		req.setOrderName("果之源订单-" + order.getOrderNumber());
    	
		Alipay alipay = AlipayBuilder.newBuilder(appid)
				.appPriKey(appPriKey)
				.appPubKey(appPubKey)
				.build();
		
		return alipay.pay().appPay(req);
    }

}
