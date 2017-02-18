package com.cactus.guozy.core.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.core.dao.OrderDao;
import com.cactus.guozy.core.domain.OrderStatus;
import com.cactus.guozy.core.dto.PayRequestParam;
import com.cactus.guozy.core.service.CheckoutService;
import com.cactus.guozy.core.service.PayRouteService;

@Service("checkoutService")
public class CheckoutServiceImpl implements CheckoutService {

	@Autowired
	protected OrderDao orderDao;
	
	@Resource(name = "payRouteService")
	private PayRouteService payRouteService;
	
	@Override
	@Transactional
	public Map<String, Object> performCheckout(PayRequestParam payRequestParam) {
		orderDao.updateStatus(payRequestParam.getOrderId(), OrderStatus.SUBMITTED.getType());
		return payRouteService.getPayRetMap(payRequestParam);
	}

}
