package com.cactus.guozy.api.wrapper;

import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.domain.SalerStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalerWrapper {
	
	private Long id;
	
	private String phone;

	private String nickname;

	private String avatar;
	
	private String token;
	
	private Long shopId;
	
	private String status;
	
	public void wrapDetails(Saler user) {
		wrapSummary(user);
	}

	public void wrapSummary(Saler user) {
		id = user.getId();
		phone = user.getPhone();
		nickname = user.getNickname();
		avatar = "/"+RuntimeEnvConfigService.resolveSystemProperty("asset.url.prefix","") + user.getAvatar();
		shopId = user.getShopId();
		status = SalerStatus.of(user.getStatus()).getDesc();
	}
	
	public Saler upwrap() {
		Saler saler = Saler.builder()
				.avatar(avatar)
				.shopId(shopId)
				.phone(phone)
				.nickname(nickname)
				.build();
		saler.setId(id);
		
		return saler;
	}

}
