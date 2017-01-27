package com.cactus.guozy.common.file.supports;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cactus.guozy.common.file.FileAppType;
import com.cactus.guozy.common.file.FileServiceException;
import com.cactus.guozy.common.file.FileWorkArea;

/**
 * @author Chaven Peng
 */
@Service("defaultFileServiceProvider")
public class FSFileServiceProvider implements FileServiceProvider {
	
	private static final Logger LOG = LoggerFactory.getLogger(FSFileServiceProvider.class);

    @Value("${file.service.base.directory}")
    protected String fsBaseDirectory;

    @Value("${file.service.directory.depth:2}")
    protected int directoryDepth;

    protected String baseDirectory;

    @Override
    public File getResource(String url) {
        return getResource(url, FileAppType.ALL);
    }

    @Override
    public File getResource(String url, FileAppType fileAppType) {
        String fileName = buildResourceName(url);
        String filePath = FilenameUtils.normalize(getBaseDirectory() + File.separator + fileName);
        return new File(filePath);
    }
    
    @Override
    public List<String> addOrUpdateResourcesForPaths(FileWorkArea workArea, List<File> files, boolean removeFilesFromWorkArea) {
        List<String> result = new ArrayList<String>();
        for (File srcFile : files) {
            if (!srcFile.getAbsolutePath().startsWith(workArea.getFilePathLocation())) {
                throw new FileServiceException("Attempt to update file " + srcFile.getAbsolutePath() +
                        " that is not in the passed in WorkArea " + workArea.getFilePathLocation());
            }

            String fileName = srcFile.getAbsolutePath().substring(workArea.getFilePathLocation().length());
            
            String url = FilenameUtils.separatorsToUnix(fileName);
            String resourceName = buildResourceName(url);
            String destinationFilePath = FilenameUtils.normalize(getBaseDirectory() + File.separator + resourceName);
            File destFile = new File(destinationFilePath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }
            
            try {
                if (removeFilesFromWorkArea) {
                    if (destFile.exists()) {
                        FileUtils.deleteQuietly(destFile);
                    }
                    FileUtils.moveFile(srcFile, destFile);
                } else {
                    FileUtils.copyFile(srcFile, destFile);
                }
                result.add(fileName);
            } catch (IOException ioe) {
                throw new FileServiceException("Error copying resource named " + fileName + " from workArea " +
                        workArea.getFilePathLocation() + " to " + resourceName, ioe);
            }
        }
        return result;
    }

    @Override
    public boolean removeResource(String name) {
        String resourceName = buildResourceName(name);
        String filePathToRemove = FilenameUtils.normalize(getBaseDirectory() + File.separator + resourceName);
        File fileToRemove = new File(filePathToRemove);
        return fileToRemove.delete();
    }

    protected String buildResourceName(String url) {
        String fileHash = null;
        if (!url.startsWith("/")) {
            fileHash = DigestUtils.md5Hex("/" + url);
        } else {
            fileHash = DigestUtils.md5Hex(url);
        }

        String resourceName = "";
        for (int i = 1; i < directoryDepth; i++) {
            if (i == 4) {
                LOG.warn("Property directoryDepth set to high, ignoring values past 4 - value set to" + directoryDepth);
                break;
            }
            resourceName = FilenameUtils.concat(resourceName, fileHash.substring((i-1) * 2, i * 2));
        }

        return FilenameUtils.concat(resourceName, FilenameUtils.getName(url));
    }

    protected String getBaseDirectory() {
        if (baseDirectory == null) {
            if (StringUtils.isNotBlank(fsBaseDirectory)) {
                baseDirectory = fsBaseDirectory;
            } else {
                baseDirectory = System.getProperty("java.io.tmpdir");
            }
        }
        
        return baseDirectory;
    }

}
