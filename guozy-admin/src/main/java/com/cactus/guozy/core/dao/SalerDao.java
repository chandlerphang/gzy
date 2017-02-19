package com.cactus.guozy.core.dao;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.domain.SalerLock;

public interface SalerDao extends BaseDao<Saler> {
	
	int countSalerLock(Saler saler);
	
	int insertSalerLock(SalerLock sl);
	
	int acquireSalerLock(Saler saler, Long currentTime, Long timeToLive, Long belongTo);
	
	int releaseSalerLock(Saler saler);
	
}
