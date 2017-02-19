package com.cactus.guozy.core.domain;

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
public class OrderLock {
	
	private Long orderId;
	
	private Boolean locked;
	
	private Long lastUpdated;
	
}
