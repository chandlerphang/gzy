package com.cactus.guozy.api.wrapper;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.core.domain.Goods;
import com.cactus.guozy.core.domain.OrderItem;

@XmlRootElement(name = "orderitem")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class OrderItemWrapper {

	private Long id;

	private BigDecimal price;

	private BigDecimal quantity;
	
	@XmlElement
	private String name;
	
	@XmlElement
	private String pic;
	
	@XmlElement
	private Long goodsId;
	
	public void wrapDetails(OrderItem item) {
		wrapSummary(item);
	}

	public void wrapSummary(OrderItem item) {
		id = item.getId();
		price = item.getPrice();
		name= item.getName();
		if(item.getPic() != null) {
			pic = "/"+RuntimeEnvConfigService.resolveSystemProperty("asset.url.prefix","") + item.getPic();
		}
		quantity = item.getQuantity();
		if(item.getGoods() != null) {
			goodsId = item.getGoods().getId();
		}
	}
	
	public OrderItem upwrap() {
		OrderItem item = new OrderItem();
		item.setId(getId());
		item.setPrice(getPrice());
		item.setQuantity(getQuantity());
		item.setName(getName());
		item.setPic(getPic());
		if(getGoodsId() != null) {
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

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
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
