package com.cactus.guozy.api.wrapper;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.profile.domain.User;

public class UserWrapper {
	
	private Long id;
	
	private String phone;

	private String nickname;

	private Boolean isSaler = false;

	private Boolean canLineToSaler = true;
	
	private Boolean deactivated = false;
	
	private String avatar;
	
	private String token;
	
	private Long shopId;
	
	public void wrapDetails(User user) {
		wrapSummary(user);
	}

	public void wrapSummary(User user) {
		id = user.getId();
		phone = user.getPhone();
		nickname = user.getNickname();
		isSaler = user.getIsSaler();
		canLineToSaler = user.getCanLineToSaler();
		avatar = "/"+RuntimeEnvConfigService.resolveSystemProperty("asset.url.prefix","") + user.getAvatar();
		deactivated = user.getDeactivated();
		shopId = user.getShopId();
	}
	
	public User upwrap() {
		User user = new User();
		user.setId(getId());
		user.setAvatar(getAvatar());
		user.setCanLineToSaler(getCanLineToSaler());
		user.setDeactivated(getDeactivated());
		user.setNickname(getNickname());
		user.setIsSaler(getIsSaler());
		
		return user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Boolean getIsSaler() {
		return isSaler;
	}

	public void setIsSaler(Boolean isSaler) {
		this.isSaler = isSaler;
	}

	public Boolean getCanLineToSaler() {
		return canLineToSaler;
	}

	public void setCanLineToSaler(Boolean canLineToSaler) {
		this.canLineToSaler = canLineToSaler;
	}

	public Boolean getDeactivated() {
		return deactivated;
	}

	public void setDeactivated(Boolean deactivated) {
		this.deactivated = deactivated;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}
	
}
