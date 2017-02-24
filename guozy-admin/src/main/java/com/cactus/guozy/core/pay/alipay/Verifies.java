package com.cactus.guozy.core.pay.alipay;

import java.util.Map;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;

public class Verifies extends Component {

    Verifies(Alipay alipay){
        super(alipay);
    }

    /**
     * RSA验证通知参数是否合法(如APP服务器通知)
     * @param notifyParams 通知参数
     * @return 合法返回true，反之false
     */
    public Boolean rsa(Map<String, String> notifyParams){
    	String charset = notifyParams.get(AlipayField.CHARSET.field());
    	try {
			return AlipaySignature.rsaCheckV1(notifyParams, 
					alipay.aliPubKey, 
					charset, 
					notifyParams.get(AlipayField.SIGN_TYPE.field()));
		} catch (AlipayApiException e) {
			return false;
		}
    }

}
