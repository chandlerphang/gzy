package com.cactus.guozy.core.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.stereotype.Service;

import com.cactus.guozy.core.service.GenericResponse;
import com.cactus.guozy.core.service.VerifyCodeService;
import com.cactus.guozy.core.service.exception.ServiceException;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Service("vcodeService")
public class VerifyCodeServiceImpl implements VerifyCodeService{
	
	@Resource(name="cacheManager")
	protected EhCacheCacheManager springCacheManager;

	@Override
	public GenericResponse sendAndSaveCodeByPhone(String phone, String moduleName) throws ServiceException {
		String vcode = sendCodeByPhone(phone, moduleName);
		return saveCode(phone, vcode, moduleName);
	}

	@Override
	public boolean verifyCode(Object key, String code, String moduleName) throws ServiceException {
		Cache vcodeCache = getCache(moduleName);
		Element vcodeEle = vcodeCache.get(key);
		if(vcodeEle != null && !vcodeEle.isExpired()) {
			if(vcodeEle.getObjectValue().equals(code)){
				return true;
			} else {
				// 命中50以上，可能被暴力破验证码。设置1秒后过期
				if(vcodeEle.getHitCount() > 50) {
					vcodeEle.setTimeToLive(1);
				}
				return false;
			}
		} else {
			return false;
		}
	}
	
	protected Cache getCache(String moduleName) {
		return springCacheManager.getCacheManager().getCache(moduleName);
	}

	@Override
	public String sendCodeByPhone(String phone, String moduleName) {
		String vcode = RandomStringUtils.randomNumeric(6);
		
		TaobaoClient client = new DefaultTaobaoClient("https://eco.taobao.com/router/rest", "23599964", "9728249051d77cced150b05f723737d2");
		AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
		req.setExtend("123456");
		req.setSmsType("normal");
		req.setSmsFreeSignName("果之源");
		req.setSmsParamString("{\"number\":\""+vcode+"\"}");
		//请填写需要接收的手机号码
		req.setRecNum(phone);
		//短信模板id
		if(VerifyCodeService.MODULE_REGISTER.equals(moduleName)){
			req.setSmsTemplateCode("SMS_41610122");
		} else if(VerifyCodeService.MODULE_NOPASSWD.equals(moduleName)){
			req.setSmsTemplateCode("SMS_41685028");
		} else if(VerifyCodeService.MODULE_RESETPWD.equals(moduleName)){
			req.setSmsTemplateCode("SMS_41685028");
		}
		
		try {
			AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		return vcode;
	}

	@Override
	public GenericResponse saveCode(Object key, String code, String moduleName) {
		Cache vcodeCache = getCache(moduleName);
		Element vcodeEle = vcodeCache.get(key);
		if(vcodeEle != null && !vcodeEle.isExpired()) {
			if(new Date().getTime() - vcodeEle.getCreationTime() <= 60 * 1000) {
				return GenericResponse.error().addErrorCode("010101");
			}
		}
		vcodeEle = new Element(key, code);
		vcodeCache.put(vcodeEle);
		return GenericResponse.success();
	}

}
