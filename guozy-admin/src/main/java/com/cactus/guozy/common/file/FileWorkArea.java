package com.cactus.guozy.common.file;

import java.io.File;

/**
 * @author Chaven Peng
 */
public class FileWorkArea {

    private String filePathLocation;

    public String getFilePathLocation() {
    	return filePathLocation;
    }
    
    public void setFilePathLocation(String filePathLocation) {
        if (!filePathLocation.endsWith(File.separator)) {
            this.filePathLocation = filePathLocation + File.separator;
        } else {
            this.filePathLocation = filePathLocation;
        }
    }
    
}
