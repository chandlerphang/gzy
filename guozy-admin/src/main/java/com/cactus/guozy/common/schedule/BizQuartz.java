package com.cactus.guozy.common.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cactus.guozy.core.service.SalerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BizQuartz {
	
	@Autowired
	SalerService salerService;
	
	@Scheduled(cron = "0 0/1 * * * ? ")
	public void addUserScore() {
		log.info("检查所有导购员的状态");
	}
	
}
