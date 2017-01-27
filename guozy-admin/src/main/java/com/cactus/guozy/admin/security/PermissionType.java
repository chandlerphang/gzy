package com.cactus.guozy.admin.security;

public enum PermissionType {

    READ("Read"),
    CREATE("Create"),
    UPDATE("Update"),
    DELETE("Delete"),
    ALL("All"),
    OTHER("Other");

    private String friendlyName;

    private PermissionType(final String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getName() {
        return name();
    }

    public String getFriendlyName() {
        return friendlyName;
    }

}
