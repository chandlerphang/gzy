package com.cactus.guozy.core.domain;

import java.util.Date;

import javax.persistence.Table;

import com.cactus.guozy.common.BaseDomain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Table(name="feedback")
public class Feedback extends BaseDomain {

	private static final long serialVersionUID = -1101205669112022910L;

	private String nickname;
	
	private String phone;
	
	private String feedback;
	
	private Date dateCreated;
	
}
