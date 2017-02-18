package com.cactus.guozy.common.cms.supports;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.common.cms.Asset;
import com.cactus.guozy.common.cms.AssetService;
import com.cactus.guozy.common.cms.dao.AssetDao;
import com.cactus.guozy.common.service.BaseServiceImpl;
import com.cactus.guozy.core.util.TransactionUtils;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import eu.medsea.mimeutil.detector.ExtensionMimeDetector;
import eu.medsea.mimeutil.detector.MagicMimeMimeDetector;

@Service("assetService")
public class AssetServiceImpl extends BaseServiceImpl<Asset> implements AssetService {

    private static final Logger LOG = LoggerFactory.getLogger(AssetServiceImpl.class);
    
    @Autowired
    protected AssetDao assetDao;

    @Override
    public Asset findAssetById(Long id) {
        return assetDao.readAssetById(id);
    }
    
    @Override
    public List<Asset> readAllAssets() {
        return assetDao.readAllAssets();
    }

    static {
        MimeUtil.registerMimeDetector(ExtensionMimeDetector.class.getName());
        MimeUtil.registerMimeDetector(MagicMimeMimeDetector.class.getName());
    }

    protected String getFileExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > 0) {
            return fileName.substring(pos + 1, fileName.length()).toLowerCase();
        } else {
            LOG.warn("No extension provided for asset : " + fileName);
            return null;
        }
    }

    protected String buildAssetURL(Map<String, String> assetProperties, String originalFilename) {
        StringBuilder path = new StringBuilder("/");
        
        String module = assetProperties.get("module");
        String resourceId = assetProperties.get("resourceId");
        
        if (module != null && !"null".equals(module)) {
            path = path.append(module).append("/");
        } else {
            LOG.warn("The given module to build the asset URL was null for file " + originalFilename + " and resourceId " + resourceId + ", investigate probably cause");
        }

        if (resourceId != null && !"null".equals(resourceId)) {
            path = path.append(resourceId).append("/");
        } else {
            LOG.warn("The given module to build the asset URL was null for file " + originalFilename + " and resourceId " + module + ", investigate probably cause");
        }

        return path.append(RandomStringUtils.randomAlphanumeric(16))
        		.append(".")
        		.append(getFileExtension(originalFilename)).toString();
    }

    @Override
    public Asset createAssetFromFile(MultipartFile file, Map<String, String> properties) {
        try {
            return createAsset(file.getInputStream(), file.getOriginalFilename(), file.getSize(), properties);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public Asset createAsset(InputStream inputStream, String fileName, long fileSize, Map<String, String> properties) {
        if (properties == null) {
            properties = new HashMap<String, String>();
        }

        String fullUrl = buildAssetURL(properties, fileName);
        Asset newAsset = assetDao.readAssetByUrl(fullUrl);
        int count = 0;
        while (newAsset != null) {
            count++;
            newAsset = assetDao.readAssetByUrl(getCountUrl(fullUrl, count));
            if (newAsset == null) {
                newAsset = assetDao.readAssetByUrl(getCountUrl(fullUrl, count));
            }
        }

        if (count > 0) {
            fullUrl = getCountUrl(fullUrl, count);
        }
        newAsset = new Asset();

        newAsset.setName(fileName);
        newAsset.setMimeType(getMimeType(inputStream, fileName));
        newAsset.setFileExtension(getFileExtension(fileName));
        newAsset.setFileSize(fileSize);
        newAsset.setUrl(fullUrl);

        int ret = assetDao.insertAsset(newAsset);
        if(ret != 1) {
        	if(LOG.isErrorEnabled()) {
        		LOG.error("asset cannot be persisted. [filename: " + fileName + "; url: " + fullUrl + "]");
        	}
        	return null;
        }
        
        return newAsset;
    }
    
    protected String getCountUrl(String fullUrl, int count) {
        String countUrl = fullUrl + '-' + count;
        int dotIndex = fullUrl.lastIndexOf('.');
        if (dotIndex != -1) {
            countUrl = fullUrl.substring(0, dotIndex) + '-' + count + '.' + fullUrl.substring(dotIndex + 1);
        }
        
        return countUrl;
    }

    protected String getMimeType(InputStream inputStream, String fileName) {
        @SuppressWarnings("rawtypes")
		Collection mimeTypes = MimeUtil.getMimeTypes(fileName);
        if (!mimeTypes.isEmpty()) {
            MimeType mimeType = (MimeType) mimeTypes.iterator().next();
            return mimeType.toString();
        } else {
            mimeTypes = MimeUtil.getMimeTypes(inputStream);
            if (!mimeTypes.isEmpty()) {
                MimeType mimeType = (MimeType) mimeTypes.iterator().next();
                return mimeType.toString();
            }
            return "";
        }
    }

    @Override
    public Asset findAssetByUrl(String fullUrl) {
        try {
            fullUrl = URLDecoder.decode(fullUrl, "UTF-8");
            fullUrl = fullUrl.replaceAll("(?i);jsessionid.*?=.*?(?=\\?|$)", "");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported encoding to decode fullUrl", e);
        }
        return assetDao.readAssetByUrl(fullUrl);
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public Asset addAsset(Asset staticAsset) {
    	int ret = assetDao.insertAsset(staticAsset);
    	if(ret != 1) {
    		LOG.error("asset cannot be persisted");
    		return null;
    	}
    	return staticAsset;
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public Asset updateAsset(Asset staticAsset) {
         assetDao.updateAsset(staticAsset);
         return staticAsset;
    }

    @Override
    @Transactional(TransactionUtils.DEFAULT_TRANSACTION_MANAGER)
    public void deleteAsset(Asset staticAsset) {
        assetDao.delete(staticAsset);
    }

	@Override
	public BaseDao<Asset> getBaseDao() {
		return assetDao;
	}
  
}
