package com.cactus.guozy.api.wrapper;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.core.domain.Offer;

@XmlRootElement(name = "offer")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OfferWrapper {

	@XmlElement
	private Long id;

	@XmlElement
	private String name;

	@XmlElement
	private String description;

	@XmlElement
	private String discountType;

	@XmlElement
	private BigDecimal value;

	@XmlElement
	private Long maxUsesPerOrder;
	
	@XmlElement
	private Long maxUsesPerUser;
	
	@XmlElement
	private Date startDate;
	
	@XmlElement
	private Date endDate;
	
	public void wrapDetails(Offer offer) {
		wrapSummary(offer);
	}

	public void wrapSummary(Offer offer) {
		id = offer.getId();
		name = offer.getDescription();
		description = offer.getDescription();
		discountType = offer.getDiscountType().getType();
		value = offer.getValue();
		maxUsesPerOrder = offer.getMaxUsesPerOrder();
		maxUsesPerUser = offer.getMaxUsesPerUser();
		startDate = offer.getStartDate();
		endDate = offer.getEndDate();
	}
	
	public Offer upwrap() {
		Offer offer = new Offer();
		offer.setId(id);
		offer.setDescription(description);
		offer.setDiscountType(discountType);
		offer.setValue(value);
		offer.setMaxUsesPerOrder(maxUsesPerOrder);
		offer.setMaxUsesPerUser(maxUsesPerUser);
		offer.setStartDate(startDate);
		offer.setEndDate(endDate);
		return offer;
	}

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

	public String getDiscountType() {
		return discountType;
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

	public Long getMaxUsesPerOrder() {
		return maxUsesPerOrder;
	}

	public void setMaxUsesPerOrder(Long maxUsesPerOrder) {
		this.maxUsesPerOrder = maxUsesPerOrder;
	}
	
	public Long getMaxUsesPerUser() {
		return maxUsesPerUser;
	}

	public void setMaxUsesPerUser(Long maxUsesPerUser) {
		this.maxUsesPerUser = maxUsesPerUser;
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
	
}
