package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.core.domain.FruitCommonSense;

public interface FruitCommonSenseDao {
	
	List<FruitCommonSense> readAll();
	
	int insert(FruitCommonSense cs);
	
	int update(FruitCommonSense cs);

}
