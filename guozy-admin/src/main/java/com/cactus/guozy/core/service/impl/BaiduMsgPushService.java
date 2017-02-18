package com.cactus.guozy.core.service.impl;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;
import com.cactus.guozy.common.utils.LazyLoader;
import com.cactus.guozy.common.utils.LazyLoader.Loader;
import com.cactus.guozy.core.service.MsgPushService;
import com.cactus.guozy.core.service.exception.MessagePushException;
import com.cactus.guozy.core.util.ExceptionHelper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("baiduPushService")
public class BaiduMsgPushService implements MsgPushService {
	
	@Value("${push.request.baidu.appkey}")
	protected String apiKey;
	
	@Value("${push.request.baidu.secret}")
	protected String secretKey;
	
	protected LazyLoader<BaiduPushClient, Object> baiduClient;
	
	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		baiduClient = LazyLoader.getDefault(new Loader<BaiduPushClient, Object>(){

			@Override
			public BaiduPushClient load(Object context) {
				PushKeyPair pair = new PushKeyPair(apiKey, secretKey);
				BaiduPushClient client = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);
				client.setChannelLogHandler(new YunLogHandler() {
		            @Override
		            public void onHandle(YunLogEvent event) {
		            	log.info(event.getMessage());
		            }
		        });
				
				return client;
			}
			
		});
	}
	
	@Override
	public void pushMsgToUser(String channelId, String msg) {
        BaiduPushClient pushClient = baiduClient.getInstance();
        try {
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest().
                addChannelId(channelId).
                addMsgExpires(new Integer(20)).
                addMessageType(0).addMessage(msg);
        
            log.info("开始推送消息到指定用户...");
            PushMsgToSingleDeviceResponse response = pushClient.pushMsgToSingleDevice(request);
            log.info("msgId: " + response.getMsgId() + ",sendTime: " + response.getSendTime() );
        } catch (PushClientException e) {
        	log.error(e.getMessage());
        	throw ExceptionHelper.refineException(MessagePushException.class, e);
        } catch (PushServerException e) {
        	log.error(String.format(
                    "requestId: %d, errorCode: %d, errorMsg: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        	throw ExceptionHelper.refineException(MessagePushException.class, e);
        }
	}
	
	@Override
	public void pushMsgToAllUser(String msg) {
		BaiduPushClient pushClient = baiduClient.getInstance();

        try {
            PushMsgToAllRequest request = new PushMsgToAllRequest()
                    .addMsgExpires(new Integer(120))
                    .addMessageType(0)
                    .addMessage(msg)
                    // 设置定时推送时间，必需超过当前时间一分钟，单位秒.实例61秒后推送
                    .addSendTime(System.currentTimeMillis() / 1000 + 61);
            
            log.info("开始推送消息到所有用户...");
            PushMsgToAllResponse response = pushClient.pushMsgToAll(request);
            log.info("msgId: " + response.getMsgId() + ",sendTime: "    
                    + response.getSendTime() + ",timerId: "
                    + response.getTimerId());
        } catch (PushClientException e) {
        	log.error(e.getMessage());
        	throw ExceptionHelper.refineException(e);
        } catch (PushServerException e) {
        	log.error(String.format(
                    "requestId: %d, errorCode: %d, errorMsg: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        	throw ExceptionHelper.refineException(MessagePushException.class, e);
        }
	}

	@Override
	public void pushMsgToSaler(String channelId, String msg) {
	}
}
