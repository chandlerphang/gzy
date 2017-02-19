package com.cactus.guozy.api.endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cactus.guozy.api.wrapper.OrderWrapper;
import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.dto.PayRequestParam;
import com.cactus.guozy.core.service.CheckoutService;
import com.cactus.guozy.profile.domain.User;

@RestController
@RequestMapping("/orders")
public class OrderEndpoint extends BaseEndpoint {

	@Autowired
	protected CheckoutService checkoutService;

	@RequestMapping(value = { "/", "" }, method = RequestMethod.POST)
	public OrderWrapper submit(
			HttpServletRequest request, 
			@RequestBody OrderWrapper order,
			@RequestParam(value = "priceOrder", defaultValue = "true") boolean priceOrder) {
		Order real = order.upwrap();
		Long uid = getCurrentUserId();
		if(uid == null || !uid.equals(order.getSubject())) {
			throw new BizException("500", "未授权");
		}
		
		if(real.getIsSalerOrder()) {
			Saler saler = salerService.getById(order.getSubject());
			real = orderService.createSalerOrder(real, saler, priceOrder, order.getChannelId());
		} else {
			User user = userService.getById(order.getSubject());
			real = orderService.createOrderForUser(real, user, priceOrder);		
		}
		
		order.wrapDetails(real);

		return order;
	}
	
	@RequestMapping(value = { "/{orderid}"}, method = RequestMethod.GET)
	public OrderWrapper getOrder(@PathVariable("orderid") Long orderId) {
		Order real = orderService.findOrderById(orderId);
		OrderWrapper wrapper = new OrderWrapper();
		wrapper.wrapDetails(real);

		return wrapper;
	}

	@RequestMapping(value={"/checkout"}, method = RequestMethod.POST)
	public GenericWebResult performCheckout(HttpServletRequest request, @RequestBody PayRequestParam payRequestParam) {
		Map<String, Object> ret = checkoutService.performCheckout(payRequestParam);
		return GenericWebResult.success(ret);
    }

	@RequestMapping(value = { "/offer" }, method = RequestMethod.POST)
	public GenericWebResult addOffer(HttpServletRequest request, @RequestParam("orderId") Long orderId,
			@RequestParam("userOfferId") Long userOfferId) {
		orderService.addOfferToOrder(orderId, userOfferId);
		return GenericWebResult.success("ok");
	}

	@RequestMapping(value = { "/offer" }, method = RequestMethod.DELETE)
	public GenericWebResult delOffer(HttpServletRequest request, @RequestParam("orderId") Long orderId,
			@RequestParam("userOfferId") Long userOfferId) {
		orderService.removeOfferFromOrder(orderId, userOfferId);
		return GenericWebResult.success("ok");
	}

	@RequestMapping(value = { "/offers" }, method = RequestMethod.DELETE)
	public GenericWebResult delAllOffer(HttpServletRequest request, @RequestParam("orderId") Long orderId) {
		orderService.removeAllOffersFromOrder(orderId);
		return GenericWebResult.success("ok");
	}

	@RequestMapping(value = { "items/{itemId}" }, method = RequestMethod.DELETE)
	public GenericWebResult delItem(HttpServletRequest request, @PathVariable("itemId") Long itemId) {
		orderService.removeItemFromOrder(itemId);
		return GenericWebResult.success("ok");
	}

	@RequestMapping(value = { "items/{itemId}" }, method = RequestMethod.PUT)
	public GenericWebResult updateItem(HttpServletRequest request, @PathVariable("itemId") Long itemId,
			@RequestParam("quantity") Long quantity) {
		orderService.updateItem(itemId, quantity);
		return GenericWebResult.success("ok");
	}

	@RequestMapping(value = { "address" }, method = RequestMethod.POST)
	public GenericWebResult updateAddress(HttpServletRequest request, @RequestParam("orderId") Long orderId,
			@RequestParam("addrId") Long addrId) {
		orderService.updateAddress(orderId, addrId);
		return GenericWebResult.success("ok");
	}

	@RequestMapping(value = { "/topay" }, method = RequestMethod.GET)
	public List<OrderWrapper> findOrdersUnpay(HttpServletRequest request) {
		List<Order> orders = orderService.findOrdersUnpay(getCurrentUserId());
		List<OrderWrapper> wrappers = new ArrayList<>();
		for (Order order : orders) {
			OrderWrapper wrapper = new OrderWrapper();
			wrapper.wrapDetails(order);
			wrappers.add(wrapper);
		}
		return wrappers;
	}

	@RequestMapping(value = { "/history" }, method = RequestMethod.GET)
	public List<OrderWrapper> findOrdersHistory(HttpServletRequest request) {
		List<Order> orders = orderService.findOrdersCompleted(getCurrentUserId());
		List<OrderWrapper> wrappers = new ArrayList<>();
		for (Order order : orders) {
			OrderWrapper wrapper = new OrderWrapper();
			wrapper.wrapDetails(order);
			wrappers.add(wrapper);
		}
		return wrappers;
	}
	
}
