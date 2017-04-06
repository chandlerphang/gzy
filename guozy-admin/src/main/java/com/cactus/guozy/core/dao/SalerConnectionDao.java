package com.cactus.guozy.core.dao;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.core.domain.Saler;
import com.cactus.guozy.core.domain.SalerConnection;

public interface SalerConnectionDao extends BaseDao<SalerConnection> {
	
	SalerConnection selectLastConnection(Saler saler, Long userId);

}
