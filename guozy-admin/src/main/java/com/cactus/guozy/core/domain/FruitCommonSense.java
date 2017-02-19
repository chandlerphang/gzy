package com.cactus.guozy.core.domain;

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
@NoArgsConstructor
@AllArgsConstructor
@Table(name="fruit_cs")
public class FruitCommonSense extends BaseDomain {
	
	private static final long serialVersionUID = 398033849173876926L;

	private String title;
	
	private String picurl;
	
	private String cnturl;

}
