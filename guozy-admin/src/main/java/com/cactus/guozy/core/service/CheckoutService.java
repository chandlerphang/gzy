package com.cactus.guozy.core.service;

import java.util.Map;

import com.cactus.guozy.core.dto.PayRequestParam;

public interface CheckoutService {
	
	Map<String, Object> performCheckout(PayRequestParam payRequestParam);

}
