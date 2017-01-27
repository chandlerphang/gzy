package com.cactus.guozy.common.cms;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cactus.guozy.common.service.BaseService;

public interface AssetService extends BaseService<Asset> {

	Asset findAssetById(Long id);

	List<Asset> readAllAssets();

	Asset findAssetByUrl(String fullUrl);

	Asset createAssetFromFile(MultipartFile file, Map<String, String> properties);

	Asset createAsset(InputStream inputStream, String fileName, long fileSize, Map<String, String> properties);

	Asset addAsset(Asset staticAsset);

	Asset updateAsset(Asset staticAsset);

	void deleteAsset(Asset staticAsset);

}
