package com.cactus.guozy.admin.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.cactus.guozy.admin.security.PermissionType;

public class AdminPermission {

    protected Long id;

    protected String name;

    protected String type;

    protected String description;
    
    protected Set<AdminRole> allRoles= new HashSet<AdminRole>();

    protected Set<AdminUser> allUsers= new HashSet<AdminUser>();

    protected List<AdminPermission> allChildPermissions = new ArrayList<AdminPermission>();
    
    protected List<AdminPermission> allParentPermissions = new ArrayList<AdminPermission>();

    protected Boolean isFriendly = Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AdminRole> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(Set<AdminRole> allRoles) {
        this.allRoles = allRoles;
    }

    public PermissionType getType() {
    	return PermissionType.valueOf(type);
    }

    public void setType(PermissionType type) {
        if (type != null) {
            this.type = type.getName();
        }
    }

    public Set<AdminUser> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(Set<AdminUser> allUsers) {
        this.allUsers = allUsers;
    }

    public List<AdminPermission> getAllChildPermissions() {
        return allChildPermissions;
    }

    public List<AdminPermission> getAllParentPermissions() {
        return allParentPermissions;
    }

    public Boolean isFriendly() {
        if(isFriendly == null) {
            return false;
        }
        return isFriendly;
    }
    
}
