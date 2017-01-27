package com.cactus.guozy.admin.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.cactus.guozy.admin.domain.AdminUser;

public class AdminUserDetails extends User {
    
    private static final long serialVersionUID = 1L;
    
    protected AdminUser adminUser;
    
    public AdminUserDetails(AdminUser adminUser, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(adminUser, username, password, true, true, true, true, authorities);
    }
    
    public AdminUserDetails(AdminUser adminUser, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.adminUser = adminUser;
    }
    
	public AdminUser getAdminUser() {
		return adminUser;
	}

	public void setAdminUser(AdminUser adminUser) {
		this.adminUser = adminUser;
	}

}