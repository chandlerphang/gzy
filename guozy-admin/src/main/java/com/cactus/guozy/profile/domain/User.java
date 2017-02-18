package com.cactus.guozy.profile.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
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
	private Boolean deactivated;

	@Column(name="line_to_saler")
	private Boolean canLineToSaler;

	@Column(name="avatar")
	private String avatar;
	
	@Transient
	private List<Address> addrs;
	
	@Column(name="create_time")
	private Date dateCreated;
	
	public User(Long id) { this.id = id;}

}
