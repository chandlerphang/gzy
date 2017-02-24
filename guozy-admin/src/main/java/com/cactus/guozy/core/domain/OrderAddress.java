package com.cactus.guozy.core.domain;

import javax.persistence.Column;
import javax.persistence.Table;

import com.cactus.guozy.common.BaseDomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="order_addr")
public class OrderAddress extends BaseDomain {
	
	private static final long serialVersionUID = -5287046118595084473L;

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
	
	@Column(name = "odr_id")
	private Long orderId;

}
