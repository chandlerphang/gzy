package com.baidu.yun.push.sample;

import java.io.IOException;
import java.util.Map;

import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;

public class Test {
	public static void main(String[] args) throws PushClientException, PushServerException, IOException {

        /** 
         * 单条推送
         */
         Map<String, Object> resultMap = PushMessageNew.pushMsgToSingleDevice("4066860599761372682", 3600, 1, "代码测试", "测试ing", 2, "www.baidu.com", 4, "","");

        /** * 批量推送 String[] channelIds = {"4066860599761372682","3814863451180523498"} ; Map<String, Object> resultMap = PushMessageNew.androidPushBatchUniMsg("测试*****", "测试1205", channelIds , 3); */

        /** * 广播推送 Map<String, Object> resultMap = PushMessageNew.pushMsgToAll("涌泉金服", "注册有礼", System.currentTimeMillis() / 1000 + 120, 3600, 1, 1, "https://www.baidu.com/", 3, ""); System.out.println(resultMap); */

        /** * 消息状态的查看 */
        //String[] strs={"8361003517954776467"};
        //List<?> list = PushMessageNew.QueryMsgStatus(strs, 3);
        System.out.println(resultMap);
    }
}
