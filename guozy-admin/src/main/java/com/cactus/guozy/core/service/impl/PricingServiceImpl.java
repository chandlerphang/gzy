package com.cactus.guozy.core.service.impl;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderAdjustment;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.PricingException;
import com.cactus.guozy.core.service.PricingService;

@Service("pricingService")
public class PricingServiceImpl implements PricingService {

	private final static Logger LOG = LoggerFactory.getLogger(PricingService.class);
	
	@Resource(name = "orderService")
	protected OrderService orderService;
	
	@Override
	public Order executePricing(Order order) {
		if(order.getIsSalerOrder() && order.getSalePriceOverride()) {
			if(order.getSalePrice() == null || order.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
				final String msg = "导购员推送订单价格设置异常. Order Id: " + order.getId() + ", Sale Price: " + order.getSalePrice().toString();
				if(LOG.isErrorEnabled()) {
					LOG.error(msg);
				}
				throw new PricingException(msg);
			}
			order.setTotal(order.getSalePrice().add(order.getShipPrice()));
			order.setSubTotal(order.calculateSubTotal());
			orderService.updateTotal(order);
			return order;
		}
		
		
		BigDecimal subtotal = order.calculateSubTotal();
		order.setSubTotal(subtotal);
		
		// 加原总价
		BigDecimal total = BigDecimal.ZERO.add(order.getSubTotal());
		if(LOG.isDebugEnabled()) {
			LOG.debug("Order Id: " + order.getId() + ", SubTotal: " + total);
		}
		
		// 减优惠券
		List<OrderAdjustment> adjustments = orderService.findAllAdjustments(order.getId());
		for(OrderAdjustment adjustment : adjustments) {
			if(LOG.isDebugEnabled()) {
				LOG.debug("Order Id: " + order.getId() + ", Adjustment: " + adjustment.getValue());
			}
			total = total.subtract(adjustment.getValue());
		}
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("Order Id: " + order.getId() + ", Ship Price: " + order.getShipPrice());
		}
		// 加配送费
		total = total.add(order.getShipPrice());
		
		if(LOG.isDebugEnabled()) {
			LOG.debug("Order Id: " + order.getId() + ", Total: " + total);
		}
		order.setTotal(total);
		orderService.updateTotal(order);
		return order;
	}

}
