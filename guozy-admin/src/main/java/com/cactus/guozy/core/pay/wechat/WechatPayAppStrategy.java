package com.cactus.guozy.core.pay.wechat;

import java.math.BigDecimal;
import java.util.Map;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.common.json.Jsons;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.pay.PayStrategy;
import com.cactus.guozy.core.pay.PayType;

/**
 * 微信app支付
 */
public class WechatPayAppStrategy implements PayStrategy {

	@Override
	public String generatePayParams(PayType payType, Map<String, Object> params) {
		final String appId = RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.appid");
		final String mchId = RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.mch_id");
		final String api_key = RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.appkey");
		final String notifyUrl= RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.notify_url");
		final String spbill_create_ip= RuntimeEnvConfigService.resolveSystemProperty("pay.request.wechart.spbill_create_ip");
		
		Order order =  (Order)params.get("order");
		PayRequest req = PayRequest.builder()
				.notifyUrl(notifyUrl)
				.body("果之源订单-" + order.getOrderNumber())
				.clientId(spbill_create_ip)
				.outTradeNo(String.valueOf(order.getId()))
				.totalFee(order.getTotal().multiply(BigDecimal.valueOf(100)).intValue())
				.build();
		
		Wepay wepay = WepayBuilder.newBuilder(appId, api_key, mchId).build();
		Map<String, String> map = wepay.pay().appPay(req);
		return Jsons.DEFAULT.toJson(map);
	}
}
