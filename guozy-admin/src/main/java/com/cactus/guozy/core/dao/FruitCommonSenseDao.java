package com.cactus.guozy.core.dao;

import java.util.List;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.core.domain.FruitCommonSense;

public interface FruitCommonSenseDao extends BaseDao<FruitCommonSense> {
	
	List<FruitCommonSense> readAll();
	
}
