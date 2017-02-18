package com.cactus.guozy.profile.domain;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cactus.guozy.common.BaseDomain;

@Table(name="user_addr")
public class UserAddress extends BaseDomain {
	
	private static final long serialVersionUID = 1L;

	@Column(name="name")
	private String name;
	
	@Column(name="is_def")
	private Boolean isDefault;
	
	@Column(name="addr")
	private Long addrId;
	
	/**
	 * addrId 对应的Address
	 */
	@Transient
	private Address addr;
	
	@Column(name="user")
	private Long userId;
	
	/**
	 * userId 对应的User
	 */
	@Transient
	private User user;

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public Address getAddr() {
		return addr;
	}

	public void setAddr(Address addr) {
		this.addr = addr;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getAddrId() {
		return addrId;
	}

	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
