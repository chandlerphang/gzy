package com.cactus.guozy.profile.dao;

import java.util.List;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.profile.domain.UserAddress;

public interface UserAddressDao extends BaseDao<UserAddress> {
	
	List<UserAddress> readByUserId(Long userId);
	
	int unDefaultAddr(Long userId);

}
