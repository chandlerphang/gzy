package com.cactus.guozy.core.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cactus.guozy.common.TransactionUtils;
import com.cactus.guozy.common.config.SystemConfig;
import com.cactus.guozy.common.config.dao.SystemConfigDao;
import com.cactus.guozy.core.dao.FruitCommonSenseDao;
import com.cactus.guozy.core.domain.FruitCommonSense;
import com.cactus.guozy.core.service.AppSettingService;

@Service("appSettingService")
public class AppSettingServiceImpl implements AppSettingService{

	@Autowired
	SystemConfigDao systemConfigDao;
	
	@Autowired
	FruitCommonSenseDao fruitDao;
	
	@Override
	@Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
	public void saveAboutusUrl(String url) {
		int ret = systemConfigDao.update(AppSettingService.ABOUT_US, url);
		if(ret < 1) {
			systemConfigDao.insert(AppSettingService.ABOUT_US, url);
		}
	}

	@Override
	@Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
	public void saveFruitCommonSense(FruitCommonSense cs) {
		if(cs.getId() != null) {
			fruitDao.update(cs);
		} else {
			fruitDao.insert(cs);
		}
	}

	@Override
	public String findAboutusUrl() {
		SystemConfig config = systemConfigDao.readPropertyByName(AppSettingService.ABOUT_US);
		return config == null ? null : config.getValue();
	}

	@Override
	public List<FruitCommonSense> findAllFruitCommonSense() {
		return fruitDao.readAll();
	}

}
