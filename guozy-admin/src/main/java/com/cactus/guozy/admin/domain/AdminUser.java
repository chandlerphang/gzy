package com.cactus.guozy.admin.domain;

import java.util.HashSet;
import java.util.Set;

public class AdminUser {

    private Long id;

    protected String name;

    protected String login;

    protected String password;

    protected String email;

    protected String phoneNumber;

    protected Boolean activeStatus = Boolean.TRUE;

    protected Set<AdminRole> allRoles = new HashSet<AdminRole>();

    protected Set<AdminPermission> allPermissions = new HashSet<AdminPermission>();

    protected String unencodedPassword;
    
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Boolean activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Set<AdminRole> getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(Set<AdminRole> allRoles) {
        this.allRoles = allRoles;
    }

    public Set<AdminPermission> getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(Set<AdminPermission> allPermissions) {
        this.allPermissions = allPermissions;
    }
    
    public String getUnencodedPassword() {
        return unencodedPassword;
    }

    public void setUnencodedPassword(String unencodedPassword) {
        this.unencodedPassword = unencodedPassword;
    }

}
