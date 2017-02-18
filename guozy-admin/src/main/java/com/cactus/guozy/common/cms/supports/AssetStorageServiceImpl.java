package com.cactus.guozy.common.cms.supports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cactus.guozy.common.cms.Asset;
import com.cactus.guozy.common.cms.AssetNotFoundException;
import com.cactus.guozy.common.cms.AssetService;
import com.cactus.guozy.common.cms.AssetStorageService;
import com.cactus.guozy.common.file.FileService;
import com.cactus.guozy.common.file.FileWorkArea;
import com.cactus.guozy.core.util.TransactionUtils;

@Service("assetStorageService")
public class AssetStorageServiceImpl implements AssetStorageService {
	
	private static final Logger LOG = LoggerFactory.getLogger(AssetStorageServiceImpl.class);

    @Value("${asset.storage.service.max.uploadable.file.size}")
    protected long maxUploadableFileSize;

    @Value("${asset.storage.service.file.buffer.size}")
    protected int fileBufferSize = 8096;

    @Resource(name="assetService")
    protected AssetService staticAssetService;

    @Resource(name = "fileService")
    protected FileService fileService;

    protected Asset findStaticAsset(String fullUrl) {
        Asset staticAsset = staticAssetService.findAssetByUrl(fullUrl);
        return staticAsset;
    }

    protected File lookupAssetAndCreateLocalFile(Asset staticAsset, File localFile) throws IOException {
        File returnFile = fileService.getResource(staticAsset.getUrl());
        if (!returnFile.getAbsolutePath().equals(localFile.getAbsolutePath())) {
            createLocalFileFromInputStream(new FileInputStream(returnFile), localFile);
        }
        return fileService.getResource(staticAsset.getUrl());            
    }   

    protected void createLocalFileFromClassPathResource(Asset staticAsset, File baseLocalFile) throws IOException {
        InputStream is = fileService.getClasspathResource(staticAsset.getUrl());
        createLocalFileFromInputStream(is, baseLocalFile);
    }
    
    protected void createLocalFileFromInputStream(InputStream is, File baseLocalFile) throws IOException {
        FileOutputStream tos = null;
        FileWorkArea workArea = null;
        try {
            if (!baseLocalFile.getParentFile().exists()) {
                boolean directoriesCreated = false;
                if (!baseLocalFile.getParentFile().exists()) {
                    directoriesCreated = baseLocalFile.getParentFile().mkdirs();
                    if (!directoriesCreated) {
                        // There is a chance that another VM created the directories.   If not, we may not have 
                        // proper permissions and this is an error we need to report.
                        if (!baseLocalFile.getParentFile().exists()) {
                            throw new RuntimeException("Unable to create middle directories for file: " +
                                    baseLocalFile.getAbsolutePath());
                        }
                    }
                }
            }
            
            workArea = fileService.initializeWorkArea();
            File tmpFile = new File(FilenameUtils.concat(workArea.getFilePathLocation(), baseLocalFile.getName()));
            
            tos = new FileOutputStream(tmpFile);

            IOUtils.copy(is, tos);
            
            // close the input/output streams before trying to move files around
            is.close();
            tos.close();

            // Adding protection against this file already existing / being written by another thread.
            // Adding locks would be useless here since another VM could be executing the code. 
            if (!baseLocalFile.exists()) {
                try {
                    FileUtils.moveFile(tmpFile, baseLocalFile);
                } catch (FileExistsException e) {
                    // No problem
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("File exists error moving file " + tmpFile.getAbsolutePath(), e);
                    }
                }
            }
        } finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(tos);
            
            if (workArea != null) {
                fileService.closeWorkArea(workArea);
            }
        }
    }    

    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    @Override
    public Map<String, String> getCacheFileModel(String fullUrl) throws Exception {
        Asset staticAsset = findStaticAsset(fullUrl);
        if (staticAsset == null) {
            throw new AssetNotFoundException("Unable to find an asset for the url (" + fullUrl + ")");
        }
        String mimeType = staticAsset.getMimeType();
        String cachedFileName = constructCacheFileName(staticAsset);
        
        File cacheFile = fileService.getLocalResource(cachedFileName);
        if (cacheFile.exists()) {
            return buildModel(cacheFile.getAbsolutePath(), mimeType);
        }
        
        cacheFile = lookupAssetAndCreateLocalFile(staticAsset, cacheFile);
        return buildModel(cacheFile.getAbsolutePath(), mimeType);
    }

    protected Map<String, String> buildModel(String returnFilePath, String mimeType) {
        Map<String, String> model = new HashMap<String, String>(2);
        model.put("cacheFilePath", returnFilePath);
        model.put("mimeType", mimeType);

        return model;
    }

    protected String constructCacheFileName(Asset staticAsset) {
        String fileName = staticAsset.getUrl();

        StringBuilder sb = new StringBuilder(200);
        sb.append(fileName.substring(0, fileName.lastIndexOf('.')));
        sb.append("---");

        StringBuilder sb2 = new StringBuilder(200);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        if (staticAsset.getDateUpdated() != null) {
            sb2.append(format.format(staticAsset.getDateUpdated()));
        } else if(staticAsset.getDateCreated() != null) {
        	sb2.append(format.format(staticAsset.getDateCreated()));
        }
        
        String digest;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(sb2.toString().getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            digest = number.toString(16);
        } catch(NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        sb.append(pad(digest, 32, '0'));
        sb.append(fileName.substring(fileName.lastIndexOf('.')));

        return sb.toString();
    }

    protected String pad(String s, int length, char pad) {
        StringBuilder buffer = new StringBuilder(s);
        while (buffer.length() < length) {
            buffer.insert(0, pad);
        }
        return buffer.toString();
    }

    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    @Override
    public void storeAssetFile(MultipartFile file, Asset staticAsset) throws IOException {
        storeAssetFile(file.getInputStream(), staticAsset);
    }
    
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    @Override
    public void storeAssetFile(InputStream fileInputStream, Asset staticAsset) throws IOException {
    	 FileWorkArea tempWorkArea = fileService.initializeWorkArea();
         String destFileName = FilenameUtils.normalize(tempWorkArea.getFilePathLocation() + File.separator + FilenameUtils.separatorsToSystem(staticAsset.getUrl()));

         InputStream input = fileInputStream;
         byte[] buffer = new byte[fileBufferSize];

         File destFile = new File(destFileName);
         if (!destFile.getParentFile().exists()) {
             if (!destFile.getParentFile().mkdirs()) {
                 if (!destFile.getParentFile().exists()) {
                     throw new RuntimeException("Unable to create parent directories for file: " + destFileName);
                 }
             }
         }

         OutputStream output = new FileOutputStream(destFile);
         boolean deleteFile = false;
         try {
             int bytesRead;
             int totalBytesRead = 0;
             while ((bytesRead = input.read(buffer)) != -1) {
                 totalBytesRead += bytesRead;
                 if (totalBytesRead > maxUploadableFileSize) {
                     deleteFile = true;
                     throw new IOException("Maximum Upload File Size Exceeded");
                 }
                 output.write(buffer, 0, bytesRead);
             }
             
             // close the output file stream prior to moving files around
             
             output.close();
             fileService.addOrUpdateResourceForPath(tempWorkArea, destFile, deleteFile);
         } finally {
             IOUtils.closeQuietly(output);
             fileService.closeWorkArea(tempWorkArea);
         }
    }

}

