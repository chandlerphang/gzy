package com.cactus.guozy.core.service;

import java.util.Map;

import com.cactus.guozy.core.dto.PayRequestParam;

public interface PayRouteService {

    /**
     * 组装支付请求报文（http入口）
     * @param payRequestParam
     * @return
     */
    Map<String, Object> getPayRetMap(PayRequestParam payRequestParam);

}
