package com.cactus.guozy.api.endpoint;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cactus.guozy.api.wrapper.OrderWrapper;
import com.cactus.guozy.api.wrapper.ShopWrapper;
import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.dto.GenericWebResult;
import com.cactus.guozy.core.service.AppSettingService;
import com.cactus.guozy.core.service.CatalogService;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.profile.domain.User;
import com.cactus.guozy.profile.service.UserService;

@RestController
public class AppEndpoint {
	
	@Resource(name="appSettingService")
	protected AppSettingService appSettingService;
	
	@Resource(name = "orderService")
	protected OrderService orderService;
	
	@Autowired
	protected UserService userService;
	
	@Resource(name="catalogService")
	protected CatalogService catalogService;
	
	@RequestMapping(value={"/aboutus"}, method = RequestMethod.GET)
	public GenericWebResult getAboutUs(HttpServletRequest request) {
		return GenericWebResult.success(appSettingService.findAboutusUrl());
	}
	
	@RequestMapping(value={"/fruitcs"}, method = RequestMethod.GET)
	public GenericWebResult getAllFruitCS(HttpServletRequest request) {
		return GenericWebResult.success(appSettingService.findAllFruitCommonSense());
	}
	
	@RequestMapping(value={"/salerShop"}, method = RequestMethod.GET)
	public GenericWebResult salerShop(HttpServletRequest request) {
		Long userId = getCurrentUserId();
		if(userId == null) {
			throw new BizException("500", "内部错误");
		}
		
		User user = userService.getById(userId);
		if(!user.getIsSaler()) {
			throw new BizException("500", "非导购员, 无权限");
		}
		
		Long shopId = user.getShopId();
		Shop shop = catalogService.findShopById(shopId);
		ShopWrapper wrapper = new ShopWrapper();
		wrapper.wrapSummary(shop);
		
		return GenericWebResult.success(wrapper);
	}
	
	protected Long getCurrentUserId() {
		SecurityContext ctx = SecurityContextHolder.getContext();
		if (ctx != null && ctx.getAuthentication() != null) {
			User temp = (User) ctx.getAuthentication().getDetails();
			return temp.getId();
		}

		return null;
	}
	
	@RequestMapping(value = { "/orders/whichSaler" }, method = RequestMethod.GET)
	public List<OrderWrapper> whichSaler(HttpServletRequest request) {
		Long userId = getCurrentUserId();
		if(userId == null) {
			throw new BizException("500", "内部错误");
		}
		
		User user = userService.getById(userId);
		if(!user.getIsSaler()) {
			throw new BizException("500", "非导购员, 无权限");
		}
		
		List<Order> orders = orderService.findOrdersForSaler(userId);
		List<OrderWrapper> wrappers = new ArrayList<>();
		for (Order order : orders) {
			OrderWrapper wrapper = new OrderWrapper();
			wrapper.wrapDetails(order);
			wrappers.add(wrapper);
		}
		return wrappers;
	}
	
	
}
