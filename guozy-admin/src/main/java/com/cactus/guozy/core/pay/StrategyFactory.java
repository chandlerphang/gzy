package com.cactus.guozy.core.pay;

import java.util.HashMap;
import java.util.Map;

import com.cactus.guozy.core.pay.alipay.AlipayAppStrategy;
import com.cactus.guozy.core.pay.wechat.WechatPayAppStrategy;

/**
 * 支付策略工厂
 */
public class StrategyFactory {

    private static Map<Integer, PayStrategy> strategyMap = new HashMap<>();

    static {
        strategyMap.put(PayType.ALIPAY_APP.value(), new AlipayAppStrategy());
        strategyMap.put(PayType.WECHAT_APP.value(), new WechatPayAppStrategy());
    }

    private StrategyFactory() { }

    private static class InstanceHolder {
        public static StrategyFactory instance = new StrategyFactory();
    }

    public static StrategyFactory getInstance() {
        return InstanceHolder.instance;
    }

    public PayStrategy creator(Integer type) {
        return strategyMap.get(type);
    }

}
