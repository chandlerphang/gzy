package com.cactus.guozy.core.service;

import com.cactus.guozy.core.domain.Order;

public interface CheckoutService {
	
	public Order performCheckout(Order order);

}
