package com.cactus.guozy.core.service;

import java.util.List;

import com.cactus.guozy.core.domain.FruitCommonSense;

public interface AppSettingService {
	
	public static final String ABOUT_US = "about_us";
	
	void saveAboutusUrl(String url);
	
	void saveFruitCommonSense(FruitCommonSense cs);
	
	String findAboutusUrl();
	
	List<FruitCommonSense> findAllFruitCommonSense();
	
}
