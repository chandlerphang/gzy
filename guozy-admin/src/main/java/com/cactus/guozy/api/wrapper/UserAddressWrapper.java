package com.cactus.guozy.api.wrapper;

import java.util.ArrayList;
import java.util.List;

import com.cactus.guozy.profile.domain.Address;
import com.cactus.guozy.profile.domain.UserAddress;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class UserAddressWrapper {
	
	private Long id;
	
	private Long addrId;
	
	private String name;

	private String phone;

	private String addrLine1;

	private String addrLine2;

	private Double lat;

	private Double lng;
	
	private Boolean isDefault;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Address toAddress() {
		Address addr = new Address();
		addr.setName(name);
		addr.setPhone(phone);
		addr.setAddrLine1(addrLine1);
		addr.setAddrLine2(addrLine2);
		addr.setLat(lat);
		addr.setLng(lng);
		addr.setId(addrId);
		
		return addr;
	}
	
	public static List<UserAddressWrapper> toWrappers(List<UserAddress> addrs) {
		List<UserAddressWrapper> wrappers = new ArrayList<>();
		for(UserAddress addr : addrs) {
			UserAddressWrapper wrapper = new UserAddressWrapper();
			wrapper.setAddrLine1(addr.getAddr().getAddrLine1());
			wrapper.setAddrLine2(addr.getAddr().getAddrLine2());
			wrapper.setIsDefault(addr.getIsDefault());
			wrapper.setLat(addr.getAddr().getLat());
			wrapper.setLng(addr.getAddr().getLng());
			wrapper.setName(addr.getAddr().getName());
			wrapper.setPhone(addr.getAddr().getPhone());
			wrapper.setId(addr.getId());
			wrapper.setAddrId(addr.getAddr().getId());
			
			wrappers.add(wrapper);
		}
		
		return wrappers;
		
	}
	
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

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Long getAddrId() {
		return addrId;
	}

	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}

}
