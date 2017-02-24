package com.cactus.guozy.core.pay.alipay;

public class AlipayBuilder {
	private Alipay alipay;

	public static AlipayBuilder newBuilder(String appId, String secret) {
		AlipayBuilder builder = new AlipayBuilder();
		builder.alipay = new Alipay(appId, secret);
		return builder;
	}

	/**
	 * 仅需要APP支付时，可以不配置secret
	 * 
	 * @param appId
	 *            商户编号
	 * @return this builder
	 */
	public static AlipayBuilder newBuilder(String appId) {
		return newBuilder(appId, "");
	}

	/**
	 * Set email account
	 * 
	 * @param email
	 *            email account
	 * @return this
	 */
	public AlipayBuilder email(String email) {
		alipay.email = email;
		return this;
	}

	/**
	 * Set the charset of the merchant，ex. utf-8、gbk、gb2312，default is utf-8
	 * 
	 * @param charset
	 *            charset
	 * @return this
	 */
	public AlipayBuilder inputCharset(String charset) {
		alipay.charset = charset;
		return this;
	}

	/**
	 * 设置支付超时时间，默认1h
	 *
	 * @param time
	 *            一旦超时，该笔交易就会自动被关闭。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。该参数数值不接受小数点，如1.5h，可转换为90m。
	 * @return this
	 */
	public AlipayBuilder expired(String time) {
		alipay.expired = time;
		return this;
	}

	/**
	 * 设置APP RSA私钥
	 * 
	 * @param rsaPriKey
	 *            RSA私钥
	 * @return this
	 */
	public AlipayBuilder appPriKey(String rsaPriKey) {
		alipay.appPriKey = rsaPriKey;
		return this;
	}

	/**
	 * 设置APP RSA公钥
	 * 
	 * @param rsaPubKey
	 *            RSA公钥
	 * @return this
	 */
	public AlipayBuilder appPubKey(String rsaPubKey) {
		alipay.appPubKey = rsaPubKey;
		return this;
	}
	
	public AlipayBuilder aliPubKey(String aliPubKey) {
		alipay.aliPubKey = aliPubKey;
		return this;
	}
	
	public AlipayBuilder sellerId(String sellerId) {
		alipay.sellerId = sellerId;
		return this;
	}

	/**
	 * 设置支付超时时间
	 * 
	 * @param payExpired
	 *            超时时间
	 * @return this
	 */
	public AlipayBuilder payExpired(String payExpired) {
		alipay.expired = payExpired;
		return this;
	}

	public Alipay build() {
		return alipay.start();
	}
}
