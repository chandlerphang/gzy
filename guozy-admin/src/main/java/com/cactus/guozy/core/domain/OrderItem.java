package com.cactus.guozy.core.domain;

import java.math.BigDecimal;

import com.cactus.guozy.core.service.PricingException;

public class OrderItem {
	
	private Long id;
	
	private Order order;
	
	private BigDecimal price;
	
	private int quantity;
	
	private Goods goods;
	
	public BigDecimal getTotalPrice() {
		if(price == null) {
			if(goods == null) {
				throw new PricingException("无法获取Order Item价格. Item Id: " + id);
			} else {
				price = goods.getPrice();
			}
		}
		return price.multiply(BigDecimal.valueOf(quantity));
	}
	
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

}
