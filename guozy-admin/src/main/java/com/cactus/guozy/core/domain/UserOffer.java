package com.cactus.guozy.core.domain;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.cactus.guozy.common.BaseDomain;
import com.cactus.guozy.profile.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user_offer")
public class UserOffer extends BaseDomain {

	private static final long serialVersionUID = 172621463440775276L;

	@Transient
	private User user;
	
	@Transient
	private Offer offer;
	
	private Long userId;
	
	private Long offerId;
	
	private Boolean isUsed;

}
