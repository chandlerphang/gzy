package com.cactus.guozy.core.domain;

import java.math.BigDecimal;
import java.util.Date;

public class Offer {
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private String discountType;
	
	private BigDecimal value;
	
	private Date startDate;
	
	private Date endDate;
	
	private Long maxUsesPerUser;
	
	private Long maxUsesPerOrder;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public OfferDiscountType getDiscountType() {
		return OfferDiscountType.getInstance(discountType);
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getMaxUsesPerUser() {
		return maxUsesPerUser;
	}

	public void setMaxUsesPerUser(Long maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
	}

	public Long getMaxUsesPerOrder() {
		return maxUsesPerOrder;
	}

	public void setMaxUsesPerOrder(Long maxUsesPerOrder) {
		this.maxUsesPerOrder = maxUsesPerOrder;
	}
	
}
