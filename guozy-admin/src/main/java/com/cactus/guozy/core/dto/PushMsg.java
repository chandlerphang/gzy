package com.cactus.guozy.core.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PushMsg {
	
	// 0:导购员状态变化 1:用户向导购员请求通话
	// 2:导购员接受通话 3:导购员拒绝通话 4:订单推送
	// 5:通话结束
	private int msgType;

	private Long subjectId;
	
	private Object extra;
}
