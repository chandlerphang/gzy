package com.cactus.guozy.common.cms.dao;

import java.util.List;

import com.cactus.guozy.common.BaseDao;
import com.cactus.guozy.common.cms.Asset;

public interface AssetDao extends BaseDao<Asset> {

	Asset readAssetById(Long id);

	List<Asset> readAllAssets();

	int updateAsset(Asset asset);
	
	int insertAsset(Asset asset);

	Asset readAssetByUrl(String url);

}
