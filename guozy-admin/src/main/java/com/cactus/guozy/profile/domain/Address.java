package com.cactus.guozy.profile.domain;

import javax.persistence.Column;
import javax.persistence.Table;

import com.cactus.guozy.common.BaseDomain;

@Table(name = "address")
public class Address extends BaseDomain {

	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;

	@Column(name = "phone")
	private String phone;

	@Column(name = "addr_line1")
	private String addrLine1;

	@Column(name = "addr_line2")
	private String addrLine2;

	@Column(name = "lat")
	private Double lat;

	@Column(name = "lng")
	private Double lng;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddrLine1() {
		return addrLine1;
	}

	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}

	public String getAddrLine2() {
		return addrLine2;
	}

	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

}
