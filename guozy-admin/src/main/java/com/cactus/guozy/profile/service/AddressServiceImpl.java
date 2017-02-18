package com.cactus.guozy.profile.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.common.service.BaseServiceImpl;
import com.cactus.guozy.profile.dao.AddressDao;
import com.cactus.guozy.profile.domain.Address;

@Service("addrService")
public class AddressServiceImpl extends BaseServiceImpl<Address> implements AddressService {

	@Autowired
	protected AddressDao addrDao;
	
	@Override
	public BaseDao<Address> getBaseDao() {
		return addrDao;
	}

}
