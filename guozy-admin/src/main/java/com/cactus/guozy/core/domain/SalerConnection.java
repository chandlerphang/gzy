package com.cactus.guozy.core.domain;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Table(name="saler_connection")
public class SalerConnection {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	private Long salerId;
	
	private Long userId;
	
	private String salerCid;
	
	private String userCid;
	
	private Date startTime;
	
	private Date endTime;
	
	private String status;
	
	private String homeId;
	
}
