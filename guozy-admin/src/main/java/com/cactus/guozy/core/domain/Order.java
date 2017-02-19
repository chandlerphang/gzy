package com.cactus.guozy.core.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.cactus.guozy.profile.domain.User;

public class Order {

	private Long id;

	private String orderNumber;

	private User user;

	private Shop shop;

	private String status;

	private List<OrderItem> orderItems;

	private OrderAddress shipAddr;

	private BigDecimal shipPrice;

	private List<UserOffer> candidateOffers;

	private OrderPayment payment;

	private BigDecimal total;

	private BigDecimal subTotal;

	private BigDecimal salePrice;

	private Boolean salePriceOverride;

	private Boolean isSalerOrder;

	private Date dateCreated;

	private Date dateUpdated;

	private Long createdBy;

	private Long updatedBy;

	private Date dateSubmited;
	
	public Order() {}
	
	public Order(Long id) {
		this.id = id;
	}

	public BigDecimal calculateSubTotal() {
		BigDecimal subTotal = BigDecimal.ZERO;
		for (OrderItem orderItem : orderItems) {
			subTotal = subTotal.add(orderItem.getTotalPrice());
		}
		return subTotal;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public OrderStatus getStatus() {
		return OrderStatus.valueOf(status);
	}

	public void setStatus(OrderStatus status) {
		this.status = status.getType();
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public OrderAddress getShipAddr() {
		return shipAddr;
	}

	public void setShipAddr(OrderAddress shipAddr) {
		this.shipAddr = shipAddr;
	}

	public BigDecimal getShipPrice() {
		return shipPrice;
	}

	public void setShipPrice(BigDecimal shipPrice) {
		this.shipPrice = shipPrice;
	}

	public OrderPayment getPayment() {
		return payment;
	}

	public void setPayment(OrderPayment payment) {
		this.payment = payment;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(Date dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	public List<UserOffer> getCandidateOffers() {
		return candidateOffers;
	}

	public void setCandidateOffers(List<UserOffer> candidateOffers) {
		this.candidateOffers = candidateOffers;
	}

	public Date getDateSubmited() {
		return dateSubmited;
	}

	public void setDateSubmited(Date dateSubmited) {
		this.dateSubmited = dateSubmited;
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

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

}
