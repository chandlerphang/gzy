package com.cactus.guozy.common.file.supports;

import java.io.File;
import java.util.List;

import com.cactus.guozy.common.file.FileAppType;
import com.cactus.guozy.common.file.FileWorkArea;

/**
 * @author Chaven Peng
 */
public interface FileServiceProvider {
    
    File getResource(String url);

    File getResource(String url, FileAppType fileApplicationType);

    List<String> addOrUpdateResourcesForPaths(FileWorkArea workArea, List<File> files, boolean removeFilesFromWorkArea);
    
    boolean removeResource(String name);
    
}
