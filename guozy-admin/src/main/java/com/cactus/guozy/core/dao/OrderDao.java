package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderAdjustment;
import com.cactus.guozy.core.domain.OrderItem;
import com.cactus.guozy.core.domain.OrderLock;
import com.cactus.guozy.core.domain.OrderStatus;
import com.cactus.guozy.profile.domain.User;

public interface OrderDao {
	
	Order readOrderById(final Long orderId);
	
	int save(final Order order);
	
	List<Order> readOrdersForUser(final User user, final OrderStatus orderStatus);
	
	List<Order> readOrdersForUser(final User user);
	
	Order readCartForUser(final User user);
	
	Order readOrderByOrderNumber(final String orderNumber);
	
	int insertOrderItemBatch(List<OrderItem> items);
	
	int insertOrder(Order order);
	
	int deleteItem(Long itemId);
	
	int updateItem(Long itemId, Long quantity);
	
	int insertOfferToOrder(Long orderId, Long userOfferId);
	
	int deleteOrderAdjustment(Long orderId, Long userOfferId);
	
	int deleteAllOrderAdjustments(Long orderId);
	
	List<Order> readOrdersUnpay(Long userId);
	
	List<Order> readOrdersCompleted(Long userId);
	
	List<Order> readOrdersForSaler(Long salerId);
	
	List<OrderAdjustment> readAllAdjustments(Long orderId);
	
	int updateTotal(Order order);
	
	int updateAddress(Long orderId, Long addrId);
	
	int updateStatus(Long orderId, String status);
	
	int updateOrderNumber(Order order);
	
	User readUserForOrder(Long orderId);
	
	List<OrderItem> readItemsForOrder(Long orderId);
	
	int countOrderLock(Order order);
	
	int insertOrderLock(OrderLock ol);
	
	int acquireOrderLock(Order order, Long currentTime, Long timeToLive);
	
	int releaseOrderLock(Order order);
}
