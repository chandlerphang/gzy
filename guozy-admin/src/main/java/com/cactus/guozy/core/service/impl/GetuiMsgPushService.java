package com.cactus.guozy.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cactus.guozy.common.utils.LazyLoader;
import com.cactus.guozy.common.utils.LazyLoader.Loader;
import com.cactus.guozy.core.service.MsgPushService;
import com.cactus.guozy.core.service.exception.MessagePushException;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.AppMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.AbstractTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("getuiPushService")
public class GetuiMsgPushService implements MsgPushService {

	// 用户端 appid, appkey, master secret
	@Value("${push.getui.user.appid}")
	protected String appid;
	@Value("${push.getui.user.appkey}")
	protected String apiKey;
	@Value("${push.getui.user.master_secret}")
	protected String secretKey;
	
	// 导购员端 appid, appkey, master secret
	@Value("${push.getui.saler.appid}")
	protected String salerAppid;
	@Value("${push.getui.saler.appkey}")
	protected String salerAppkey;
	@Value("${push.getui.saler.master_secret}")
	protected String salerSecretKey;

	protected LazyLoader<IGtPush, Object> userClient;
	protected LazyLoader<IGtPush, Object> salerClient;
	
	@PostConstruct
	public void init() {
		userClient = LazyLoader.getDefault(new Loader<IGtPush, Object>(){

			@Override
			public IGtPush load(Object context) {
				return new IGtPush(apiKey, secretKey);
			}
			
		});
		
		salerClient = LazyLoader.getDefault(new Loader<IGtPush, Object>(){

			@Override
			public IGtPush load(Object context) {
				return new IGtPush(salerAppkey, salerSecretKey);
			}
			
		});
	}
	
	protected AbstractTemplate makeTemplate(String msg, boolean isTransmission) {
		if(isTransmission) {
			TransmissionTemplate template = new TransmissionTemplate();
			// 设置APPID与APPKEY
			template.setAppId(salerAppid);
			template.setAppkey(salerAppkey);
			template.setTransmissionType(2);
			template.setTransmissionContent(msg);
			
			return template;
		} else {
			TransmissionTemplate template = new TransmissionTemplate();
			// 设置APPID与APPKEY
			template.setAppId(salerAppid);
			template.setAppkey(salerAppkey);
			template.setTransmissionType(1);
			template.setTransmissionContent("");
			APNPayload payload = new APNPayload();
		    //在已有数字基础上加1显示，设置为-1时，在已有数字上减1显示，设置为数字时，显示指定数字
		    payload.setContentAvailable(1);
		    payload.setSound("default.caf");
		    //简单模式APNPayload.SimpleMsg 
		    payload.setAlertMsg(new APNPayload.SimpleAlertMsg(msg));
			
			template.setAPNInfo(payload);
			return template;
		}
	}
	
	@Override
	public void pushMsgToSaler(String channelId, String msg) {
		pushMsgToSaler(channelId, msg, true);
	}
	
	@Override
	public void pushMsgToSaler(String channelId, String msg, boolean isTransmission) {
		if(log.isDebugEnabled()) {
			log.debug("准备推送消息到导购员 ( " + channelId + ") <==== " + msg);
		}
		
		AbstractTemplate template = makeTemplate(msg, isTransmission);

		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(30 * 1000);
		message.setData(template);
		message.setPushNetWorkType(0); // 可选，1为wifi，0为不限制网络环境
		
		Target target = new Target();
		target.setAppId(salerAppid);
		target.setClientId(channelId);
		IPushResult ret = null;
		try {
			ret = salerClient.getInstance().pushMessageToSingle(message, target);
		} catch (RequestException e) {
			if(log.isDebugEnabled()) {
				log.debug("消息推送失败，可能的原因 -> " + e.getMessage());
				log.debug("尝试重新发送");
			}
			
			ret = salerClient.getInstance().pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			if(log.isDebugEnabled()) {
				log.debug(ret.getResponse().toString());
			}
			assertSuccess(ret);
		} else {
			log.error("个推服务器响应异常");
			throw new MessagePushException("个推服务器响应异常");
		}
	}

	public void pushMsgToUser(String channelId, String msg) {
		if(log.isDebugEnabled()) {
			log.debug("准备推送消息到 " + channelId + " <- " + msg);
		}
		
		TransmissionTemplate template = new TransmissionTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appid);
		template.setAppkey(apiKey);
		template.setTransmissionType(2);
		template.setTransmissionContent(msg);

		SingleMessage message = new SingleMessage();
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(8*1000);
		message.setData(template);
		message.setPushNetWorkType(0); // 可选，1为wifi，0为不限制网络环境
		
		Target target = new Target();
		target.setAppId(appid);
		target.setClientId(channelId);
		IPushResult ret = null;
		try {
			ret = userClient.getInstance().pushMessageToSingle(message, target);
		} catch (RequestException e) {
			if(log.isDebugEnabled()) {
				log.debug("消息推送失败，可能的原因 -> " + e.getMessage());
				log.debug("尝试重新发送");
			}
			
			ret = userClient.getInstance().pushMessageToSingle(message, target, e.getRequestId());
		}
		if (ret != null) {
			if(log.isDebugEnabled()) {
				log.debug(ret.getResponse().toString());
			}
			assertSuccess(ret);
		} else {
			log.error("个推服务器响应异常");
			throw new MessagePushException("个推服务器响应异常");
		}
	}

	public void pushMsgToAllUser(String msg) {
		// 
		TransmissionTemplate template = new TransmissionTemplate();
		template.setAppId(appid);
		template.setAppkey(apiKey);
		template.setTransmissionType(2);
		template.setTransmissionContent(msg);
		
		AppMessage message = new AppMessage();
		List<String> appIdList = new ArrayList<String>();
        appIdList.add(appid);
        message.setAppIdList(appIdList);
		message.setData(template);
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		IPushResult ret = userClient.getInstance().pushMessageToApp(message);
		
		if (ret != null) {
			if(log.isDebugEnabled()) {
				log.debug(ret.getResponse().toString());
			}
			assertSuccess(ret);
		} else {
			log.error("个推服务器响应异常");
			throw new MessagePushException("个推服务器响应异常");
		}
	}
	
	public void assertSuccess(IPushResult result) {
		String status = (String)result.getResponse().get("result");
		switch(status) {
		case "ok":
		case "successed_online":
			; // do nothing
			break;
		default: 
			throw new MessagePushException("消息推送失败，第三方响应结果为 " + result.getResponse().toString());
		}
	}

}
