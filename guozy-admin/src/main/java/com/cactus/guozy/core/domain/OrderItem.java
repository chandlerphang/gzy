package com.cactus.guozy.core.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cactus.guozy.common.BaseDomain;
import com.cactus.guozy.core.service.PricingException;

@Table(name="order_item")
public class OrderItem extends BaseDomain {

	private static final long serialVersionUID = -5613863638728060744L;

	@Column
	private Long odrId;
	
	@Transient
	private Order order;
	
	private BigDecimal price;
	
	private String name;
	
	private String pic;
	
	private Integer quantity;
	
	@Column
	private Long goodsId;
	
	@Transient
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}
