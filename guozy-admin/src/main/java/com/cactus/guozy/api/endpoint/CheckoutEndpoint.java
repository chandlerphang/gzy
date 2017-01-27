package com.cactus.guozy.api.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cactus.guozy.api.wrapper.OrderWrapper;

@RestController
@RequestMapping("/cart/checkout")
public class CheckoutEndpoint {
	
    @RequestMapping(method = RequestMethod.POST)
	public OrderWrapper performCheckout(HttpServletRequest request) {
    	return null;
    }

}
