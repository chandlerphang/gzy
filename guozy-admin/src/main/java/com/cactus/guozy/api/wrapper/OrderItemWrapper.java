package com.cactus.guozy.api.wrapper;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.OrderItem;

@XmlRootElement(name = "orderitem")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OrderItemWrapper {

	@XmlElement
	private Long id;

	@XmlElement
	private BigDecimal price;

	@XmlElement
	private int quantity;

	@XmlElement
	private Long goodsId;
	
	@XmlElement
	private Goods goods;
	
	public void wrapDetails(OrderItem item) {
		wrapSummary(item);
	}

	public void wrapSummary(OrderItem item) {
		id = item.getId();
		price = item.getPrice();
		quantity = item.getQuantity();
		goodsId = item.getGoods() != null ? item.getGoods().getId() : null;
		goods = item.getGoods();
	}
	
	public OrderItem upwrap() {
		OrderItem item = new OrderItem();
		item.setId(getId());
		item.setPrice(getPrice());
		item.setQuantity(getQuantity());
		if(getGoods() != null) {
			item.setGoods(getGoods());
		} else if(getGoodsId() != null) {
			item.setGoods(new Goods(getGoodsId()));
		}
		
		return item;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

}
