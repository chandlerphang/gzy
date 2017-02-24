package com.cactus.guozy.core.pay.alipay;

import java.util.HashMap;
import java.util.Map;

import com.cactus.guozy.common.annotations.Optional;

/**
 * 支付宝支付
 */
public final class Alipay {

    /**
     * 支付宝网关
     */
    static final String GATEWAY = "https://openapi.alipay.com/gateway.do";

    /**
     * 支付宝分配给开发者的应用ID
     */
    String appId;
    
    /**
     * 接口名称
     */
    String method = "alipay.trade.app.pay";
    
    /**
     * 数据格式
     */
    @Optional
    String format = "JSON";
    
    /**
     * 商户网站使用的编码格式，如utf-8、gbk、gb2312等
     */
    String charset = "UTF-8";
    
    String version = "1.0";
    
    /**
     * 商户密钥
     */
    String secret;

    /**
     * 商户邮箱帐号
     */
    String email;

    /**
     * 支付超时时间
     */
    String expired = "1h";

    /**
     * APP RSA私钥
     */
    String appPriKey;

    /**
     * APP RSA公钥
     */
    String appPubKey;
    
    /**
     * Ali RSA公钥
     */
    String aliPubKey;
    
    String sellerId;

    /**
     * 支付配置参数，不需每次请求都生成
     */
    Map<String, String> payConfig;

    Map<String, Component> components = new HashMap<>();

    Alipay(String appid, String secret){
        this.appId = appid;
        this.secret = secret;
    }

    Alipay start() {
        initConfig();
        initComponents();
        return this;
    }

    private void initConfig() {
        payConfig = new HashMap<>();
        payConfig.put(AlipayField.APP_ID.field(), appId);
        payConfig.put(AlipayField.METHOD.field(), method);
        payConfig.put(AlipayField.FORMAT.field(), format);
        payConfig.put(AlipayField.CHARSET.field(), charset);
        payConfig.put(AlipayField.VERSION.field(), version);
    }

    private void initComponents() {
        components.put(Pays.class.getSimpleName(), new Pays(this));
        components.put(Verifies.class.getSimpleName(), new Verifies(this));
    }

    public Pays pay(){
        return (Pays)components.get(Pays.class.getSimpleName());
    }

    public Verifies verify(){
        return (Verifies)components.get(Verifies.class.getSimpleName());
    }
    
    public String getSellerId() {
    	return sellerId;
    }
    
    public String getAppId() {
    	return appId;
    }

    @Override
    public String toString() {
        return "Alipay{" +
                "appId='" + appId + '\'' +
                ", secret='" + secret + '\'' +
                ", charset='" + charset + '\'' +
                ", expired='" + expired + '\'' +
                ", appPriKey='" + appPriKey + '\'' +
                ", appPubKey='" + appPubKey + '\'' +
                '}';
    }
}
