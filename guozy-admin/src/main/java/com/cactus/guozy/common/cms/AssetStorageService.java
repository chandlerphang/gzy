package com.cactus.guozy.common.cms;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

public interface AssetStorageService {

    Map<String, String> getCacheFileModel(String fullUrl) throws Exception;

    void storeAssetFile(MultipartFile file, Asset staticAsset) throws IOException;

    void storeAssetFile(InputStream fileis, Asset staticAsset) throws IOException;

}
