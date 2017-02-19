package com.cactus.guozy.core.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.ibatis.type.JdbcType;

import tk.mybatis.mapper.annotation.ColumnType;

@Table(name="shop")
public class Shop {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name="name")
    private String name;

	@Column(name="address")
    private String address;

	@Column(name="ship_distance")
    private Short shipDistance;

	@Column(name="ship_price")
    private BigDecimal shipPrice;

	@Column(name="open_time")
	@ColumnType(jdbcType=JdbcType.TIME)
    private Date openTime;

	@Column(name="close_time")
	@ColumnType(jdbcType=JdbcType.TIME)
    private Date closeTime;
    
	@Transient
    private List<Category> categories;
    
    public Shop(){}
    
    public Shop(Long id){
    	this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
}