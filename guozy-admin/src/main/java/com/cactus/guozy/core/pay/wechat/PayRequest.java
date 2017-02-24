package com.cactus.guozy.core.pay.wechat;

import java.io.Serializable;

import com.cactus.guozy.common.annotations.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PayRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
     * 设备号
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#DEVICE_INFO}
     */
    @Optional
    private String deviceInfo = "WEB";
    
	/**
     * 商品描述
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#BODY}
     */
    private String body;
    
    /**
     * 商品详情
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#DETAIL}
     */
    @Optional
    private String detail;
    
    /**
     * 附加信息
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#ATTACH}
     */
    @Optional
    private String attach;

    /**
     * 业务系统唯一订单号
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#OUT_TRADE_NO}
     */
    private String outTradeNo;
    
    /**
     * 货币类型
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#FEE_TYPE}
     */
    @Optional
    private String feeType = "CNY";

    /**
     * 总金额(分)
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#TOTAL_FEE}
     */
    private Integer totalFee;
    
    /**
     * 客户端IP
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#SPBILL_CREATE_IP}
     */
    private String clientId;

    /**
     * 交易开始时间
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#TIME_START}
     */
    @Optional
    private String timeStart;

    /**
     * 交易结束时间
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#TIME_EXPIRE}
     */
    @Optional
    private String timeExpire;
    
    /**
     * 商品标记
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#GOODS_TAG}
     */
    @Optional
    private String goodsTag;
    
    /**
     * 通知URL
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#NOTIFY_URL}
     */
    private String notifyUrl;

    /**
     * 指定支付方式
     * {@link com.cactus.guozy.core.pay.wechat.WepayField#LIMIT_PAY}
     */
    @Optional
    private String limitPay;

    @Override
    public String toString() {
        return "PayDetail{" +
                "body='" + body + '\'' +
                ", outTradeNo='" + outTradeNo + '\'' +
                ", totalFee=" + totalFee +
                ", clientId='" + clientId + '\'' +
                ", notifyUrl='" + notifyUrl + '\'' +
                ", deviceInfo='" + deviceInfo + '\'' +
                ", attach='" + attach + '\'' +
                ", detail='" + detail + '\'' +
                ", feeType=" + feeType +
                ", timeStart='" + timeStart + '\'' +
                ", timeExpire='" + timeExpire + '\'' +
                ", goodsTag='" + goodsTag + '\'' +
                ", limitPay='" + limitPay + '\'' +
                '}';
    }
}
