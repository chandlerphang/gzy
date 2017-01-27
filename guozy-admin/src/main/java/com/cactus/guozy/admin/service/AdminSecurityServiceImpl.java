package com.cactus.guozy.admin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cactus.guozy.admin.dao.AdminUserDao;
import com.cactus.guozy.admin.domain.AdminPermission;
import com.cactus.guozy.admin.domain.AdminRole;
import com.cactus.guozy.admin.domain.AdminUser;
import com.cactus.guozy.admin.security.PermissionType;

@Service("adminSecurityService")
public class AdminSecurityServiceImpl implements AdminSecurityService {

    final String[] DEFAULT_PERMISSIONS = { "PERMISSION_OTHER_DEFAULT" };
    
    @Autowired
    private AdminUserDao adminUserDao;
    
	@Override
	public List<AdminUser> readAllAdminUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminUser readAdminUserById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminUser readAdminUserByLogin(String login) {
		return adminUserDao.readByLogin(login);
	}

	@Override
	public AdminUser saveAdminUser(AdminUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAdminUser(AdminUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public AdminUser readAdminUsersByEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AdminRole> readAllAdminRoles() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminRole readAdminRoleById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminRole saveAdminRole(AdminRole role) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAdminRole(AdminRole role) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<AdminPermission> readAllAdminPermissions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminPermission readAdminPermissionById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AdminPermission saveAdminPermission(AdminPermission permission) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAdminPermission(AdminPermission permission) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType,
			String ceilingEntityFullyQualifiedName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean doesOperationExistForCeilingEntity(PermissionType permissionType,
			String ceilingEntityFullyQualifiedName) {
		// TODO Auto-generated method stub
		return false;
	}

}
