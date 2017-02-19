package com.cactus.guozy.core.service;

public interface MsgPushService {

	void pushMsgToAllUser(String msg);

	void pushMsgToUser(String channelId, String msg);
	
	void pushMsgToSaler(String channelId, String msg);

}
