package com.cactus.guozy.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.core.dao.GoodsDao;
import com.cactus.guozy.core.dao.OrderDao;
import com.cactus.guozy.core.dao.ShopDao;
import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderAdjustment;
import com.cactus.guozy.core.domain.OrderItem;
import com.cactus.guozy.core.domain.OrderStatus;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.domain.UserOffer;
import com.cactus.guozy.core.dto.OrderItemRequestDTO;
import com.cactus.guozy.core.service.OfferService;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.PricingException;
import com.cactus.guozy.profile.domain.User;

@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	protected OrderDao orderDao;
	
	@Autowired 
	protected ShopDao shopDao;
	
	@Autowired 
	protected GoodsDao goodsDao;
	
	@Resource(name="offerService")
	protected OfferService offerService;
	
	@Override
	public Order createNewCartForUser(User user) {
		Order order = new Order();
		order.setUser(user);
        order.setStatus(OrderStatus.IN_PROCESS);
        
        orderDao.save(order);
        return order;
	}
	
	@Override
	@Transactional
	public Order createOrderForUser(Order order, User user) {
		if(order.getIsSalerOrder() != null && order.getIsSalerOrder()) {
			if(!user.getIsSaler()) {
				throw new BizException("500", "非导购员，无权限");
			}
			
			if(order.getUser() == null) {
				throw new BizException("500", "找不到要生成订单的用户");
			}
			if(order.getSalePriceOverride() == null) {
				order.setSalePriceOverride(false);
			}
		} else {
			order.setUser(user);
			order.setIsSalerOrder(false);
		    order.setSalePriceOverride(false);
		}
        order.setStatus(OrderStatus.IN_PROCESS);
        order.setDateCreated(new Date());
        order.setCreatedBy(user.getId());
		order.setUpdatedBy(user.getId());
        order.setDateUpdated(new Date());
       
        Shop shop = shopDao.selectByPrimaryKey(order.getShop().getId());
        order.setShipPrice(shop.getShipPrice());
        if(orderDao.insertOrder(order) != 1) {
        	throw new RuntimeException();
        }
        
        List<OrderItem> items = order.getOrderItems();
        for(OrderItem item : items) {
        	item.setOrder(order);
        	Goods goods = goodsDao.selectByPrimaryKey(item.getGoods().getId());
        	item.setPrice(goods.getPrice());
        }
        
        if(orderDao.insertOrderItemBatch(items) != items.size()) {
        	throw new RuntimeException();
        }
        
        List<UserOffer> offers = offerService.findUnusedOffers(user.getId());
        order.setCandidateOffers(offers);
        
        return order;
	}
	
	@Override
	@Transactional
	public void removeItemFromOrder(Long itemId) {
		orderDao.deleteItem(itemId);
	}
	
	@Override
	@Transactional
	public void updateItem(Long itemId, Long quantity) {
		orderDao.updateItem(itemId, quantity);
	}
	
	@Override
	@Transactional
	public void addOfferToOrder(Long orderId, Long userOfferId) {
		User user = orderDao.readUserForOrder(orderId);
		if(user == null || user.getId() == null) {
			throw new RuntimeException();
		}
		
		boolean found = false;
		List<UserOffer> unusedOffers = offerService.findUnusedOffers(user.getId());
		for(UserOffer userOffer : unusedOffers) {
			if(userOffer.getId() == userOfferId) {
				found = true;
				break;
			}
		}
		
		if(found == false) {
			throw new RuntimeException();
		}
		offerService.setUsed(userOfferId);
		orderDao.insertOfferToOrder(orderId, userOfferId);
	}

	@Override
	@Transactional
	public void removeOfferFromOrder(Long orderId, Long userOfferId) {
		User user = orderDao.readUserForOrder(orderId);
		if(user == null || user.getId() == null) {
			throw new RuntimeException();
		}
		
		UserOffer usedOffer = offerService.findUserOffer(userOfferId);
		if(usedOffer == null || !user.getId().equals(usedOffer.getUser().getId())) {
			throw new RuntimeException(); 
		}
		orderDao.deleteOrderAdjustment(orderId, userOfferId);
	}

	@Override
	@Transactional
	public void removeAllOffersFromOrder(Long orderId) {
		orderDao.deleteAllOrderAdjustments(orderId);
	}
	
	@Override
	public List<Order> findOrdersUnpay(Long userId) {
		return orderDao.readOrdersUnpay(userId);
	}
	
	@Override
	public List<Order> findOrdersCompleted(Long userId) {
		return orderDao.readOrdersCompleted(userId);
	}
	
	@Override
	public List<Order> findOrdersForSaler(Long salerId) {
		return orderDao.readOrdersForSaler(salerId);
	}
	
	@Override
	@Transactional
	public void updateAddress(Long orderId, Long addrId) {
		orderDao.updateAddress(orderId, addrId);
	}
	
	@Override
	public List<OrderAdjustment> findAllAdjustments(Long orderId) {
		return orderDao.readAllAdjustments(orderId);
	}

	@Override
	@Transactional
	public void updateTotal(Order order) {
		if(order.getTotal() == null || order.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
			throw new PricingException();
		} 
		
		if(order.getSubTotal() == null || order.getSubTotal().compareTo(BigDecimal.ZERO) <= 0) {
			throw new PricingException();
		} 
		
		orderDao.updateTotal(order);
	}
	
	@Override
	public Order findCartForUser(User user) {
		return orderDao.readCartForUser(user);
	}

	@Override
	public Order findOrderById(Long orderId) {
		return orderDao.readOrderById(orderId);
	}

	@Override
	public List<Order> findOrdersForUser(User user) {
		return orderDao.readOrdersForUser(user);
	}

	@Override
	public List<Order> findOrdersForUser(User user, OrderStatus status) {
		return orderDao.readOrdersForUser(user, status);
	}

	@Override
	public Order findOrderByOrderNumber(String orderNumber) {
		return orderDao.readOrderByOrderNumber(orderNumber);
	}

	@Override
	public void cancelOrder(Order order) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Order confirmOrder(Order order) {
		orderDao.updateStatus(order.getId(), OrderStatus.SUBMITTED.getType());
		return order;
	}

	@Override
	public Order addItem(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order addItemWithPriceOverrides(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order updateItemQuantity(Long orderId, OrderItemRequestDTO orderItemRequestDTO, boolean priceOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Order removeItem(Long orderId, Long orderItemId, boolean priceOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean acquireLock(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean releaseLock(Order order) {
		// TODO Auto-generated method stub
		return false;
	}

}
