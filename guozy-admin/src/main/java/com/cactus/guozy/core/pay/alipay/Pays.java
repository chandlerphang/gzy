package com.cactus.guozy.core.pay.alipay;

import static com.cactus.guozy.common.utils.Preconditions.checkNotNullAndEmpty;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alipay.api.internal.util.AlipaySignature;
import com.cactus.guozy.common.json.Jsons;
import com.cactus.guozy.core.util.DateTimeUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Pays extends Component {

	Pays(Alipay alipay) {
		super(alipay);
	}

	/**
	 * APP支付
	 * 
	 * @param appPayDetail
	 *            支付字段信息
	 * @return APP支付字符串
	 */
	public String appPay(AppPayDetail appPayDetail) {
		checkNotNullAndEmpty(alipay.appPriKey, "app private key");
		Map<String, String> appPayParams = buildAppPayParams(appPayDetail);
		return buildRsaPayString(appPayParams);
	}

	private Map<String, String> buildAppPayParams(AppPayDetail payDetail) {

		Map<String, String> payParams = new HashMap<>();
		// 配置公共参数
		payParams.putAll(alipay.payConfig);
		putIfNotEmpty(payParams, AlipayField.TIMESTAMP, DateTimeUtil.dateTimeFrom(new Date()));
		putIfNotEmpty(payParams, AlipayField.NOTIFY_URL, payDetail.getNotifyUrl());

		// === 业务参数
		Map<String, String> reqParams = new HashMap<>();
		putIfNotEmpty(reqParams, AlipayField.BODY, payDetail.getBody());
		
		checkNotNullAndEmpty(payDetail.getOrderName(), "orderName");
		reqParams.put(AlipayField.SUBJECT.field(), payDetail.getOrderName());
		
		checkNotNullAndEmpty(payDetail.getOutTradeNo(), "outTradeNo");
		reqParams.put(AlipayField.OUT_TRADE_NO.field(), payDetail.getOutTradeNo());
		
		checkNotNullAndEmpty(payDetail.getTotalFee(), "totalFee");
		reqParams.put(AlipayField.TOTAL_AMOUNT.field(), payDetail.getTotalFee());
		reqParams.put(AlipayField.PRODUCT_CODE.field(), "QUICK_MSECURITY_PAY");
		// ===

		putIfNotEmpty(payParams, AlipayField.BIZ_CONTENT, Jsons.DEFAULT.toJson(reqParams));

		return payParams;
	}

	/**
	 * 构建RSA签名参数
	 * 
	 * @param payParams
	 *            支付参数
	 * @return RSA签名后的支付字符串
	 */
	private String buildRsaPayString(Map<String, String> payParams) {
		payParams.put(AlipayField.SIGN_TYPE.field(), "RSA2");
		String signing = AlipaySignature.getSignContent(payParams);
		if(log.isDebugEnabled()) {
			log.debug("支付宝签名前参数串: " + signing);
		}
		String signed = "";
		try {
			signed = AlipaySignature.rsa256Sign(signing, alipay.appPriKey, alipay.charset);
			payParams.put("sign", signed);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(log.isDebugEnabled()) {
			log.debug("签名: " + signed);
		}
		
        List<String> keys = new ArrayList<>(payParams.keySet());
        StringBuilder payString = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = encode(payParams.get(key), alipay.charset);
            if (i == keys.size() - 1) {
                //拼接时，不包括最后一个&字符
                payString.append(key).append("=").append(value);
            } else {
                payString.append(key).append("=").append(value).append("&");
            }
        }

        return payString.toString();
	}
	
	private String encode(String str, String charset) {
		try {
			return URLEncoder.encode(str, charset);
		} catch (UnsupportedEncodingException e) {
			log.error("编码错误");
			return str;
		}
	}

}
