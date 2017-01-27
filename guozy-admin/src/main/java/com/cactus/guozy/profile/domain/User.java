package com.cactus.guozy.profile.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cactus.guozy.common.BaseDomain;

@Table(name="user")
public class User extends BaseDomain {

	private static final long serialVersionUID = 6726149518794271187L;

	@Column(name="nickname")
	private String nickname;

	@Column(name="password")
	private String password;

	@Column(name="phone")
	private String phone;

	@Column(name="deactivated")
	private Boolean deactivated = false;

	@Column(name="is_saler")
	private Boolean isSaler = false;

	@Column(name="line_to_saler")
	private Boolean canLineToSaler = true;

	@Column(name="avatar")
	private String avatar;
	
	@Transient
	private List<Address> addrs;
	
	@Column(name="sid")
	private Long shopId;
	
	@Column(name="create_time")
	private Date dateCreated;
	
	public User() {}
	
	public User(Long id) { this.id = id;}

	public Boolean getCanLineToSaler() {
		return canLineToSaler;
	}

	public void setCanLineToSaler(Boolean canLineToSaler) {
		this.canLineToSaler = canLineToSaler;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Boolean getDeactivated() {
		return deactivated;
	}

	public void setDeactivated(Boolean deactivated) {
		this.deactivated = deactivated;
	}

	public Boolean getIsSaler() {
		return isSaler;
	}

	public void setIsSaler(Boolean isSaler) {
		this.isSaler = isSaler;
	}

	public List<Address> getAddrs() {
		return addrs;
	}

	public void setAddrs(List<Address> addrs) {
		this.addrs = addrs;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

}
