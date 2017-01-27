package com.cactus.guozy.admin.security;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cactus.guozy.admin.domain.AdminPermission;
import com.cactus.guozy.admin.domain.AdminRole;
import com.cactus.guozy.admin.domain.AdminUser;
import com.cactus.guozy.admin.service.AdminSecurityService;

/**
 * Spring Security中UserDetailsService的自定义实现
 */
public class AdminUserDetailsServiceImpl implements UserDetailsService {

    @Resource(name="adminSecurityService")
    protected AdminSecurityService adminSecurityService;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        AdminUser adminUser = adminSecurityService.readAdminUserByLogin(username);
        if (adminUser == null || adminUser.getActiveStatus() == null || !adminUser.getActiveStatus()) {
            throw new UsernameNotFoundException("user is not exists.");
        }

        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (AdminRole role : adminUser.getAllRoles()) {
            for (AdminPermission permission : role.getAllPermissions()) {
                if(permission.isFriendly()) {
                    for (AdminPermission childPermission : permission.getAllChildPermissions()) {
                        authorities.add(new SimpleGrantedAuthority(childPermission.getName()));
                    }
                } else {
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                }
            }
        }
        for (AdminPermission permission : adminUser.getAllPermissions()) {
            if(permission.isFriendly()) {
                for (AdminPermission childPermission : permission.getAllChildPermissions()) {
                    authorities.add(new SimpleGrantedAuthority(childPermission.getName()));
                }
            } else {
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }
        }
        for (String perm : AdminSecurityService.DEFAULT_PERMISSIONS) {
            authorities.add(new SimpleGrantedAuthority(perm));
        }
        return new AdminUserDetails(adminUser, username, adminUser.getPassword(), true, true, true, true, authorities);
    }

}
