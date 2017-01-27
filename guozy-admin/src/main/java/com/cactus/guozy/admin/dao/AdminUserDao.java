package com.cactus.guozy.admin.dao;

import com.cactus.guozy.admin.domain.AdminUser;

public interface AdminUserDao {
	
	AdminUser readById(Long id);
	
	AdminUser readByLogin(String login);

}
