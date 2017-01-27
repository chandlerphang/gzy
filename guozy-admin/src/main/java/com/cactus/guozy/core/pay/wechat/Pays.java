package com.cactus.guozy.core.pay.wechat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;

import com.cactus.guozy.common.encrypt.MD5;
import com.cactus.guozy.core.dto.PayRequest;

public final class Pays extends Component {

    /**
     * 统一下单接口
     */
    private static final String PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    protected Pays(Wepay wepay) {
        super(wepay);
    }

    /**
     * app支付
     * @param request 支付请求对象
     * @return AppPayResponse对象，或抛WepayException
     */
    public Map<String, String> appPay(PayRequest request){
        checkPayParams(request);
        Map<String, String> respData = doAppPay(request);
        return buildAppPayResp(respData);
    }

    /**
     * APP支付
     * @param request 支付信
     * @return 支付结
     */
    private Map<String, String> doAppPay(PayRequest request){
        Map<String, String> payParams = buildPayParams(request);
        return doPay(payParams);
    }

    private Map<String, String> doPay(Map<String, String> payParams) {
        buildSignParams(payParams);
        return doPost(PAY_URL, payParams);
    }

    private Map<String, String> buildAppPayResp(Map<String, String> data) {
        String appId = wepay.getAppId();
        String partnerId= wepay.getMchId();
        String nonceStr = RandomStringUtils.randomAlphanumeric(16);
        String timeStamp = String.valueOf(new Date().getTime() / 1000);
        String prepayId = String.valueOf(data.get(WepayField.PREPAY_ID));

        String signing =
                WepayField.APP_ID + "=" + appId +
                "&"+ WepayField.NONCESTR +"=" + nonceStr +
                "&" + WepayField.PKG + "=Sign=WXPay" +
                "&" + WepayField.PARTNERID + "=" + partnerId +
                "&" + WepayField.PREPAYID + "=" + prepayId +
                "&" + WepayField.TIMESTAMP + "=" + timeStamp +
                "&" + WepayField.KEY + "=" + wepay.getAppKey();
        String signed = MD5.generate(signing, false).toUpperCase();
        
        Map<String, String> ret = new HashMap<>();
        put(ret, WepayField.APP_ID, appId);
        put(ret, WepayField.PARTNERID, partnerId);
        put(ret, WepayField.PREPAYID, prepayId);
        put(ret, WepayField.PKG, "Sign=WXPay");
        put(ret, WepayField.NONCE_STR, nonceStr);
        put(ret, WepayField.TIMESTAMP, timeStamp);
        put(ret, WepayField.SIGN, signed);

        return ret;
    }

    private void checkPayParams(PayRequest request) {
//        checkNotNull(request, "pay detail can't be null");
//        checkNotNullAndEmpty(request.getBody(), "body");
//        checkNotNullAndEmpty(request.getOutTradeNo(), "outTradeNo");
//        Integer totalFee = request.getTotalFee();
//        checkArgument(totalFee != null && totalFee > 0, "totalFee must > 0");
//        checkNotNullAndEmpty(request.getClientId(), "clientId");
//        checkNotNullAndEmpty(request.getNotifyUrl(), "notifyUrl");
//        checkNotNull(request.getFeeType(), "feeType can't be null");
//        checkNotNullAndEmpty(request.getTimeStart(), "timeStart");
    }

    /**
     * 构建公共支付参数
     * @param request 支付请求对象
     * @param tradeType 交易类型
     * @return 支付MAP参数
     */
    private Map<String, String> buildPayParams(PayRequest request) {
        Map<String, String> payParams = new TreeMap<>();

        // 配置参数
        buildConfigParams(payParams);

        // 业务必需参数
        put(payParams, WepayField.BODY, request.getBody());
        put(payParams, WepayField.OUT_TRADE_NO, request.getOutTradeNo());
        put(payParams, WepayField.TOTAL_FEE, request.getTotalFee() + "");
        put(payParams, WepayField.SPBILL_CREATE_IP, request.getClientId());
        put(payParams, WepayField.NOTIFY_URL, request.getNotifyUrl());
        put(payParams, WepayField.FEE_TYPE, request.getFeeType());
        put(payParams, WepayField.NONCE_STR, RandomStringUtils.randomAlphanumeric(16));
        put(payParams, WepayField.TIME_START, request.getTimeStart());
        put(payParams, WepayField.TRADE_TYPE, "APP");

        // 业务可选参数
        putIfNotEmpty(payParams, WepayField.DEVICE_INFO, request.getDeviceInfo());
        putIfNotEmpty(payParams, WepayField.ATTACH, request.getAttach());
        putIfNotEmpty(payParams, WepayField.DETAIL, request.getDetail());
        putIfNotEmpty(payParams, WepayField.GOODS_TAG, request.getGoodsTag());
        putIfNotEmpty(payParams, WepayField.TIME_EXPIRE, request.getTimeExpire());
        putIfNotEmpty(payParams, WepayField.LIMIT_PAY, request.getLimitPay());

        return payParams;
    }
}
