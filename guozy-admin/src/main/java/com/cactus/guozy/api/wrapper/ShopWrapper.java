package com.cactus.guozy.api.wrapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.Shop;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement(name = "shop")
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class ShopWrapper {

	@XmlElement
	private Long id;

	@XmlElement
	private String name;

	@XmlElement
	private String address;

	@XmlElement
	private Short shipDistance;

	@XmlElement
	private BigDecimal shipPrice;

	@XmlElement
	private Date openTime;

	@XmlElement
	private Date closeTime;

	@XmlElementWrapper(name = "categories")
	@XmlElement(name = "category")
	private List<CategoryWrapper> categories;

	public void wrapDetails(Shop shop) {
		wrapSummary(shop);
		categories = new ArrayList<>();
		for(Category c : shop.getCategories()) {
			CategoryWrapper wrapper = new CategoryWrapper();
			wrapper.wrapSummary(c);
			categories.add(wrapper);
		}
	}

	public void wrapSummary(Shop shop) {
		id = shop.getId();
		name = shop.getName();
		address = shop.getAddress();
		shipDistance = shop.getShipDistance();
		shipPrice = shop.getShipPrice();
		openTime = shop.getOpenTime();
		closeTime= shop.getCloseTime();
	}
	
	public Shop upwrap() {
		Shop shop = new Shop();
		shop.setId(getId());
		shop.setName(getName());
		shop.setAddress(getAddress());
		shop.setShipDistance(getShipDistance());
		shop.setShipPrice(getShipPrice());
		shop.setOpenTime(getOpenTime());
		shop.setCloseTime(getCloseTime());
		if(getCategories() != null && !getCategories().isEmpty()) {
			List<Category> categories = new ArrayList<Category>();
			for(CategoryWrapper c : getCategories()) {
				categories.add(c.upwrap());
			}
			shop.setCategories(categories);
		}
		return shop;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Short getShipDistance() {
		return shipDistance;
	}

	public void setShipDistance(Short shipDistance) {
		this.shipDistance = shipDistance;
	}

	public BigDecimal getShipPrice() {
		return shipPrice;
	}

	public void setShipPrice(BigDecimal shipPrice) {
		this.shipPrice = shipPrice;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public List<CategoryWrapper> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryWrapper> categories) {
		this.categories = categories;
	}

}
