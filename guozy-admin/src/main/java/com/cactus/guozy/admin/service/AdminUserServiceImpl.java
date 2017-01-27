package com.cactus.guozy.admin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.admin.dao.AdminUserDao;
import com.cactus.guozy.admin.domain.AdminUser;

@Service("adminUserService")
public class AdminUserServiceImpl implements AdminUserService {

	@Autowired
	private AdminUserDao userDao;
	
	@Override
	public AdminUser findById(Long id) {
		return userDao.readById(id);
	}

}
