package com.cactus.guozy.api.endpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cactus.guozy.api.wrapper.ErrorMsgWrapper;
import com.cactus.guozy.common.cms.Asset;
import com.cactus.guozy.common.config.RuntimeEnvConfigService;
import com.cactus.guozy.core.dto.GenericWebResult;

@RestController
public class AppEndpoint extends BaseEndpoint {
	
	@RequestMapping(value={"/aboutus"}, method = RequestMethod.GET)
	public GenericWebResult getAboutUs(HttpServletRequest request) {
		return GenericWebResult.success(appSettingService.findAboutusUrl());
	}
	
	@RequestMapping(value={"/fruitcs"}, method = RequestMethod.GET)
	public GenericWebResult getAllFruitCS(HttpServletRequest request) {
		return GenericWebResult.success(appSettingService.findAllFruitCommonSense());
	}
	
	@RequestMapping(value = "/assets", method = RequestMethod.POST)
	public GenericWebResult uploadAssets(HttpServletRequest request, 
			@RequestParam("file") MultipartFile file) {
		
		Map<String, String> properties = new HashMap<>();
		properties.put("module", "common");
		properties.put("resourceId", RandomStringUtils.randomNumeric(6));
		
		Asset asset=assetService.createAssetFromFile(file, properties);
		if(asset == null) {
			return GenericWebResult.error("500").withData(ErrorMsgWrapper.error("unknownError").withMsg("服务器内部错误"));
		}
		
		try {
			ass.storeAssetFile(file, asset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return GenericWebResult.error("200").withData("/"+RuntimeEnvConfigService.resolveSystemProperty("asset.url.prefix","")+asset.getUrl());
	}
	
}
