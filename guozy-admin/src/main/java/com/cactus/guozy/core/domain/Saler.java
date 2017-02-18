package com.cactus.guozy.core.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cactus.guozy.common.BaseDomain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="saler")
public class Saler extends BaseDomain {
	
	private static final long serialVersionUID = 5335518030347232673L;

	@Column(name="nickname")
	private String nickname;

	@Column(name="password")
	private String password;

	@Column(name="phone")
	private String phone;

	@Column(name="avatar")
	private String avatar;
	
	@Column(name="channel_id")
	private String channelId;
	
	@Column(name="shop_id")
	private Long shopId;
		
	@Transient
	private Shop shop;
	
	@Column(name="create_time")
	private Date dateCreated;
	
	@Column(name="last_active_time")
	private Date lastActiveTime;
	
	@Column(name="status")
	private Integer status; 

}
