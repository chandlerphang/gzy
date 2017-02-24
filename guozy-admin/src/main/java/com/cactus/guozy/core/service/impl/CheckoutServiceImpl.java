package com.cactus.guozy.core.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.core.dao.OrderDao;
import com.cactus.guozy.core.dao.PayMapDao;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderStatus;
import com.cactus.guozy.core.domain.PayMap;
import com.cactus.guozy.core.dto.PayRequestParam;
import com.cactus.guozy.core.pay.PayType;
import com.cactus.guozy.core.pay.PlatformType;
import com.cactus.guozy.core.pay.StrategyContext;
import com.cactus.guozy.core.service.CheckoutService;
import com.cactus.guozy.core.service.PricingService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("checkoutService")
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	protected OrderDao orderDao;
	
	@Autowired
	private PayMapDao payMapDao;

	@Resource(name = "pricingService")
	protected PricingService pricingService;

	@Override
	@Transactional
	public Map<String, Object> performCheckout(PayRequestParam payRequestParam, Order order) {
//		if(!OrderStatus.IN_PROCESS.getType().equals(order.getStatus().getType())) {
//			log.error("订单状态异常：" + order.getStatus().getType() + ", 期望 " + OrderStatus.IN_PROCESS.getType());
//			throw new BizException("500", "订单状态异常，无法完成支付");
//		}
		
		order.setStatus(OrderStatus.SUBMITTED);
		orderDao.updateStatus(payRequestParam.getOrderId(), OrderStatus.SUBMITTED.getType());
		Map<String, Object> retMap = assembleRetMap(payRequestParam, order);
		return retMap;
	}

	
	public PayType dealPayType(String onLineStyle, String browseType) {
		PayType payType = null;
		switch (onLineStyle) {
		case "alipay":
			switch (browseType) {
			case "app":
				payType = PayType.ALIPAY_APP;
				break;
			default:
				payType = null;
			}
			break;
		case "wechat":
			switch (browseType) {
			case "app":
				payType = PayType.WECHAT_APP;
				break;
			default:
				payType = null;
			}
			break;
		default:
			payType = null;
		}
		return payType;
	}

	private void savePayRecord(String payRequsetMsg, PayType payType, PayRequestParam payRequestParam, Order order) {
		PayMap payMap = new PayMap();
		payMap.setOrderId(order.getId());
		payMap.setOrderCode(order.getOrderNumber());
		PlatformType type = PlatformType.getPlatform(payType.value());
		payMap.setPlatform(type.value());
		payMap.setTempPayCode(order.getId().toString());
		payMap.setPayParams(payRequsetMsg);
		payMap.setRequestBiz(payRequestParam.getRequestBiz());
		payMapDao.insertSelective(payMap);
	}

	private Map<String, Object> assembleRetMap(PayRequestParam payRequestParam, Order order) {
		if (order.getOrderNumber() == null) {
			order.setOrderNumber(determineOrderNumber(order));
			orderDao.updateOrderNumber(order);
		}

		order = pricingService.executePricing(order);

		Map<String, Object> paramsToPass = new HashMap<>();
		paramsToPass.put("order", order);
		if (StringUtils.isNotBlank(payRequestParam.getRetUrl())) {
			paramsToPass.put("retUrl", payRequestParam.getRetUrl());
		}
		PayType payType = dealPayType(payRequestParam.getPlatformType(), payRequestParam.getBrowseType());
		StrategyContext context = new StrategyContext();
		String payRequsetMsg = context.generatePayParams(payType, paramsToPass);
		if (log.isDebugEnabled()) {
			log.debug("订单code为{}的支付请求参数生成信息：{}", new Object[] { payRequestParam.getOrderNum(), payRequsetMsg });
		}
		savePayRecord(payRequsetMsg, payType, payRequestParam, order);
		Map<String, Object> retMap = new HashMap<>();
		retMap.put("payData", payRequsetMsg);
		retMap.put("payment", payRequestParam.getPlatformType());
		retMap.put("orderID", payRequestParam.getOrderId());
		retMap.put("message", "请去第三方平台支付");
		return retMap;
	}

	protected String determineOrderNumber(Order order) {
		return new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date()) + order.getId();
	}

}
