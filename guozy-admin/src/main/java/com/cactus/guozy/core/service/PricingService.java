package com.cactus.guozy.core.service;

import com.cactus.guozy.core.domain.Order;

public interface PricingService {
	
	Order executePricing(Order order) throws PricingException;
	
}
