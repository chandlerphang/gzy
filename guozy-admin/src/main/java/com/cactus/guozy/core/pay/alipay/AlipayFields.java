package com.cactus.guozy.core.pay.alipay;

import java.util.Arrays;
import java.util.List;

public class AlipayFields {

    private AlipayFields() { }
    
    /**
     * APP支付服务器通知参数
     */
    public static final List<AlipayField> APP_PAY_NOTIFY = Arrays.asList(
    		AlipayField.RECEIPT_AMOUT,
    		AlipayField.NOTIFY_TIME, AlipayField.NOTIFY_TIYE, AlipayField.NOTIFY_ID,
    		AlipayField.APP_ID, AlipayField.AUTH_APP_ID, AlipayField.CHARSET, AlipayField.VERSION,
    		AlipayField.SIGN_TYPE, AlipayField.SIGN, AlipayField.TRADE_NO,
    		AlipayField.OUT_TRADE_NO, AlipayField.OUT_BIZ_NO, AlipayField.BUYER_ID,
    		AlipayField.BUYER_LOGON_ID, AlipayField.SELLER_ID, AlipayField.SELLER_EMAIL,
    		AlipayField.TRADE_STATUS, AlipayField.TOTAL_AMOUNT, AlipayField.INVOICE_AMOUNT,
    		AlipayField.BUYER_PAY_AMOUNT, AlipayField.POINT_AMOUNT, AlipayField.REFUND_FEE,
    		AlipayField.SUBJECT, AlipayField.BODY, AlipayField.GMT_CREATE,
    		AlipayField.GMT_PAYMENT, AlipayField.GMT_REFUND, AlipayField.GMT_CLOSE,
    		AlipayField.FUND_BILL_LIST, AlipayField.PASSBACK_PARAMS, AlipayField.VOUCHER_DETAIL_LIST
    );

    /**
     * 退款服务器通知参数
     */
    public static final List<AlipayField> REFUND_NOTIFY = Arrays.asList(
            AlipayField.SIGN, AlipayField.SIGN_TYPE, AlipayField.NOTIFY_ID, AlipayField.NOTIFY_TIYE,
            AlipayField.NOTIFY_TIME, AlipayField.BATCH_NO, AlipayField.SUCCESS_NUM, AlipayField.RESULT_DETAILS
    );
}
