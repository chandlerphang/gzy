package com.cactus.guozy.common.file;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface FileService {

    FileWorkArea initializeWorkArea();

    void closeWorkArea(FileWorkArea workArea);

    File getResource(String name);

    File getResource(String name, Long timeout);

    File getLocalResource(String fullUrl);

    boolean checkForResourceOnClassPath(String name);

    InputStream getClasspathResource(String name);

    boolean removeResource(String name);

    String addOrUpdateResourceForPath(FileWorkArea workArea, File file, boolean removeFilesFromWorkArea);

    List<String> addOrUpdateResourcesForPaths(FileWorkArea workArea, boolean removeFilesFromWorkArea);

    List<String> addOrUpdateResourcesForPaths(FileWorkArea workArea, List<File> files, boolean removeFilesFromWorkArea);

}
