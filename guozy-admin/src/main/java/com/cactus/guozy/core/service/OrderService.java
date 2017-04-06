package com.cactus.guozy.core.service;

import java.util.List;

import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderAdjustment;
import com.cactus.guozy.core.domain.OrderStatus;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.dto.OrderItemRequestDTO;
import com.cactus.guozy.profile.domain.User;

public interface OrderService {
	
	Order createOrderForUser(Order order, User user, boolean priceOrder);
	
	Order createSalerOrder(Order order, Saler saler, boolean priceOrder, String channelId);
	
	List<Order> findOrdersUnpay(Long userId);
	
	List<Order> findOrdersCompleted(Long userId);
	
	List<Order> findOrdersForSaler(Long salerId);
	
	List<Order> readOrdersForShopNotPROCESS(Long shopId,int perNum,int pageNum);
	
	void updateAddress(Long orderId, Long addrId);
	
	void addOfferToOrder(Long orderId, Long userOfferId);
	void removeOfferFromOrder(Long orderId, Long userOfferId);
	void removeAllOffersFromOrder(Long orderId);
	List<OrderAdjustment> findAllAdjustments(Long orderId);
	
	void updateTotal(Order order);
	
	void updateOrderNumber(Order order);
	
	Order findOrderById(Long orderId);
	
	Order findCartForUser(User user);
	
	List<Order> findOrdersForUser(User user);
	
	List<Order> findOrdersForUser(User user, OrderStatus status);
	
	Order findOrderByOrderNumber(String orderNumber);
	
	void cancelOrder(Order order);
	
	Order finishOrder(Order order);
	
	Order confirmOrder(Order order);
	
	Order addItem(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder);

    Order addItemWithPriceOverrides(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder);

    Order updateItemQuantity(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder);

    Order removeItem(Long orderId, Long orderItemId, boolean priceOrder);
    
    boolean acquireLock(Order order);

    boolean releaseLock(Order order);

	Order findOrderWithUserById(Long orderId);

	List<Order> findOrdersPayed();
    
}
