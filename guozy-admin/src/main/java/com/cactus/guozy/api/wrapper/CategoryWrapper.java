package com.cactus.guozy.api.wrapper;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cactus.guozy.core.domain.Category;
import com.cactus.guozy.core.domain.Goods;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement(name = "category")
@XmlAccessorType(value = XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class CategoryWrapper {
	
	@XmlElement
	private Long id;

	@XmlElement
    private String name;
    
	@XmlElementWrapper(name = "goods")
	@XmlElement(name="good")
    private List<GoodsWrapper> goods;
	
	public void wrapDetails(Category category) {
		wrapSummary(category);
		goods = new ArrayList<>();
		for(Goods g : category.getGoods()) {
			GoodsWrapper wrapper = new GoodsWrapper();
			wrapper.wrapSummary(g);
			goods.add(wrapper);
		}
	}

	public void wrapSummary(Category category) {
		id = category.getId();
		name = category.getName();
	}
	
	public Category upwrap() {
		Category category = new Category();
		category.setId(getId());
		category.setName(getName());
		if(getGoods() != null && !getGoods().isEmpty()) {
			List<Goods> goods = new ArrayList<Goods>();
			for(GoodsWrapper g : getGoods()) {
				goods.add(g.upwrap());
			}
			category.setGoods(goods);
		}
		return category;
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
        this.name = name == null ? null : name.trim();
    }
    
    public List<GoodsWrapper> getGoods() {
		return goods;
	}

	public void setGoods(List<GoodsWrapper> goods) {
		this.goods = goods;
	}

}
