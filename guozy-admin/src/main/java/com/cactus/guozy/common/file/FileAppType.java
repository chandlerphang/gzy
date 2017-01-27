package com.cactus.guozy.common.file;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author Chaven Peng
 */
public class FileAppType {

    private static final Map<String, FileAppType> TYPES = new LinkedHashMap<String, FileAppType>();

    public static final FileAppType ALL = new FileAppType("ALL", "All");
    public static final FileAppType IMAGE = new FileAppType("IMAGE", "Images");
    public static final FileAppType STATIC = new FileAppType("STATIC", "Static Assets");

    public static FileAppType getInstance(final String type) {
        return TYPES.get(type);
    }

    private String name;
    private String friendlyName;

    public FileAppType() {
        //do nothing
    }

    public FileAppType(final String name, final String friendlyName) {
        this.friendlyName = friendlyName;
        setName(name);
    }

	public String getName() {
		return name;
	}

	public String getFriendlyName() {
		return friendlyName;
	}

    private void setName(final String name) {
        this.name = name;
        if (!TYPES.containsKey(name)) {
            TYPES.put(name, this);
        }
    }

    @Override
    public int hashCode() {
    	return new HashCodeBuilder().append(name).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
    	if (obj == null) { return false; }
    	if (obj == this) { return true; }
    	if (obj.getClass() != getClass()) {
    		return false;
    	}
    	FileAppType other = (FileAppType) obj;
    	return new EqualsBuilder()
    			.appendSuper(super.equals(obj))
    			.append(name, other.name)
    			.isEquals();
    }

}
