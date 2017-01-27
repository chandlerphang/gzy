package com.cactus.guozy.api.wrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.core.domain.Order;
import com.cactus.guozy.core.domain.OrderItem;
import com.cactus.guozy.core.domain.Shop;
import com.cactus.guozy.core.domain.UserOffer;
import com.cactus.guozy.profile.domain.Address;
import com.cactus.guozy.profile.domain.User;

@XmlRootElement(name = "order")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OrderWrapper {
	
	@XmlElement
	private Long id;
	
	@XmlElement
	private Long sid;
	
	@XmlElement
	private String orderNumber;

	@XmlElementWrapper(name = "items")
	@XmlElement(name="item")
	private List<OrderItemWrapper> orderItems;
	
	@XmlElement
	private Address shipAddr;
	
	@XmlElement
	private BigDecimal shipPrice;
	
	@XmlElementWrapper(name = "candidateOffers")
	@XmlElement(name="candidateOffer")
	private List<UserOfferWrapper> candidateOffers;
	
	@XmlElement
	private BigDecimal salePrice;
	
	@XmlElement
	private Boolean salePriceOverride;
	
	@XmlElement
	private Boolean isSalerOrder;
	
	@XmlElement
	private Long uid;
	
	public void wrapDetails(Order order) {
		wrapSummary(order);
		if(order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
			orderItems = new ArrayList<OrderItemWrapper>();
			for(OrderItem item : order.getOrderItems()) {
				OrderItemWrapper wrapper = new OrderItemWrapper();
				wrapper.wrapDetails(item);
				orderItems.add(wrapper);
			}
		}
		
		if(order.getCandidateOffers() != null && !order.getCandidateOffers().isEmpty()) {
			candidateOffers = new ArrayList<>();
			for(UserOffer userOffer : order.getCandidateOffers()) {
				UserOfferWrapper wrapper = new UserOfferWrapper();
				wrapper.wrapDetails(userOffer);
				candidateOffers.add(wrapper);
			}
		}
	}

	public void wrapSummary(Order order) {
		id = order.getId();
		orderNumber = order.getOrderNumber();
		shipAddr = order.getShipAddr();
		shipPrice = order.getShipPrice();
		salePrice = order.getSalePrice();
		salePriceOverride = order.getSalePriceOverride();
		isSalerOrder = order.getIsSalerOrder();
		sid = order.getShop() != null?order.getShop().getId():null;
		uid = order.getUser() != null?order.getUser().getId():null;
	}
	
	public Order upwrap() {
		Order order = new Order();
		order.setId(id);
		order.setOrderNumber(orderNumber);
		order.setShipAddr(shipAddr);
		order.setShipPrice(shipPrice);
		order.setIsSalerOrder(isSalerOrder);
		order.setSalePriceOverride(salePriceOverride);
		order.setShop(new Shop(sid));
		if(uid != null) {
			order.setUser(new User(uid));
		}
		
		if(orderItems != null && !orderItems.isEmpty()) {
			order.setOrderItems(new ArrayList<>());
			for(OrderItemWrapper wrapper : orderItems) {
				OrderItem item = wrapper.upwrap();
				order.getOrderItems().add(item);
			}
		}
		
		if(candidateOffers != null && !candidateOffers.isEmpty()) {
			order.setCandidateOffers(new ArrayList<>());
			for(UserOfferWrapper wrapper : candidateOffers) {
				UserOffer userOffer = wrapper.unwrap();
				order.getCandidateOffers().add(userOffer);
			}
		}
		
		return order;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<OrderItemWrapper> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemWrapper> orderItems) {
		this.orderItems = orderItems;
	}

	public Address getShipAddr() {
		return shipAddr;
	}

	public void setShipAddr(Address shipAddr) {
		this.shipAddr = shipAddr;
	}

	public BigDecimal getShipPrice() {
		return shipPrice;
	}

	public void setShipPrice(BigDecimal shipPrice) {
		this.shipPrice = shipPrice;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Boolean getSalePriceOverride() {
		return salePriceOverride;
	}

	public void setSalePriceOverride(Boolean salePriceOverride) {
		this.salePriceOverride = salePriceOverride;
	}

	public Boolean getIsSalerOrder() {
		return isSalerOrder;
	}

	public void setIsSalerOrder(Boolean isSalerOrder) {
		this.isSalerOrder = isSalerOrder;
	}

	public List<UserOfferWrapper> getCandidateOffers() {
		return candidateOffers;
	}

	public void setCandidateOffers(List<UserOfferWrapper> candidateOffers) {
		this.candidateOffers = candidateOffers;
	}

	public Long getSid() {
		return sid;
	}

	public void setSid(Long sid) {
		this.sid = sid;
	}
	
}
