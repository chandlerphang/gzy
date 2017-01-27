package com.cactus.guozy.core.pay.wechat;

import java.util.Map;
import java.util.TreeMap;

import com.cactus.guozy.common.encrypt.MD5;
import com.cactus.guozy.common.http.Http;
import com.cactus.guozy.common.http.Https;
import com.cactus.guozy.common.json.Jsons;
import com.cactus.guozy.common.utils.Maps;
import com.cactus.guozy.common.utils.Strings;
import com.cactus.guozy.common.xml.XmlReaders;

public abstract class Component {

    protected Wepay wepay;

    protected Component(Wepay wepay){
        this.wepay = wepay;
    }

    protected Map<String, String> doPost(final String url, final Map<String, String> params){
        String requestBody = Maps.toXml(params);
        String resp = Http.post(url).ssl().body(requestBody).request();
        Map<String, String> respMap = toMap(resp.replaceAll("(\\r|\\n)", ""));
        doVerifySign(respMap);
        return respMap;
    }

    protected <T> T doHttpsPost(final String url, final Map<String, String> params, Class<T> respClazz){
        String requestBody = Maps.toXml(params);
        String resp = Https.post(url).body(requestBody)
                .ssLSocketFactory(wepay.getSslSocketFactory()).request();
        Map<String, String> respMap = toMap(resp.replaceAll("(\\r|\\n)", ""));
        doVerifySign(respMap);
        return Jsons.DEFAULT.fromJson(Jsons.DEFAULT.toJson(respMap), respClazz);
    }


    /**
     * 将微信XML转换为Map
     * @param xml xml字符串
     * @return Map对象，或抛WechatException
     */
    protected Map<String, String> toMap(final String xml) {
        XmlReaders readers = readResp(xml);
        return Maps.toMap(readers);
    }

    /**
     * 读取微信xml响应
     * @param xml xml字符串
     * @return 若成功，返回对应Reader，反之抛WepayException
     */
    private XmlReaders readResp(final String xml) {
        XmlReaders readers = XmlReaders.create(xml, wepay.respEncode);
        String returnCode = readers.getNodeStr(WepayField.RETURN_CODE);
        if (WepayField.SUCCESS.equals(returnCode)){
            String resultCode = readers.getNodeStr(WepayField.RESULT_CODE);
            if (WepayField.SUCCESS.equals(resultCode)){
                return readers;
            }
            throw new WepayException(
                    readers.getNodeStr(WepayField.ERR_CODE),
                    readers.getNodeStr(WepayField.ERR_CODE_DES));
        }
        throw new WepayException(
                readers.getNodeStr(WepayField.RETURN_CODE),
                readers.getNodeStr(WepayField.RETURN_MSG));
    }

    /**
     * 构建配置参数
     * @param params 参数
     */
    protected void buildConfigParams(final Map<String, String> params){
        params.put(WepayField.APP_ID, wepay.getAppId());
        params.put(WepayField.MCH_ID, wepay.getMchId());
    }

    /**
     * 构建签名参数
     * @param params 支付参数
     */
    protected void buildSignParams(final Map<String, String> params) {
        String sign = doSign(params);
        put(params, WepayField.SIGN, sign);
    }

    /**
     * 支付请求前签名
     * @param params 参数(已经升序, 排出非空值和sign)
     * @return MD5的签名字符串(大写)
     */
    protected String doSign(final Map<String, String> params) {
        StringBuilder signing = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!Strings.isNullOrEmpty(entry.getValue())){
                signing.append(entry.getKey()).append('=').append(entry.getValue()).append("&");
            }
        }

        // append key
        signing.append("key=").append(wepay.getAppKey());

        // md5
        return MD5.generate(signing.toString(), false).toUpperCase();
    }

    /**
     * 校验
     * @param xml 微信xml内容
     * @return 校验成功返回true，反之false
     */
    protected Boolean doVerifySign(final String xml) {
        return doVerifySign(toMap(xml));
    }

    /**
     * 校验参数
     * @param data 待校验参数
     * @return 校验成功返回true，反之false
     */
    protected Boolean doVerifySign(final Map<String, ?> data) {
        String actualSign = String.valueOf(data.get(WepayField.SIGN));
        Map<String, String> signingMap = filterSignParams(data);
        String expectSign = doSign(signingMap);
        return expectSign.equals(actualSign);
    }

    /**
     * 过滤签名参数(升序，排出空值，sign)
     * @param params 待校验参数
     * @return 过滤后的参数
     */
    protected Map<String, String> filterSignParams(final Map<String, ?> params) {
        Map<String, String> validParams = new TreeMap<>();

        for (Map.Entry<String, ?> param : params.entrySet()){
            if (WepayField.SIGN.equals(param.getKey())
                    || param.getValue() == null
                    || "".equals(String.valueOf(param.getValue()))){
                continue;
            }
            validParams.put(param.getKey(), String.valueOf(param.getValue()));
        }

        return validParams;
    }

    protected void putIfNotEmpty(final Map<String, String> map, String field, String paramValue) {
        if (!Strings.isNullOrEmpty(paramValue)){
            map.put(field, paramValue);
        }
    }

    protected void put(final Map<String, String> map, String field, String paramValue){
        map.put(field, paramValue);
    }
}
