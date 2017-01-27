package com.cactus.guozy.common.file.supports;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.cactus.guozy.common.file.FileService;
import com.cactus.guozy.common.file.FileServiceException;
import com.cactus.guozy.common.file.FileWorkArea;

@Service("fileService")
public class FileServiceImpl implements FileService {
    
    private static final Logger LOG = LoggerFactory.getLogger(FileServiceImpl.class);
    
    private static final String DEFAULT_STORAGE_DIRECTORY = System.getProperty("java.io.tmpdir");

    @Resource(name = "defaultFileServiceProvider")
    protected FileServiceProvider defaultFileServiceProvider;
    
    @Value("${file.service.temp.file.base.directory}")
    protected String tempFileSystemBaseDirectory;    
    
    @Value("${file.service.directory.depth}")
    protected int maxGeneratedDirectoryDepth = 2;
    
    @Value("${file.service.file.classpath.directory}")
    protected String fileServiceClasspathDirectory;

    @Override
    public FileWorkArea initializeWorkArea() {
        String baseDirectory = getBaseDirectory();
        String tempDirectory = getTempDirectory(baseDirectory);
        FileWorkArea fw = new FileWorkArea();
        fw.setFilePathLocation(tempDirectory);
        
        File tmpDir = new File(tempDirectory);
        if (!tmpDir.exists()) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("创建工作目录 " + tempDirectory);
            }
            if (!tmpDir.mkdirs()) {
                throw new FileServiceException("无法创建工作目录 " + tempDirectory);
            }
        }

        return fw;
    }

    @Override
    public void closeWorkArea(FileWorkArea fwArea) {
        File tempDirectory = new File(fwArea.getFilePathLocation());
        try {
            if (tempDirectory.exists()) {
                FileUtils.deleteDirectory(tempDirectory);
            }

            for (int i = 1; i < maxGeneratedDirectoryDepth; i++) {
                tempDirectory = tempDirectory.getParentFile();
                if (tempDirectory.list().length == 0 && tempDirectory.exists()) {
                    FileUtils.deleteDirectory(tempDirectory);
                }
            }
        } catch (IOException ioe) {
            throw new FileServiceException("无法删除工作目录 " + tempDirectory, ioe);
        }
    }

    @Override
    public boolean removeResource(String resourceName) {
        return selectFileServiceProvider().removeResource(resourceName);
    }
    
    @Override
    public File getResource(String name) {
        return selectFileServiceProvider().getResource(name);
    }

    @Override
    public File getLocalResource(String resourceName) {
        String baseDirectory = getBaseDirectory();
        String systemResourcePath = FilenameUtils.separatorsToSystem(resourceName);
        String filePath = FilenameUtils.normalize(baseDirectory + File.separator + systemResourcePath);
        return new File(filePath);
    }

    @Override
    public File getResource(String name, Long localTimeout) {
        File returnFile = getLocalResource(name);
        if (returnFile != null && returnFile.exists()) {
            if (localTimeout != null) {
                long lastModified = returnFile.lastModified();
                long now = System.currentTimeMillis();
                if ((now - lastModified) <= localTimeout.longValue()) {
                    return returnFile;
                }
            } else {
                return returnFile;
            }
        }

        return getResource(name);
    }

    @Override
    public boolean checkForResourceOnClassPath(String name) {
        ClassPathResource resource = lookupResourceOnClassPath(name);
        return (resource != null && resource.exists());
    }

    protected ClassPathResource lookupResourceOnClassPath(String name) {
        if (!StringUtils.isBlank(fileServiceClasspathDirectory)) {
            try {
                String resourceName = FilenameUtils.separatorsToUnix(FilenameUtils.normalize(fileServiceClasspathDirectory + '/' + name));
                ClassPathResource resource = new ClassPathResource(resourceName);
                if (resource.exists()) {
                    return resource;
                }
            } catch (Exception e) {
                LOG.error("无法从classpath中获取资源", e);
            }
        }
        return null;
    }

    @Override
    public InputStream getClasspathResource(String name) {
        try {
            ClassPathResource resource = lookupResourceOnClassPath(name);
            if (resource != null && resource.exists()) {
                InputStream assetFile = resource.getInputStream();
                BufferedInputStream bufferedStream = new BufferedInputStream(assetFile);
                bufferedStream.mark(0);
                return bufferedStream;
            } else {
                return null;
            }
        } catch (Exception e) {
            LOG.error("无法从classpath中获取资源", e);
        }
        return null;
    }

    @Override
    public String addOrUpdateResourceForPath(FileWorkArea workArea, File file, boolean removeFilesFromWorkArea) {
        List<File> files = new ArrayList<File>();
        files.add(file);
        return addOrUpdateResourcesForPaths(workArea, files, removeFilesFromWorkArea).get(0);
    }

    @Override
    public List<String> addOrUpdateResourcesForPaths(FileWorkArea workArea, boolean removeFilesFromWorkArea) {
        File folder = new File(workArea.getFilePathLocation());
        List<File> fileList = new ArrayList<File>();
        buildFileList(folder, fileList);
        return addOrUpdateResourcesForPaths(workArea, fileList, removeFilesFromWorkArea);
    }
    
    @Override
    public List<String> addOrUpdateResourcesForPaths(FileWorkArea workArea, List<File> files, boolean removeFilesFromWorkArea) {
        checkFiles(workArea, files);
        return selectFileServiceProvider().addOrUpdateResourcesForPaths(workArea, files, removeFilesFromWorkArea);
    }

    protected FileServiceProvider selectFileServiceProvider() {
        return defaultFileServiceProvider;
    }

    protected void checkFiles(FileWorkArea workArea, List<File> fileList) {
        for (File file : fileList) {
            String fileName = file.getAbsolutePath();
            if (!fileName.startsWith(workArea.getFilePathLocation())) {
                throw new FileServiceException("试图对work area之外的文件进行操作. "
                        + fileName + ".  Work area = " + workArea.getFilePathLocation());
            }
            if (!file.exists()) {
                throw new FileServiceException("试图对一个不存在的文件进行操作. 文件名 " + fileName);
            }
        }
    }

    protected String getBaseDirectory() {
        String path = "";
        if (StringUtils.isBlank(tempFileSystemBaseDirectory)) {
            path = DEFAULT_STORAGE_DIRECTORY;
        } else {
            path = tempFileSystemBaseDirectory;
        }

        return path;
    }

    protected String getTempDirectory(String baseDirectory) {
        Assert.notNull(baseDirectory);
        Random random = new Random();
        for (int i = 1; i < maxGeneratedDirectoryDepth; i++) {
            if (i == 4) {
                LOG.warn("属性 file.max.generated.directory.depth 可能设置的太高了, 当前是 " + maxGeneratedDirectoryDepth);
                break;
            }
            int num = random.nextInt(256);
            baseDirectory = FilenameUtils.concat(baseDirectory, Integer.toHexString(num));
        }
        return baseDirectory;
    }

    protected void buildFileList(File file, Collection<File> fileList) {
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    if (child.isDirectory()) {
                        buildFileList(child, fileList);
                    } else {
                        fileList.add(child);
                    }
                }
            }
        } else {
            fileList.add(file);
        }
    }

    public String getTempFileSystemBaseDirectory() {
        return tempFileSystemBaseDirectory;
    }
    
    public void setTempFileSystemBaseDirectory(String tempFileSystemBaseDirectory) {
        this.tempFileSystemBaseDirectory = tempFileSystemBaseDirectory;
    }

    public int getMaxGeneratedDirectoryDepth() {
        return maxGeneratedDirectoryDepth;
    }

    public void setMaxGeneratedDirectoryDepth(int maxGeneratedDirectoryDepth) {
        this.maxGeneratedDirectoryDepth = maxGeneratedDirectoryDepth;
    }

    public FileServiceProvider getDefaultFileServiceProvider() {
        return defaultFileServiceProvider;
    }

    public void setDefaultFileServiceProvider(FileServiceProvider defaultFileServiceProvider) {
        this.defaultFileServiceProvider = defaultFileServiceProvider;
    }

}
