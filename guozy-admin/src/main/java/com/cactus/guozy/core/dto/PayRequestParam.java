package com.cactus.guozy.core.dto;

import java.math.BigDecimal;

public class PayRequestParam {

	/**
	 * 通知回调跳转页面
	 */
	private String retUrl;

	/**
	 * 订单ID
	 */
	private Long orderId;

	/**
	 * 订单编号
	 */
	private String orderNum;

	/**
	 * 支付编号
	 */
	private String payCode;

	/**
	 * 支付平台
	 */
	private String platformType;

	/**
	 * 支付入口
	 */
	private String browseType;

	/**
	 * 支付金额
	 */
	private BigDecimal toPay;

	/**
	 * 商家
	 */
	private String sellerName;

	/**
	 * 商家编号
	 */
	private String sellerCode;

	/**
	 * 创建时间
	 */
	private Long createTime;

	/**
	 * 有效时间
	 */
	private Long invalidTime;

	/**
	 * 请求业务来源
	 */
	private String requestBiz;

	public String getRetUrl() {
		return retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderID) {
		this.orderId = orderID;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderCode) {
		this.orderNum = orderCode;
	}

	public String getPlatformType() {
		return platformType;
	}

	public void setPlatformType(String onLineStyle) {
		this.platformType = onLineStyle;
	}

	public String getBrowseType() {
		return browseType;
	}

	public void setBrowseType(String browseType) {
		this.browseType = browseType;
	}

	public BigDecimal getToPay() {
		return toPay;
	}

	public void setToPay(BigDecimal toPay) {
		this.toPay = toPay;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public String getSellerCode() {
		return sellerCode;
	}

	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getInvalidTime() {
		return invalidTime;
	}

	public void setInvalidTime(Long invalidTime) {
		this.invalidTime = invalidTime;
	}

	public String getRequestBiz() {
		return requestBiz;
	}

	public void setRequestBiz(String requestBiz) {
		this.requestBiz = requestBiz;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

}
