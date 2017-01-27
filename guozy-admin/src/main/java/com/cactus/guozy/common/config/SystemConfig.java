package com.cactus.guozy.common.config;

public class SystemConfig {

    protected Long id;

    protected String name;

    protected String value;

    protected String propertyType;

    protected String friendlyName;

    protected String friendlyGroup;

    protected String friendlyTab;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyGroup() {
        return friendlyGroup;
    }

    public void setFriendlyGroup(String friendlyGroup) {
        this.friendlyGroup = friendlyGroup;
    }
    
    public String getFriendlyTab() {
        return friendlyTab;
    }

    public void setFriendlyTab(String friendlyTab) {
        this.friendlyTab = friendlyTab;
    }

    public SystemPropertyFieldType getPropertyType() {
        if (propertyType != null) {
            SystemPropertyFieldType returnType = SystemPropertyFieldType.valueOf(propertyType);
            if (returnType != null) {
                return returnType;
            }
        }
        return SystemPropertyFieldType.STRING;
    }

    public void setPropertyType(SystemPropertyFieldType propertyType) {
        this.propertyType = propertyType.toString();
    }
    
}
