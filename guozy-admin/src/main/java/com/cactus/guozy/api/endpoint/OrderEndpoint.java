package com.cactus.guozy.api.endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cactus.guozy.api.WebServiceException;
import com.cactus.guozy.api.wrapper.OrderWrapper;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.dto.PayRequestParam;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.PayRouteService;
import com.cactus.guozy.profile.domain.User;

@RestController
@RequestMapping("/orders")
public class OrderEndpoint {

	@Resource(name = "orderService")
	protected OrderService orderService;

	@Resource(name = "payRouteService")
	private PayRouteService payRouteService;

	@RequestMapping(value = { "/", "" }, method = RequestMethod.POST)
	public OrderWrapper submit(HttpServletRequest request, @RequestBody OrderWrapper order) {
		Order real = order.upwrap();
		User user = getCurrentUser();
		if (user == null) {
			throw WebServiceException.build(500);
		}

		real = orderService.createOrderForUser(real, user);
		order.wrapDetails(real);

		return order;
	}

	@RequestMapping(value={"/checkout"}, method = RequestMethod.POST)
	public GenericWebResult performCheckout(HttpServletRequest request, @RequestBody PayRequestParam payRequestParam) {
		orderService.confirmOrder(new Order(payRequestParam.getOrderId()));
		return GenericWebResult.success(payRouteService.getPayRetMap(payRequestParam));
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
		List<Order> orders = orderService.findOrdersUnpay(getCurrentUser().getId());
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
		List<Order> orders = orderService.findOrdersCompleted(getCurrentUser().getId());
		List<OrderWrapper> wrappers = new ArrayList<>();
		for (Order order : orders) {
			OrderWrapper wrapper = new OrderWrapper();
			wrapper.wrapDetails(order);
			wrappers.add(wrapper);
		}
		return wrappers;
	}
	
	protected User getCurrentUser() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx != null && ctx.getAuthentication() != null) {
			return (User) ctx.getAuthentication().getDetails();
		}

		return null;
	}

}
