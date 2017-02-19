package com.cactus.guozy.core.domain;

import java.math.BigDecimal;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.cactus.guozy.common.BaseDomain;

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
@Table(name="order_adjustment")
public class OrderAdjustment extends BaseDomain{

	private static final long serialVersionUID = -3123545384982388162L;

	@Transient
	protected Order order;

	@Transient
	protected Offer offer;
	
	@Transient
	protected UserOffer userOffer;

	protected String reason;

	protected BigDecimal value;
	
	protected Long odrId;
	
	protected Long usrofferId;

}
