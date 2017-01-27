package com.cactus.guozy.core.domain;

import java.math.BigDecimal;

public class OrderAdjustment {

	private Long id;

	protected Order order;

	protected Offer offer;
	
	protected UserOffer userOffer;

	protected String reason;

	protected BigDecimal value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public UserOffer getUserOffer() {
		return userOffer;
	}

	public void setUserOffer(UserOffer userOffer) {
		this.userOffer = userOffer;
	}

}
