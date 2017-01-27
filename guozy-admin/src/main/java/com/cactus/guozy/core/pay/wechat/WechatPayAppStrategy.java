package com.cactus.guozy.core.pay.wechat;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.common.json.Jsons;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.dto.PayRequest;
import com.cactus.guozy.core.pay.PayStrategy;
import com.cactus.guozy.core.pay.PayType;

/**
 * 微信app支付
 */
public class WechatPayAppStrategy implements PayStrategy {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(WechatPayAppStrategy.class);

	@Override
	public String generatePayParams(PayType payType, Map<String, Object> params) {
		final String appId = RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.appid");
		final String mchId = RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.mch_id");
		final String api_key = RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.appkey");
		final String notifyUrl= RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.notify_url");
		final String spbill_create_ip= RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.spbill_create_ip");
		
		Wepay wepay = WepayBuilder.newBuilder(appId, api_key, mchId).build();
		
		Order order =  (Order)params.get("order");
		PayRequest req = new PayRequest();
		req.setNotifyUrl(notifyUrl);
		String orderNum = order.getOrderNumber();
        if (StringUtils.isNotBlank(orderNum)) {
        	req.setBody("果之源-订单编号：" + orderNum);
        } else {
        	req.setBody("果之源-订单支付");
        }
        if(StringUtils.isNotBlank((String)params.get("clientIp"))) {
        	req.setClientId((String)params.get("clientIp"));
        } else {
        	req.setClientId(spbill_create_ip);
        }
        
		req.setOutTradeNo(orderNum);
		req.setTotalFee(order.getTotal().multiply(BigDecimal.valueOf(100)).intValue());
		
		Map<String, String> map = wepay.pay().appPay(req);
		return Jsons.DEFAULT.toJson(map);
	}
}
