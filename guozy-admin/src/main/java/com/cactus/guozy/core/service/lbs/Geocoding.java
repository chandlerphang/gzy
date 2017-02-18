package com.cactus.guozy.core.service.lbs;

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
public class Geocoding {
	
	private Long status;
	
	private Location location;
	
	private Long precise;
	
	private Long confidence;
	
	private String level;

}
