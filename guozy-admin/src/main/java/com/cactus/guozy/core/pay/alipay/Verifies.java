package com.cactus.guozy.core.pay.alipay;

import static com.cactus.guozy.common.utils.Preconditions.checkNotNullAndEmpty;

import java.util.Map;
import java.util.Objects;

import com.cactus.guozy.common.encrypt.RSA;

public class Verifies extends Component {

    Verifies(Alipay alipay){
        super(alipay);
    }

    /**
     * MD5验证通知参数签名是否合法(WEB，WAP，退款服务器通知)
     * @param notifyParams 通知参数
     * @return 合法返回true，反之false
     */
    public Boolean md5(Map<String, String> notifyParams){
        Map<String, String> validParams = filterSigningParams(notifyParams);
        String signing = buildSignString(validParams);
        String signed = md5(signing);
        return Objects.equals(notifyParams.get(AlipayField.SIGN.field()), signed);
    }

    /**
     * RSA验证通知参数是否合法(如APP服务器通知)
     * @param notifyParams 通知参数
     * @return 合法返回true，反之false
     */
    public Boolean rsa(Map<String, String> notifyParams){
        String expectSign = notifyParams.get(AlipayField.SIGN.field());
        Map<String, String> validParams = filterSigningParams(notifyParams);
        String signing = buildSignString(validParams);
        checkNotNullAndEmpty(alipay.appPubKey, "app public key can't be empty before rsa verify.");
        return RSA.verify(signing, expectSign, alipay.appPubKey, alipay.charset);
    }

    /**
     * 验证支付通知ID是否合法
     * @param notifyId 通知ID，后置通知中会有
     * @return 合法返回true，反之false
     */
    public Boolean notifyId(String notifyId){
//        String url = Alipay.GATEWAY +
//                "&" + AlipayField.SERVICE.field() + "=" + Service.NOTIFY_VERIFY.value() +
//                "&" + AlipayField.PARTNER.field() + "=" + alipay.merchantId +
//                "&" + AlipayField.NOTIFY_ID.field() + "=" + notifyId;
//        String resp = Http.get(url).request();
//        return "true".equalsIgnoreCase(resp);
    	
    	return false;
    }
}
