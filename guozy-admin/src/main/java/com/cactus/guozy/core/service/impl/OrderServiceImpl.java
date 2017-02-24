package com.cactus.guozy.core.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.common.exception.BizException;
import com.cactus.guozy.common.json.Jsons;
import com.cactus.guozy.core.dao.GoodsDao;
import com.cactus.guozy.core.dao.OrderAddressDao;
import com.cactus.guozy.core.dao.OrderAdjustmentDao;
import com.cactus.guozy.core.dao.OrderDao;
import com.cactus.guozy.core.dao.ShopDao;
import com.cactus.guozy.core.dao.UserOfferDao;
import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderAddress;
import com.cactus.guozy.core.domain.OrderAdjustment;
import com.cactus.guozy.core.domain.OrderItem;
import com.cactus.guozy.core.domain.OrderLock;
import com.cactus.guozy.core.domain.OrderStatus;
import com.cactus.guozy.core.domain.Page;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.domain.UserOffer;
import com.cactus.guozy.core.dto.OrderItemRequestDTO;
import com.cactus.guozy.core.dto.PushMsg;
import com.cactus.guozy.core.service.LockManager;
import com.cactus.guozy.core.service.MsgPushService;
import com.cactus.guozy.core.service.OrderService;
import com.cactus.guozy.core.service.PricingException;
import com.cactus.guozy.core.service.PricingService;
import com.cactus.guozy.core.service.offer.OfferService;
import com.cactus.guozy.core.util.StreamCapableTransactionalOperationAdapter;
import com.cactus.guozy.profile.domain.Address;
import com.cactus.guozy.profile.domain.User;
import com.cactus.guozy.profile.service.AddressService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("orderService")
public class OrderServiceImpl implements OrderService {

	@Autowired
	protected OrderDao orderDao;
	
	@Autowired 
	protected ShopDao shopDao;
	
	@Autowired
	protected UserOfferDao userOfferDao;
	
	@Autowired
	protected OrderAddressDao orderAddrDao;
	
	@Autowired
	protected AddressService addrService;
	
	@Autowired
	protected OrderAdjustmentDao orderAdjustmentDao;
	
	@Autowired 
	protected GoodsDao goodsDao;
	
	@Resource(name="offerService")
	protected OfferService offerService;
	
	@Resource(name="pricingService")
	protected PricingService pricingService;
	
	@Resource(name="getuiPushService")
	protected MsgPushService pushService;
	
	@Resource(name = "streamingTransactionCapableUtil")
    protected StreamingTransactionCapableUtil transUtil;
	
	@Autowired
	LockManager lockManager;
	
	@Override
	@Transactional
	public Order createOrderForUser(Order order, User user, boolean priceOrder) {
		if(order.getIsSalerOrder() != null && order.getIsSalerOrder()) {
			throw new BizException("500", "非用户订单");
		}
		
		order.setUser(user);
		order.setIsSalerOrder(false);
	    order.setSalePriceOverride(false);
	    
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
        
        // 
        insertOrderItem(order);
       
        List<UserOffer> offers = offerService.findUnusedOffers(user.getId());
        order.setCandidateOffers(offers);
        
        if(priceOrder) {
        	pricingService.executePricing(order);
        }
        
        return order;
	}
	
	private void insertOrderItem(Order order) {
		List<OrderItem> items = order.getOrderItems();
        StringBuilder ids = new StringBuilder();
        for(OrderItem item : items) {
        	ids.append(item.getGoods().getId() + ",");
        }
        
        String idlst = null;
        if(ids.length() > 0) {
        	idlst = ids.substring(0, ids.length() - 1);
        } else {
        	idlst = ids.toString();
        }
        
        List<Goods> goods = goodsDao.selectByIds(idlst);
        for(OrderItem item : items) {
        	for(Goods g : goods) {
        		if(g.getId().equals(item.getGoods().getId())) {
        			item.setOrder(order);
        			item.setName(g.getName());
        			item.setPic(g.getPic());
        			item.setPrice(g.getPrice());
        			item.setGoods(g);
        		}
        	}
        }
        
        if(orderDao.insertOrderItemBatch(items) != items.size()) {
        	throw new RuntimeException();
        }
	}
	
	@Override
	@Transactional
	public Order createSalerOrder(Order order, Saler saler, boolean priceOrder, String channelId) {
		if(order.getIsSalerOrder() == null || !order.getIsSalerOrder()) {
			throw new BizException("500", "非导购员订单");
		}
		
		if(order.getUser() == null) {
			throw new BizException("500", "找不到要生成订单的用户");
		}
		
		if(order.getSalePriceOverride() == null || order.getSalePriceOverride().equals(true)) {
			order.setSalePriceOverride(true);
			if(order.getSalePrice() == null || order.getSalePrice().compareTo(BigDecimal.ZERO) <= 0) {
				throw new BizException("500", "售价不能为空或0");
			}
		}
		
		order.setStatus(OrderStatus.IN_PROCESS);
        order.setDateCreated(new Date());
        order.setCreatedBy(saler.getId());
		order.setUpdatedBy(saler.getId());
        order.setDateUpdated(new Date());
        
        Shop shop = shopDao.selectByPrimaryKey(order.getShop().getId());
        order.setShipPrice(shop.getShipPrice());
        if(orderDao.insertOrder(order) != 1) {
        	throw new RuntimeException();
        }
        
        // 
        insertOrderItem(order);
        
        List<UserOffer> offers = offerService.findUnusedOffers(order.getUser().getId());
        order.setCandidateOffers(offers);
        
        if(priceOrder) {
        	pricingService.executePricing(order);
        }
        
        // 推送通知给用户
 		PushMsg msg = PushMsg.builder().msgType(4)
 				.subjectId(saler.getId())
 				.extra(order.getId())
 				.build();
 		try {
 			if(log.isDebugEnabled()) {
 				log.debug("向用户: " + order.getUser().getId() + " 推送导购员订单");
 			}
 			pushService.pushMsgToUser(channelId, Jsons.DEFAULT.toJson(msg));
 		} catch (RuntimeException e) {
 			log.error("消息推送失败: " + e.getMessage());
 			throw new BizException("500", "消息推送失败", e);
 		}
 		
        return order;
	}
	
	@Override
	public void updateOrderNumber(Order order) {
		orderDao.updateOrderNumber(order);
	}

	@Override
	public Order finishOrder(Order order) {
		orderDao.updateStatus(order.getId(), OrderStatus.COMPLETED.getType());
		return order;
	}
	
	@Override
	@Transactional
	public void addOfferToOrder(Long orderId, Long userOfferId) {
		User user = orderDao.readUserForOrder(orderId);
		if(user == null || user.getId() == null) {
			throw new RuntimeException();
		}
		
		UserOffer userOffer = null;
		List<UserOffer> offers = offerService.findUserOffers(user.getId());
		for(UserOffer uo : offers) {
			if(uo.getId().equals(userOfferId)) {
				userOffer = uo;
				break;
			}
		}
		
		if(userOffer == null) {
			throw new BizException("500", "该用户不具有对应的优惠券[" + userOfferId + "]");
		} else if(userOffer.getIsUsed().booleanValue() == true) {
			throw new BizException("500", "优惠券[" + userOfferId + "] 已使用");
		}
		
		offerService.setUsed(userOfferId);
		
		OrderAdjustment oa = OrderAdjustment.builder()
				.odrId(orderId)
				.usrofferId(userOfferId)
				.value(userOffer.getOffer().getValue())
				.reason(userOffer.getOffer().getName())
				.build();
		orderAdjustmentDao.insert(oa);
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
		usedOffer.setIsUsed(false);
		userOfferDao.updateByPrimaryKey(usedOffer);
		orderDao.deleteOrderAdjustment(orderId, userOfferId);
	}

	@Override
	@Transactional
	public void removeAllOffersFromOrder(Long orderId) {
		orderDao.deleteAllOrderAdjustments(orderId);
	}
	
	@Override
	public List<Order> findOrdersUnpay(Long userId) {
		List<Order> orders = orderDao.readOrdersUnpay(userId);
		return orders;
	}
	
	@Override
	public List<Order> findOrdersCompleted(Long userId) {
		List<Order> orders = orderDao.readOrdersCompleted(userId);
		return orders;
	}
	
	@Override
	public List<Order> findOrdersForSaler(Long salerId) {
		List<Order> orders = orderDao.readOrdersForSaler(salerId);
		return orders;
	}

	@Override
	public List<Order> readOrdersForShopNotPROCESS(Long shopId,int perNum,int pageNum) {
		List<Order> orders = orderDao.readOrdersForShopNotPROCESS(new Page(shopId,perNum,pageNum));
		for(Order order : orders) {
			order.setOrderItems(orderDao.readItemsForOrder(order.getId()));
		}
		return orders;
	}
	@Override
	@Transactional
	public void updateAddress(Long orderId, Long addrId) {
		OrderAddress condition = OrderAddress.builder().orderId(orderId).build();
		int count = orderAddrDao.selectCount(condition);
		if(count > 0) {
			orderAddrDao.delete(condition);
		}
		
		Address addr = addrService.getById(addrId);
		OrderAddress odraddr = OrderAddress.builder()
				.name(addr.getName())
				.phone(addr.getPhone())
				.addrLine1(addr.getAddrLine1())
				.addrLine2(addr.getAddrLine2())
				.lat(addr.getLat())
				.lng(addr.getLng())
				.build();
		odraddr.setOrderId(orderId);
		
		orderAddrDao.insert(odraddr);
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
	public Order findOrderById(Long orderId) {
		Order order = orderDao.readOrderById(orderId);
		return order;
	}
	
	@Override
	public Order findCartForUser(User user) {
		return orderDao.readCartForUser(user);
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
	@Transactional
	public boolean acquireLock(Order order) {
		int count = orderDao.countOrderLock(order);
        if (count == 0) {
        	// 对于同一个Order，可能会有多个线程进入该段逻辑。OrderLock的主键是orderId，
        	// 因此只会有一个线程成功插入数据，也即只有一个线程能成功获取到锁
            try {
                OrderLock sl = OrderLock.builder()
                		.orderId(order.getId())
                		.locked(true)
                		.lastUpdated(System.currentTimeMillis())
                		.build();
                return orderDao.insertOrderLock(sl) < 1 ? false : true;
            } catch (PersistenceException e) {
                return false;
            }
        }

        Long timeToLive = -1L;
        int rowsAffected = orderDao.acquireOrderLock(order, System.currentTimeMillis(), timeToLive);
        return rowsAffected == 1;
	}

	@Override
	public boolean releaseLock(Order order) {
		final boolean[] response = {false};
        try {
            transUtil.runTransactionalOperation(new StreamCapableTransactionalOperationAdapter() {
                @Override
                public void execute() throws Throwable {
                    int rowsAffected = orderDao.releaseOrderLock(order);
                    response[0] = rowsAffected == 1;
                }

                @Override
                public boolean shouldRetryOnTransactionLockAcquisitionFailure() {
                    return true;
                }
            }, RuntimeException.class);
        } catch (RuntimeException e) {
            log.error(String.format("Could not release order lock (%s)", order.getId()), e);
        }
        return response[0];
	}

}
