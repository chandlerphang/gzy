package com.cactus.guozy.core.pay.wechat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;

@org.springframework.stereotype.Component
public class WepaySupport {

    @Value("${appId}")
    private String appId;

    @Value("${appKey}")
    private String appKey;

    @Value("${mchId}")
    private String mchId;

    @Value("${payNotifyUrl}")
    private String payNotifyUrl;

    private Wepay wepay;

    @PostConstruct
    public void initWepay() {
        try(InputStream in = this.getClass().getClassLoader().getResourceAsStream("cert.p12")) {
            // 加载证书文件
        	ByteArrayOutputStream baos = new ByteArrayOutputStream();
        	IOUtils.copy(in, baos);
            byte[] certs = baos.toByteArray();
            wepay = WepayBuilder.newBuilder(appId, appKey, mchId)
                    .certPasswd(mchId)
                    .certs(certs)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 校验签名
     * @param params 参数(包含sign)
     * @return 校验成功返回true，反之false
     */
    public Boolean verifySign(Map<String, ?> params){
        return wepay.notifies().verifySign(params);
    }

    /**
     * 通知成功
     */
    public String notifyOk(){
        return wepay.notifies().ok();
    }

    /**
     * 通知不成功
     * @param errMsg 错误消息
     */
    public String notifyNotOk(String errMsg){
        return wepay.notifies().notOk(errMsg);
    }
}
