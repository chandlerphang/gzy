package com.cactus.guozy.admin.domain;

import java.util.HashSet;
import java.util.Set;

public class AdminRole {

    protected Long id;

    protected String name;

    protected String description;

    protected Set<AdminUser> allUsers = new HashSet<AdminUser>();

    protected Set<AdminPermission> allPermissions= new HashSet<AdminPermission>();

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

    public Set<AdminUser> getAllUsers() {
        return allUsers;
    }
    
    public void setAllUsers(Set<AdminUser> allUsers) {
        this.allUsers = allUsers;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAllPermissions(Set<AdminPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }
    
    public Set<AdminPermission> getAllPermissions() {
        return allPermissions;
    }

}
