package com.cactus.guozy.core.domain;

import java.util.List;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.cactus.guozy.common.BaseDomain;

@Table(name="category")
public class Category extends BaseDomain {

	private static final long serialVersionUID = -8469762733026259998L;

    private String name;
    
    @Transient
    private Shop shop;
    
    @Transient
    private List<Goods> goods;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

}