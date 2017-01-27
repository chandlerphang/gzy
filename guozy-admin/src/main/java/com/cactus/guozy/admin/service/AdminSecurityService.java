package com.cactus.guozy.admin.service;

import java.util.List;

import com.cactus.guozy.admin.domain.AdminPermission;
import com.cactus.guozy.admin.domain.AdminRole;
import com.cactus.guozy.admin.domain.AdminUser;
import com.cactus.guozy.admin.security.PermissionType;

public interface AdminSecurityService {

    final String[] DEFAULT_PERMISSIONS = { "PERMISSION_OTHER_DEFAULT" };

    List<AdminUser> readAllAdminUsers();
    
    AdminUser readAdminUserById(Long id);
    
    AdminUser readAdminUserByLogin(String login);
    
    AdminUser saveAdminUser(AdminUser user);
    
    void deleteAdminUser(AdminUser user);
    
    AdminUser readAdminUsersByEmail(String email);

    List<AdminRole> readAllAdminRoles();
    
    AdminRole readAdminRoleById(Long id);
    
    AdminRole saveAdminRole(AdminRole role);
    
    void deleteAdminRole(AdminRole role);

    List<AdminPermission> readAllAdminPermissions();
    
    AdminPermission readAdminPermissionById(Long id);
    
    AdminPermission saveAdminPermission(AdminPermission permission);
    
    void deleteAdminPermission(AdminPermission permission);

    boolean isUserQualifiedForOperationOnCeilingEntity(AdminUser adminUser, PermissionType permissionType, String ceilingEntityFullyQualifiedName);
    
    boolean doesOperationExistForCeilingEntity(PermissionType permissionType, String ceilingEntityFullyQualifiedName);

}
